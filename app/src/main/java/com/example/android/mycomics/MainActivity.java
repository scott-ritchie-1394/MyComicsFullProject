package com.example.android.mycomics;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.CharacterAdapterRecycler;
import com.example.android.ComicUtils;

import java.io.File;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements CharacterAdapterRecycler.CharacterCallback {
    static public String filePath = "";
    static public String filePathPrime = "";
    static CharacterAdapterRecycler adapter;
    RecyclerView listView;
    public static ImageView currentUserEdit;
    public static ComicCharacter currentSaveComicCharacter;

    Context context = this;
    String nextDisplayName = "";//Used to hold String for new ComicCharacter as input by user
    int comicHeightInPx;//For dp conversion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gets height using dp
        comicHeightInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
        filePathPrime = this.getFilesDir().getPath().toString();
        filePath = filePathPrime + "/saveFile.txt";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new CharacterAdapterRecycler(this);
        listView = (RecyclerView) findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);
        try {
            ComicUtils.readCharacters(filePath, adapter);//Should build our array of Characters from file.
        } catch (Exception e) {
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
            Log.d("READ ERROR", e.toString());
        }
    }

    //Creates dialoge for adding a ComicCharacter
    public void characterDialog(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText txtInput = new EditText(this);
        dialogBuilder.setTitle("Add Character:");
        dialogBuilder.setView(txtInput);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int firstSize = adapter.size();
                nextDisplayName = txtInput.getText().toString();
                ComicCharacter toAdd = new ComicCharacter(nextDisplayName);
                adapter.add(toAdd);
                ComicUtils.saveCharacters(filePath, context, adapter);
            }
        });
        AlertDialog dialogCharacterName = dialogBuilder.create();
        dialogCharacterName.show();
    }

    public static void remove(final ComicCharacter comicCharacter, final Context fromCallContext) {
        if (adapter.size() == 0) {
            return;
        }

        final Context myContext = fromCallContext;
        boolean test = false;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(myContext);
        dialogBuilder.setTitle("Are you sure you want to delete?");
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    File f = new File(filePathPrime + "/" + comicCharacter.getCharacterName() + ".txt");
                    boolean deleted = f.delete();
                    Log.d("HERE", String.valueOf(deleted));
                    adapter.removeItem(comicCharacter);
                    ComicUtils.saveCharacters(filePath, myContext, adapter);
                } catch (Exception e) {
                    Log.d("ERRROR", e.toString());
                }
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
                editName(comicCharacter, myContext);
            }
        });
        AlertDialog dialogCharacterName = dialogBuilder.create();
        dialogCharacterName.show();
    }

    public static void editName(final ComicCharacter comicCharacter, Context fromCallContext) {
        final Context myContext = fromCallContext;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(myContext);
        final EditText txtInput = new EditText(myContext);
        dialogBuilder.setTitle("New ComicCharacter Name:");
        dialogBuilder.setView(txtInput);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Next three lines update variables and saves them.
                comicCharacter.setCharacterName(txtInput.getText().toString());
                ComicUtils.saveCharacters(filePath, myContext, adapter);
            }
        });
        AlertDialog dialogCharacterName = dialogBuilder.create();
        dialogCharacterName.show();
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
                selectedImage = ComicUtils.getResizedBitmap(selectedImage, 1080 / 4, 1920 / 4);
                currentUserEdit.setImageBitmap(selectedImage);
                currentSaveComicCharacter.setImage(selectedImage);
                ComicUtils.saveCharacters(filePath, context, adapter);
            }
        } catch (Exception e) {
            Toast.makeText(this, "ImageGet ERROR", Toast.LENGTH_LONG)
                    .show();
            Log.d("ImageGet ERROR", e.toString());
        }

    }

    @Override
    public void onCharacterDeleted(ComicCharacter comicCharacter) {
        remove(comicCharacter, this);
    }

    @Override
    public void onCharacterSelected(ComicCharacter comicCharacter) {
        ComicUtils.startSeriesActivity(comicCharacter.getCharacterName(), context);
    }

}
//Saves our comicCharacters to prefrences
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
