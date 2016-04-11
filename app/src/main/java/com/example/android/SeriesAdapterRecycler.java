package com.example.android;

import android.view.View;

import com.example.android.mycomics.R;
import com.example.android.mycomics.Series;

/**
 * Created by Scott on 4/9/2016.
 */
public class SeriesAdapterRecycler extends BaseRecyclerViewAdapter<SeriesViewHolder, Series> {
    public interface SeriesCallback {
        void onSeriesSelected(Series series);

        void onSeriesDeleted(Series series);

        void onImageSelected(Series series, View v);
    }

    SeriesCallback mCallback;

    public SeriesAdapterRecycler(SeriesCallback mCallback) {
        super();
        this.mCallback = mCallback;
    }

    public void editSeriesName(Series series, String newName) {
        series.setSeriesName(newName);
        notifyItemChanged(items.indexOf(series));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.character_layout;
    }

    @Override
    protected SeriesViewHolder inflateViewHolder(View v) {
        return new SeriesViewHolder(v, mCallback);
    }

    @Override
    public void onBindViewHolder(SeriesViewHolder holder, int position) {
        holder.bind(items.get(position));
    }
}
