package com.example.android;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.mycomics.MainActivity;
import com.example.android.mycomics.R;

public class MovieActivity extends AppCompatActivity {
    LinearLayout activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        activity = (LinearLayout) findViewById(R.id.activityParent);
        setupToolBar();
        addMovie(R.drawable.batman_v_superman, "Batman V Superman");
        addMovie(R.drawable.civil_war, "Captain America: Civil War");
        addMovie(R.drawable.dr_strange, "Dr. Strange");
        addMovie(R.drawable.wonder_woman_movie, "Wonder Woman");
        addMovie(R.drawable.suicide_squad, "Suicide Squad");
        addMovie(R.drawable.infinity_war, "Avengers: Infinity War");
        addMovie(R.drawable.xmen_apocolypse, "X-Men Apocalypse");


    }

    private void addMovie(int resourceId, String name) {
        View child = getLayoutInflater().inflate(R.layout.movie_layout, activity, false);
        Drawable image = getResources().getDrawable(resourceId);
        child.findViewById(R.id.movieRelative).setBackground(image);
        TextView theName = (TextView) child.findViewById(R.id.movieName);
        theName.setText(name);
        activity.addView(child);
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
