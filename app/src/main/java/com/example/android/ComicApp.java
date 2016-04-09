package com.example.android;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Scott on 4/8/2016.
 */
public class ComicApp extends Application {

    static ComicApp instance;
    Pair<Integer, Integer> screenSize;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static ComicApp instance() {
        return instance;
    }

    public Pair<Integer, Integer> getScreenSize() {
        if (screenSize == null) {
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            double height = metrics.heightPixels;
            height = height / 4.6;
            double width = height * 0.654;
            int newHeight = (int) Math.round(height);
            int newWidth = (int) Math.round(width);
            screenSize = new Pair<>(newHeight, newWidth);
        }

        return screenSize;
    }
}
