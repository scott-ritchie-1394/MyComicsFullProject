package com.example.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mycomics.IssueActivity;
import com.example.android.mycomics.MainActivity;
import com.example.android.mycomics.R;
import com.example.android.mycomics.Series;
import com.example.android.mycomics.character;

import java.util.List;

/**
 * Created by Scott on 3/10/2016.
 */
public class SeriesAdapter extends ArrayAdapter<Series> {
    Context context;

    public SeriesAdapter(Context mContext, List<Series> series) {
        super(mContext, 0, series);
        context = mContext;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Series mySeries = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.character_layout, parent, false);
        }
        TextView seriesName = (TextView) convertView.findViewById(R.id.textView);
        final ImageView seriesImage = (ImageView) convertView.findViewById(R.id.imageView);
        ViewGroup.LayoutParams params = seriesImage.getLayoutParams();
        int[] size = getScreenSize();
        params.height = size[0];
        params.width = size[1];
        seriesImage.requestLayout();
        seriesImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//To set custom image
                SeriesActivity.currentUserEdit = seriesImage;
                SeriesActivity.currentSaveSeries = getItem(position);
                userSetImage();
            }
        });
        seriesName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//To set custom image
                //Toast.makeText(getContext(), mySeries.getCharacterName(), Toast.LENGTH_SHORT).show();
                nextActivity(mySeries);
            }
        });

        Bitmap bm = mySeries.getImage();
        if (bm != null) {
            seriesImage.setImageBitmap(bm);
        } else {
            seriesImage.setImageResource(R.drawable.addimage);
        }
        seriesName.setText(mySeries.getSeriesName());
        return convertView;
    }

    public void userSetImage() {//Called from clicking the image view.
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        ((Activity) context).startActivityForResult(galleryIntent, 1);
    }

    public void nextActivity(Series mySeries) {
        Intent intent = new Intent(context, IssueActivity.class);
        intent.putExtra("currentSeries", mySeries);
        intent.putExtra("character", SeriesActivity.currentCharacter);//So we know what character we are dealing with
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
}
