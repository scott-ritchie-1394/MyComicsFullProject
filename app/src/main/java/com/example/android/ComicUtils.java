package com.example.android;

import android.graphics.Bitmap;

import com.example.android.mycomics.Series;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
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
}
