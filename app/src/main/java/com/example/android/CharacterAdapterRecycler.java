package com.example.android;

import android.view.View;

import com.example.android.mycomics.ComicCharacter;
import com.example.android.mycomics.R;

/**
 * Created by Scott on 4/8/2016.
 */
public class CharacterAdapterRecycler extends BaseRecyclerViewAdapter<CharacterViewHolder, ComicCharacter> {

    public interface CharacterCallback {
        void onCharacterSelected(ComicCharacter comicCharacter);

        void onCharacterDeleted(ComicCharacter comicCharacter);
    }

    CharacterCallback mCallback;

    public CharacterAdapterRecycler(CharacterCallback mCallback) {
        super();
        this.mCallback = mCallback;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.character_layout;
    }

    @Override
    protected CharacterViewHolder inflateViewHolder(View view) {
        return new CharacterViewHolder(view, mCallback);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {
        holder.bind(items.get(position));
    }
}
