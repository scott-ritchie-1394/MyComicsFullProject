package com.example.android;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mycomics.R;


public class MovieDetails extends AppCompatActivity {
    String currentTag;
    ImageView mImageView;
    String currentMovie;
    String url;
    String summary;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Bundle extras = getIntent().getExtras();
        currentTag = extras.getString("imageTag");
        mImageView = (ImageView) findViewById(R.id.image_view);
        findImage(Integer.valueOf(currentTag));
        TextView textView = (TextView) findViewById(R.id.movieName);
        textView.setText(currentMovie);
        textView = (TextView) findViewById(R.id.summary);
        textView.setText(summary);
        textView = (TextView) findViewById(R.id.date);
        textView.setText(date);
    }

    public void findImage(int tag) {
        Drawable image = getResources().getDrawable(R.drawable.wonder_woman);
        switch (tag) {
            case 1:
                image = getResources().getDrawable(R.drawable.batman_v_superman);
                currentMovie = "Batman V Superman";
                summary = "Fearing that the actions of Superman are left unchecked, Batman takes on the Man of Steel, while the world wrestles with what kind of a hero it really needs.";
                url = "http://www.fandango.com/batmanvsuperman:dawnofjustice_169807/movietimes";
                date = "March 25, 2016";
                break;
            case 2:
                image = getResources().getDrawable(R.drawable.civil_war);
                currentMovie = "Captain America Civil War";
                summary = "Political interference in the Avengers' activities causes a rift between former allies Captain America and Iron Man.";
                url = "http://www.fandango.com/captainamerica:civilwar_185792/movietimes";
                date = "May 6, 2016";
                break;
            case 3:
                image = getResources().getDrawable(R.drawable.dr_strange);
                currentMovie = "Dr Strange";
                summary = "After his career is destroyed, a brilliant but arrogant and conceited surgeon gets a new lease on life when a sorcerer takes him under his wing and trains him to defend the world against evil.";
                url = "http://www.fandango.com/doctorstrange_186645/movietimes";
                date = "November 4, 2016";
                break;
            case 4:
                image = getResources().getDrawable(R.drawable.wonder_woman_movie);
                currentMovie = "Wonder Woman Movie";
                summary = "An Amazon princess leaves her island home to explore the world, and becomes the greatest of its female heroes.";
                url = "http://www.fandango.com/wonderwoman_191725/movieoverview";
                date = "June 2, 2017";
                break;
            case 5:
                image = getResources().getDrawable(R.drawable.suicide_squad);
                currentMovie = "Suicide Squad";
                summary = "A secret government agency recruits imprisoned supervillains to execute dangerous black ops missions in exchange for clemency.";
                url = "http://www.fandango.com/suicidesquad_179885/movietimes";
                date = "August 5, 2016";
                break;
            case 6:
                image = getResources().getDrawable(R.drawable.infinity_war);
                currentMovie = "Avengers Infinity War";
                summary = "The Universe's mightest heroes must unite to fight a foe unlike any of them have seen before";
                url = "NA";
                date = "May 4, 2018";
                break;
            case 7:
                image = getResources().getDrawable(R.drawable.xmen_apocolypse);
                currentMovie = "X-Men Apocolypse";
                summary = "With the emergence of the world's first mutant, Apocalypse, the X-Men must unite to defeat his extinction level plan.";
                url = "http://www.fandango.com/xmen:apocalypse_175470/movietimes";
                date = "May 27, 2016";
                break;

        }
        mImageView.setImageDrawable(image);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    public void search(View v) {
        Uri uri = Uri.parse("https://www.google.com/#q=" + currentMovie.replace(' ', '+') + "&tbm=nws"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void fandango(View v) {
        if (url.equals("NA")) {
            return;
        } else {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
