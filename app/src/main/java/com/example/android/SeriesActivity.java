package com.example.android;

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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.mycomics.IssueActivity;
import com.example.android.mycomics.MainActivity;
import com.example.android.mycomics.R;
import com.example.android.mycomics.Series;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

//NOTE: Currently does not support custom Series image. Parts of skeleton for that code are here.
public class SeriesActivity extends AppCompatActivity implements SeriesAdapterRecycler.SeriesCallback {//Works similaryly to MainActivity
    public static String currentCharacter = "";
    public static String filePath = "";
    static List<Series> series = new ArrayList<>();//Holds our series
    static SeriesAdapterRecycler seriesAdapter;
    RecyclerView listView;
    public static ImageView currentUserEdit;
    public static Series currentSaveSeries;

    Context context = this;
    String nextDisplayName = "";//Used to hold String for new ComicCharacter as input by user


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gets height using dp
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);
        Intent i = getIntent();
        currentCharacter = (String) i.getSerializableExtra("currentCharName");
        filePath = this.getFilesDir().getPath().toString() + "/" + currentCharacter + ".txt";
        seriesAdapter = new SeriesAdapterRecycler(this);

        listView = (RecyclerView) findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(seriesAdapter);
        try {
            ComicUtils.readSeries(filePath, seriesAdapter);
        } catch (Exception e) {
            Log.d("READ ERROR", e.toString());
        }

        setUpToolBar();

    }

    //Creates dialoge for adding a ComicCharacter
    public void seriesDialog(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText txtInput = new EditText(this);
        dialogBuilder.setTitle("Add Series:");
        dialogBuilder.setView(txtInput);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int firstSize = series.size();
                nextDisplayName = txtInput.getText().toString();//Gets name of ComicCharacter to add
                //Next three lines update variables and saves them.
                series.add(new Series(nextDisplayName));
                seriesAdapter.add(series.get(series.size() - 1));
                if (series.size() > firstSize + 1) {
                    series.remove(series.size() - 1);
                }
                saveSeries(context);
            }
        });
        AlertDialog dialogCharacterName = dialogBuilder.create();
        dialogCharacterName.show();
    }

    private static void saveSeries(Context context) {//Writes our characters array to file using serializable.
        File f = new File(filePath);
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(f);
            out = new ObjectOutputStream(fos);
            out.writeObject(seriesAdapter.getItems());
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
                currentSaveSeries.setImage(selectedImage);
                saveSeries(context);
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

    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My " + currentCharacter + " Series");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void remove(final Series series, final Context fromCallContext) {
        if (seriesAdapter.size() == 0) {
            return;
        }

        final Context myContext = fromCallContext;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(myContext);
        dialogBuilder.setTitle("Are you sure you want to delete?");
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    File f = new File(filePath);
                    boolean deleted = f.delete();
                    Log.d("HERE", String.valueOf(deleted));
                    seriesAdapter.removeItem(series);
                    saveSeries(myContext);
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
                editName(series, myContext);
            }
        });
        AlertDialog dialogCharacterName = dialogBuilder.create();
        dialogCharacterName.show();
    }

    public static void editName(final Series series, final Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final EditText txtInput = new EditText(context);
        dialogBuilder.setTitle("New Series Name:");
        dialogBuilder.setView(txtInput);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Next three lines update variables and saves them.
                seriesAdapter.editSeriesName(series, txtInput.getText().toString());
                saveSeries(context);
            }
        });
        AlertDialog dialogCharacterName = dialogBuilder.create();
        dialogCharacterName.show();
    }

    @Override
    public void onSeriesSelected(Series series) {
        Intent intent = new Intent(context, IssueActivity.class);
        intent.putExtra("arg_use_expansion", true);
        intent.putExtra("currentSeries", series);
        intent.putExtra("ComicCharacter", SeriesActivity.currentCharacter);//So we know what ComicCharacter we are dealing with
        context.startActivity(intent);
    }

    @Override
    public void onSeriesDeleted(Series series) {
        remove(series, this);
    }

    @Override
    public void onImageSelected(Series series, View v) {
        currentUserEdit = (ImageView) v;
        currentSaveSeries = series;
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) context).startActivityForResult(galleryIntent, 1);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            goToMain();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            goToMain();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

