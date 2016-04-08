package com.example.android.mycomics;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.CharacterAdapter;
import com.example.android.SeriesActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static public String filePath = "";
    static public String filePathPrime = "";
    static List<character> characters = new ArrayList<>();//Holds our characters
    static CharacterAdapter adapter;
    ListView listView;
    public static ImageView currentUserEdit;
    public static character currentSaveCharacter;

    Context context = this;
    String nextDisplayName = "";//Used to hold String for new character as input by user
    int comicHeightInPx;//For dp conversion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gets height using dp
        comicHeightInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        filePathPrime = this.getFilesDir().getPath().toString();
        filePath = filePathPrime + "/saveFile.txt";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new CharacterAdapter(this, characters);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        try {
            readCharacters();//Should build our array of Characters from file.
        } catch (Exception e) {
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
            Log.d("READ ERROR", e.toString());
        }
    }

    //Creates dialoge for adding a character
    public void characterDialog(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText txtInput = new EditText(this);
        dialogBuilder.setTitle("Add Character:");
        dialogBuilder.setView(txtInput);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int firstSize = characters.size();
                nextDisplayName = txtInput.getText().toString();//Gets name of character to add
                //Next three lines update variables and saves them.
                character toAdd = new character(nextDisplayName);
                //adapter.add(toAdd);
                characters.add(toAdd);
                adapter.notifyDataSetChanged();
                //adapter.add(characters.get(characters.size() - 1));
                /*if (characters.size() > firstSize + 1) {
                    characters.remove(characters.size() - 1);
                }*/
                saveCharacters(filePath, context);
                restartActivity();
            }
        });
        AlertDialog dialogCharacterName = dialogBuilder.create();
        dialogCharacterName.show();
    }

    public static void remove(int myPosition, final Context fromCallContext) {
        if (characters.size() == 0) {
            return;
        }
        final int position = myPosition;
        final Context myContext = fromCallContext;
        boolean test = false;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(myContext);
        dialogBuilder.setTitle("Are you sure you want to delete?");
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File f = new File(filePathPrime + "/" + characters.get(position).getCharacterName() + ".txt");
                boolean deleted = f.delete();
                Log.d("HERE", String.valueOf(deleted));
                adapter.remove(characters.get(position));
                characters.remove(position);
                saveCharacters(filePath, myContext);
            }
        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        dialogBuilder.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editName(position, myContext);
            }
        });
        AlertDialog dialogCharacterName = dialogBuilder.create();
        dialogCharacterName.show();
    }

    public static void editName(int myPosition, Context fromCallContext) {
        final Context myContext = fromCallContext;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(myContext);
        final EditText txtInput = new EditText(myContext);
        final int position = myPosition;
        dialogBuilder.setTitle("New Character Name:");
        dialogBuilder.setView(txtInput);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Next three lines update variables and saves them.
                characters.get(position).setCharacterName(txtInput.getText().toString());
                adapter.notifyDataSetChanged();
                saveCharacters(filePath, myContext);
            }
        });
        AlertDialog dialogCharacterName = dialogBuilder.create();
        dialogCharacterName.show();
    }

    private static void saveCharacters(String globalFilePath, Context context) {//Writes our characters array to file using serializable.
        File f = new File(globalFilePath);
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(f);
            out = new ObjectOutputStream(fos);
            out.writeObject(characters);
            out.close();
        } catch (Exception e) {
            Toast.makeText(context,
                    "SAVEERROR", Toast.LENGTH_LONG).show();
            Log.d("SAVEERROR", e.toString());
        }
    }

    //Sets image and saves it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);
        try {
            // When an Image is picked
            if (resultCode == RESULT_OK && null != returnedIntent) {
                // Get the Image from data
                Uri imageLocation = returnedIntent.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageLocation);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                selectedImage = getResizedBitmap(selectedImage, 1080 / 4, 1920 / 4);
                currentUserEdit.setImageBitmap(selectedImage);
                currentSaveCharacter.setImage(selectedImage);
                saveCharacters(filePath, context);
            }
        } catch (Exception e) {
            Toast.makeText(this, "ImageGet ERROR", Toast.LENGTH_LONG)
                    .show();
            Log.d("ImageGet ERROR", e.toString());
        }

    }

    //Resizes a bitmap
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // Create a matrix for manipulation
        Matrix matrix = new Matrix();
        // Resize the bitmap
        matrix.setRectToRect(new RectF(0, 0, width, height), new RectF(0, 0, newWidth, newHeight), Matrix.ScaleToFit.CENTER);
        // Return a newly created bitmap
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    protected void readCharacters() {//Reads characters array from file
        try {
            File f = new File(filePath);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fis);
            characters = (List<character>) in.readObject();
            in.close();
        } catch (Exception e) {
        }
        adapter.clear();
        adapter.addAll(characters);
    }

    public void buildDefault(View v) {/*
        characters.clear();
        System.out.println(characters);
        character Batman = new character("Batman");
        character Superman = new character("Superman");
        character wonderWoman = new character("Wonder Woman");
        character avengers = new character("Avengers");
        character justicLeague = new character("Justice League");
        character spidreman = new character("Spider-Man");
        character harelyQuinn = new character("Harley Quinn");
        characters.add(Batman);
        characters.add(Superman);
        characters.add(wonderWoman);
        characters.add(avengers);
        characters.add(justicLeague);
        characters.add(spidreman);
        characters.add(harelyQuinn);
        saveCharacters(context);
        adapter.notifyDataSetChanged();
        System.out.println(characters);*/
    }

    public void clearData(View v) {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
        characters.clear();
        saveCharacters(filePath, context);
        adapter.notifyDataSetChanged();

    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }


    //This methods below are either not working correctly at the moment or are just for refrence
    //Deletes the most recent characterview, for testing purposes only.  Not final method
    //Doesnt currently work, avoid using for now
    //Method not edited out so remove button can stay.
    public void removeTextView(View view) {
        /*LinearLayout linearLayout = (LinearLayout) findViewById(R.id.parentLinear);
        if (TextViewCount != 0) {
            LinearLayout toRemove = (LinearLayout) findViewById(TextViewCount - 1);
            linearLayout.removeView(toRemove);
            characterNames.remove(TextViewCount - 1);
            characters.remove(TextViewCount -1);
            saveCharacters(this);
            TextViewCount--;
        }
    */
    }

    //Saves our characters to prefrences
    //In old version, I save via preferences, keeping code for reference if I need it later.
    /*private void saveCharacters(Context context) {
        SharedPreferences myPrefs;
        SharedPreferences.Editor edit;
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        edit = myPrefs.edit();
        String string = listToString(characterNames);
        edit.putString(prefKey, string);
        edit.commit();
    }*/

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}

