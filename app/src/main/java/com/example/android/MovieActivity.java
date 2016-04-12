package com.example.android;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.mycomics.MainActivity;
import com.example.android.mycomics.R;

public class MovieActivity extends AppCompatActivity {
    LinearLayout activity;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        mContext = this;
        activity = (LinearLayout) findViewById(R.id.activityParent);
        setupToolBar();
        addMovie(R.drawable.batman_v_superman, "Batman V Superman", 1);
        addMovie(R.drawable.civil_war, "Captain America: Civil War", 2);
        addMovie(R.drawable.dr_strange, "Dr. Strange", 3);
        addMovie(R.drawable.wonder_woman_movie, "Wonder Woman", 4);
        addMovie(R.drawable.suicide_squad, "Suicide Squad", 5);
        addMovie(R.drawable.infinity_war, "Avengers: Infinity War", 6);
        addMovie(R.drawable.xmen_apocolypse, "X-Men Apocalypse", 7);

    }

    private void addMovie(int resourceId, String name, int tag) {
        View child = getLayoutInflater().inflate(R.layout.movie_layout, activity, false);
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof RelativeLayout) {
                    //TextView textName = (TextView) v.findViewById(R.id.movieName);
                    //search(textName.getText().toString());
                    Intent intent = new Intent(mContext, MovieDetails.class);
                    ImageView mImageView = (ImageView) v.findViewById(R.id.image_view);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Log.d("GOTTIT", "LOL NOPE");
                        Activity activity = (Activity) mContext;
                        ActivityOptions options = ActivityOptions.
                                makeSceneTransitionAnimation(activity, mImageView, "picture");
                        intent.putExtra("imageTag", mImageView.getTag().toString());
                        startActivity(intent, options.toBundle());
                    }
                }
            }
        });
        Drawable image = getResources().getDrawable(resourceId);
        ImageView mImageView = (ImageView) child.findViewById(R.id.image_view);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setImageDrawable(image);
        mImageView.setTag(String.valueOf(tag));
        TextView theName = (TextView) child.findViewById(R.id.movieName);
        theName.setText(name);
        activity.addView(child);
    }

    public void search(String searcher) {
        searcher = searcher.replace(' ', '+');
        Uri uri = Uri.parse("https://www.google.com/#q=" + searcher + "&tbm=nws"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Movies");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
