package com.example.android;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mycomics.R;
import com.example.android.mycomics.Series;

/**
 * Created by Scott on 4/9/2016.
 */
public class SeriesViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener, View.OnLongClickListener {
    TextView seriesName;
    ImageView seriesImage;

    SeriesAdapterRecycler.SeriesCallback mCallback;
    Series mSeries;

    public SeriesViewHolder(View itemView, SeriesAdapterRecycler.SeriesCallback mCallback) {
        super(itemView);
        this.mCallback = mCallback;
    }

    public void bind(Series series) {
        this.mSeries = series;

        seriesName = (TextView) itemView.findViewById(R.id.textView);//?
        seriesImage = (ImageView) itemView.findViewById(R.id.imageView);
        ViewGroup.LayoutParams params = seriesImage.getLayoutParams();
        Pair<Integer, Integer> size = ComicApp.instance().getScreenSize();
        params.height = size.first;
        params.width = size.second;
        seriesImage.requestLayout();
        seriesImage.setOnClickListener(this);
        seriesName.setOnClickListener(this);
        seriesName.setOnLongClickListener(this);

        Bitmap bm = mSeries.getImage();
        if (bm != null) {
            seriesImage.setImageBitmap(bm);
        } else {
            seriesImage.setImageResource(R.drawable.addimage);
        }
        seriesName.setText(mSeries.getSeriesName());
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            mCallback.onSeriesSelected(mSeries);
        } else if (v instanceof ImageView) {
            mCallback.onImageSelected(mSeries, v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        mCallback.onSeriesDeleted(mSeries);
        return true;
    }
}
