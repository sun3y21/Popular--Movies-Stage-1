package com.example.sunnny.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> urls=new ArrayList<>();

    Movie [] movies=null;

    ImageAdapter imageAdapter;

    //tags to recover data from the json Objects
    final String ARRAY_NAME="results";
    final String POSTER_PATH="poster_path";
    final String PLOT_SYNOPSIS="overview";
    final String TITLE="original_title";
    final String RATING="vote_average";
    final String RELEASE_DATE="release_date";
    final String MOVIE_ID="id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridView=(GridView)findViewById(R.id.gridofMovies);
        gridView.setColumnWidth(150);

        updateMoviesData();

        //fill to show some blank view on grid
        for(int i=0;i<10;i++)
            urls.add("www");

        //create a new object of image adapter
        imageAdapter=new ImageAdapter(this,urls);

        //set adapter to grid view
        gridView.setAdapter(imageAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(movies==null)
                {
                    Toast.makeText(getApplicationContext(),"Details unavailable",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Movie temp=movies[i];
                    Intent intent=new Intent(getApplicationContext(),DetailActivity.class);
                    intent.putExtra(POSTER_PATH,temp.getPoster_url());
                    intent.putExtra(PLOT_SYNOPSIS,temp.getPlot_synopsis());
                    intent.putExtra(TITLE,temp.getOrigional_title());
                    intent.putExtra(RATING,temp.getUser_rating());
                    intent.putExtra(RELEASE_DATE,temp.getRelease_date());
                    startActivity(intent);
                }

            }
        });
    }


    public void updateMoviesData()
    {
        //get the saved preference from settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sortByType = prefs.getString(getString(R.string.pref_sort_by),getString(R.string.default_sort_type));
        //fatch data for movies as per preference
        FetchMovieData f=new FetchMovieData();
        f.execute(sortByType);

    }


    public void onStart()
    {
        super.onStart();
        updateMoviesData();
    }



    class FetchMovieData extends AsyncTask<String,Void,Movie[]>{


       public Movie[] fetchMoviesDataFromJson(String jsonString)
       {
           Movie movies[]=null;
           try
           {


               String BASE_URL_FOR_POSTER="http://image.tmdb.org/t/p/";
               String POSTER_SIZE="w154";
               //append that data to url
               BASE_URL_FOR_POSTER+=POSTER_SIZE+"/";

               JSONObject jsonObject=new JSONObject(jsonString);
               JSONArray jsonArray=jsonObject.getJSONArray(ARRAY_NAME);

               movies=new Movie[jsonArray.length()];

               for(int i=0;i<jsonArray.length();i++)
               {
                   //create a new movie Object
                   Movie temp=new Movie();
                   //get Corresponding json object from json array
                   JSONObject obj=(JSONObject)jsonArray.get(i);

                   //set corresponding fields
                   temp.setOrigional_title(obj.getString(TITLE));
                   temp.setPlot_synopsis(obj.getString(PLOT_SYNOPSIS));
                   temp.setPoster_url(BASE_URL_FOR_POSTER+obj.getString(POSTER_PATH));
                   temp.setRelease_date(obj.getString(RELEASE_DATE));
                   temp.setId(obj.getString(MOVIE_ID));
                   temp.setUser_rating(obj.getString(RATING));
                 //  Log.v("URL : "+i,temp.getPoster_url());
                   movies[i]=temp;
               }

           }
           catch (Exception e)
           {
                Log.e("Error: ","JSON parsing error "+e.getMessage());
           }
           return movies;
       }


        @Override
        protected Movie[] doInBackground(String... params) {

            if(params==null)
            {
                return null;
            }


            //for url and buffer
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;




            String moviesDataJsonStr = null;
            String format = "json";


            try {

                //expected url http://api.themoviedb.org/3/movie/popular?api_key=
                String MOVIE_DATA_BASE_URL ="http://api.themoviedb.org/3/movie/popular?";


                if(params[0].equals("Top Rated"))
                {
                     MOVIE_DATA_BASE_URL ="http://api.themoviedb.org/3/movie/top_rated?";
                }


                final String KEY="api_key";


                Uri builtUri = Uri.parse(MOVIE_DATA_BASE_URL).buildUpon()
                        .appendQueryParameter(KEY,BuildConfig.API_KEY)
                        .build();

                URL url=new URL(builtUri.toString());

                 //check url
               // Log.v("URl---",url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();


                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    moviesDataJsonStr = null;

                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    moviesDataJsonStr = null;
                }
                moviesDataJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                moviesDataJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try
            {
                //got data its its time to parse
                // Log.v("Output",moviesDataJsonStr);
                return fetchMoviesDataFromJson(moviesDataJsonStr);

            }catch (Exception e)
            {
                Log.e("Error",e.getMessage());
            }

            return null;
        }


        public void onPostExecute(Movie mov[])
        {
            if(mov==null)
            {
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
            }
            else
            {
                super.onPostExecute(mov);
                urls.clear();
                movies=mov;
                for(int i=0;i<mov.length;i++)
                {
                    urls.add(mov[i].getPoster_url());
                    //  Log.v("Url : ",urls.get(i));
                    imageAdapter.add(urls.get(i));
                }
            }

        }

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem menu)
    {
        switch (menu.getItemId())
        {
            case R.id.settings:
                Intent intent=new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

}
