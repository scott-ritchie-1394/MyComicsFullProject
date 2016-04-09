package com.example.android.mycomics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//test

/**
 * Created by Scott on 3/7/2016.
 */

//Currently class just holds name and image for our charcter.
//Originally the class held an array of Series type, but when
//I tried to write to file, the Series did not save, so currently
//I write a seperate Series array to a file named after the ComicCharacter.
public class ComicCharacter implements Serializable {
    private String characterName;
    private byte[] byteArray = null;//For image associated with ComicCharacter. Bitmap not serializable so must convert from bitmap to byteArray.


    public ComicCharacter(String name) {
        characterName = name;
    }

    public void setCharacterName(String name) {
        characterName = name;
    }

    public String getCharacterName() {
        return characterName;
    }

    public Bitmap getImage() {//Returns our image as a bitmap
        if (byteArray != null) {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            return null;
        }
    }

    //Stores bitmap to byteArray.
    public void setImage(Bitmap b) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();
    }
}

