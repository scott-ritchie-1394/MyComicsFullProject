package com.example.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mycomics.MainActivity;
import com.example.android.mycomics.R;
import com.example.android.mycomics.Series;
import com.example.android.mycomics.character;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Scott on 3/10/2016.
 */
public class CharacterAdapter extends ArrayAdapter<character> {
    Context context;
    character myCharacter = null;

    public CharacterAdapter(Context mContext, List<character> characters) {
        super(mContext, 0, characters);
        context = mContext;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        myCharacter = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.character_layout, parent, false);
        }
        TextView charName = (TextView) convertView.findViewById(R.id.textView);
        final ImageView charImage = (ImageView) convertView.findViewById(R.id.imageView);
        ViewGroup.LayoutParams params = charImage.getLayoutParams();
        int[] size = getScreenSize();
        params.height = size[0];
        params.width = size[1];

        charImage.requestLayout();
        /*charImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//To set custom image
                MainActivity.currentUserEdit = charImage;
                MainActivity.currentSaveCharacter = getItem(position);
                userSetImage();
            }
        });*/
        charName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//To set custom image
                //Toast.makeText(getContext(), myCharacter.getCharacterName(), Toast.LENGTH_SHORT).show();
                nextActivity(getItem(position).getCharacterName());
            }
        });
        charName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.remove(position, context);
                return true;
            }
        });

        /*Bitmap bm = myCharacter.getImage();
        if (bm != null) {
            charImage.setImageBitmap(bm);
        } else {
            charImage.setImageResource(R.drawable.addimage);
        }*/
        List<Bitmap> bitmaps = readSeriesImages(myCharacter.getCharacterName());
        Drawable[] images = new Drawable[bitmaps.size()];
        if (images.length != 0) {
            for (int i = 0; i < bitmaps.size(); i++) {
                images[i] = new BitmapDrawable(bitmaps.get(i));
        }
            CyclicTransitionDrawable transitionDrawable = new CyclicTransitionDrawable(images);
            charImage.setImageDrawable(transitionDrawable);
            transitionDrawable.startTransition(2000, 5000);
        } else if (images.length == 1) {
            charImage.setImageBitmap(bitmaps.get(0));
        } else {
            charImage.setImageResource(R.drawable.addimage);
        }
        charName.setText(myCharacter.getCharacterName());
        return convertView;
    }

    public void userSetImage() {//Called from clicking the image view.
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        ((Activity) context).startActivityForResult(galleryIntent, 1);
    }

    public void nextActivity(String characterName) {
        Intent intent = new Intent(context, SeriesActivity.class);
        intent.putExtra("currentCharName", characterName);//So we know what character we are dealing with
        context.startActivity(intent);
    }

    public int[] getScreenSize() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        double height = metrics.heightPixels;
        height = height / 4.6;
        double width = height * 0.654;
        int newHeight = (int) Math.round(height);
        int newWidth = (int) Math.round(width);
        int[] size = new int[2];
        size[0] = newHeight;
        size[1] = newWidth;
        return size;
    }

    protected List<Bitmap> readSeriesImages(String character) {//Reads characters array from file
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
