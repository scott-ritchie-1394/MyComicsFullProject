package com.example.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mycomics.*;
import com.example.android.mycomics.ComicCharacter;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 3/10/2016.
 */
public class CharacterAdapter extends ArrayAdapter<ComicCharacter> {

    public interface CharacterDeletedCallback {
        void onCharacterDeleted(ComicCharacter comicCharacter);
    }

    CharacterDeletedCallback mCallback;
    Context context;

    public CharacterAdapter(Context context, List<ComicCharacter> objects, CharacterDeletedCallback mCallback) {
        super(context, 0, objects);
        this.mCallback = mCallback;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        final ComicCharacter currentComicCharacter = getItem(position);
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.character_layout, parent, false);
//        }
//        TextView charName = (TextView) convertView.findViewById(R.id.textView);
//        final ImageView charImage = (ImageView) convertView.findViewById(R.id.imageView);
//        ViewGroup.LayoutParams params = charImage.getLayoutParams();
//        int[] size = getScreenSize();
//        params.height = size[0];
//        params.width = size[1];
//
//        charImage.requestLayout();
//        /*charImage.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {//To set custom image
//                MainActivity.currentUserEdit = charImage;
//                MainActivity.currentSaveComicCharacter = getItem(position);
//                userSetImage();
//            }
//        });*/
//        charName.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {//To set custom image
//                //Toast.makeText(getContext(), myCharacter.getCharacterName(), Toast.LENGTH_SHORT).show();
//                nextActivity(getItem(position).getCharacterName());
//            }
//        });
//        charName.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                mCallback.onCharacterDeleted(currentComicCharacter);
////                MainActivity.remove(position, context);
//                return true;
//            }
//        });
//
//        /*Bitmap bm = myCharacter.getImage();
//        if (bm != null) {
//            charImage.setImageBitmap(bm);
//        } else {
//            charImage.setImageResource(R.drawable.addimage);
//        }*/
//        List<Bitmap> bitmaps = readSeriesImages(currentComicCharacter.getCharacterName());
//        Drawable[] images = new Drawable[bitmaps.size()];
//        if (images.length != 0) {
//            for (int i = 0; i < bitmaps.size(); i++) {
//                images[i] = new BitmapDrawable(bitmaps.get(i));
//        }
//            CyclicTransitionDrawable transitionDrawable = new CyclicTransitionDrawable(images);
//            charImage.setImageDrawable(transitionDrawable);
//            transitionDrawable.startTransition(2000, 5000);
//        } else if (images.length == 1) {
//            charImage.setImageBitmap(bitmaps.get(0));
//        } else {
//            charImage.setImageResource(R.drawable.addimage);
//        }
//        charName.setText(currentComicCharacter.getCharacterName());
        return convertView;
    }

    public void userSetImage() {//Called from clicking the image view.
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        ((Activity) context).startActivityForResult(galleryIntent, 1);
    }


}
