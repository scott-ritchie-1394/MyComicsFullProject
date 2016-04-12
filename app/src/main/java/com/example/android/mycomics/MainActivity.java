package com.example.android.mycomics;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.CharacterAdapterRecycler;
import com.example.android.ComicUtils;
import com.example.android.MovieActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filePathPrime = this.getFilesDir().getPath().toString();
        filePath = filePathPrime + "/saveFile.txt";
        adapter = new CharacterAdapterRecycler(this);
        listView = (RecyclerView) findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (16 * scale + 0.5f);
        ComicUtils.readCharacters(filePath, adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Characters");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigation_menu);
        }
        setupNavigationView();
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
        dialogBuilder.setTitle("New Character Name:");
        dialogBuilder.setView(txtInput);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Next three lines update variables and saves them.
                File old = new File(filePathPrime + "/" + comicCharacter.getCharacterName() + ".txt");
                File to = new File(filePathPrime + "/" + txtInput.getText().toString() + ".txt");
                if (old.exists()) {
                    old.renameTo(to);
                }
                old.delete();
                comicCharacter.setCharacterName(txtInput.getText().toString());
                adapter.notifyItemChanged(adapter.getItems().indexOf(comicCharacter));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupNavigationView() {
        NavigationView navView = (NavigationView) findViewById(R.id.navigation_view);
        //navView.inflateMenu(R.menu.drawer);
        MenuItem movies = navView.getMenu().findItem(R.id.movies);
        movies.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigate();
                return true;
            }
        });

    }

    public void navigate() {
        Intent intent = new Intent(context, MovieActivity.class);
        context.startActivity(intent);
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
