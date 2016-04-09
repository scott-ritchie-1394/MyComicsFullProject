package com.example.android;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mycomics.ComicCharacter;
import com.example.android.mycomics.R;

import java.util.List;

/**
 * Created by Scott on 4/8/2016.
 */
public class CharacterViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener, View.OnLongClickListener {

    TextView charName;
    ImageView charImage;
    CharacterAdapterRecycler.CharacterCallback mCallback;
    ComicCharacter mComicCharacter;

    public CharacterViewHolder(View itemView, CharacterAdapterRecycler.CharacterCallback mCallback) {
        super(itemView);
        this.mCallback = mCallback;
    }

    public void bind(ComicCharacter comicCharacter) {
        this.mComicCharacter = comicCharacter;

        charName = (TextView) itemView.findViewById(R.id.textView);
        charImage = (ImageView) itemView.findViewById(R.id.imageView);
        ViewGroup.LayoutParams params = charImage.getLayoutParams();
        Pair<Integer, Integer> size = ComicApp.instance().getScreenSize();
        params.height = size.first;
        params.width = size.second;

        charImage.requestLayout();
        /*charImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//To set custom image
                MainActivity.currentUserEdit = charImage;
                MainActivity.currentSaveComicCharacter = getItem(position);
                userSetImage();
            }
        });*/
        charName.setOnClickListener(this);
        charName.setOnLongClickListener(this);

        /*Bitmap bm = myCharacter.getImage();
        if (bm != null) {
            charImage.setImageBitmap(bm);
        } else {
            charImage.setImageResource(R.drawable.addimage);
        }*/
        List<Bitmap> bitmaps = ComicUtils.readSeriesImages(mComicCharacter.getCharacterName());
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
        charName.setText(mComicCharacter.getCharacterName());
    }

    @Override
    public void onClick(View v) {

        mCallback.onCharacterSelected(mComicCharacter);
    }

    @Override
    public boolean onLongClick(View v) {
        mCallback.onCharacterDeleted(mComicCharacter);
        return true;
    }
}
