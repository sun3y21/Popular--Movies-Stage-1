package com.example.sunnny.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //tags to recover data from the json Objects
        final String ARRAY_NAME="results";
        final String POSTER_PATH="poster_path";
        final String PLOT_SYNOPSIS="overview";
        final String TITLE="original_title";
        final String RATING="vote_average";
        final String RELEASE_DATE="release_date";
        final String MOVIE_ID="id";
        String POSTER_SIZE="w154";

        Intent intent=getIntent();

        setTitle(intent.getStringExtra(TITLE));


        //get the url
        String url=intent.getStringExtra(POSTER_PATH);


        //url have image size smaller change the size to bigger
        url=url.replace(POSTER_SIZE,"w500");


        ImageView imageView=(ImageView)findViewById(R.id.posterImage);
        Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.loading).into(imageView);

        //title of movie
        TextView textView=(TextView)findViewById(R.id.origional_title);
        textView.setText(intent.getStringExtra(TITLE));

        //user rating of movie
        textView=(TextView)findViewById(R.id.rating);
        textView.setText("User-Rating : "+intent.getStringExtra(RATING));

        //date of release
        textView=(TextView)findViewById(R.id.release_date);
        textView.setText("Release Date : "+intent.getStringExtra(RELEASE_DATE));



        //description of movie
        textView=(TextView)findViewById(R.id.description);
        textView.setText(intent.getStringExtra(PLOT_SYNOPSIS));


    }

}
