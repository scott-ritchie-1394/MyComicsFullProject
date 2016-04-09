package com.example.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;
import android.widget.Toast;

import com.example.android.mycomics.ComicCharacter;
import com.example.android.mycomics.Series;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 4/8/2016.
 */
public final class ComicUtils {

    private ComicUtils() {
    }

    public static List<Bitmap> readSeriesImages(String character) {//Reads characters array from file
        List<Bitmap> toReturn = new ArrayList<>();
        String filePath = "/data/data/com.example.android.mycomics/files/" + character + ".txt";
        try {
            File f = new File(filePath);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fis);
            List<Series> series = (List<Series>) in.readObject();
            in.close();
            for (int i = 0; i < series.size(); i++) {
                Bitmap bm = series.get(i).getImage();
                if (bm != null) {
                    toReturn.add(bm);
                }
            }
        } catch (Exception e) {

        }
        return toReturn;
    }

    public static void saveCharacters(String globalFilePath, Context context, CharacterAdapterRecycler adapter) {//Writes our comicCharacters array to file using serializable.
        File f = new File(globalFilePath);
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(f);
            out = new ObjectOutputStream(fos);
            out.writeObject(adapter.getItems());
            out.close();
        } catch (Exception e) {
            Toast.makeText(context,
                    "SAVEERROR", Toast.LENGTH_LONG).show();
            Log.d("SAVEERROR", e.toString());
        }
    }

    public static void startSeriesActivity(String characterName, Context context) {
        Intent intent = new Intent(context, SeriesActivity.class);
        intent.putExtra("currentCharName", characterName);//So we know what ComicCharacter we are dealing with
        context.startActivity(intent);
    }

    public static void readCharacters(String filePath, CharacterAdapterRecycler adapter) {//Reads comicCharacters array from file
        List<ComicCharacter> comicCharacters = null;
        try {
            File f = new File(filePath);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fis);
            comicCharacters = (List<ComicCharacter>) in.readObject();
            in.close();
        } catch (Exception e) {
        }
        adapter.swap(comicCharacters);
    }

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
}
