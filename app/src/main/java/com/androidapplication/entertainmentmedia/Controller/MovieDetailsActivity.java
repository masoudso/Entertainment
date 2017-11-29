package com.androidapplication.entertainmentmedia.Controller;

import android.content.SharedPreferences;
import android.media.Rating;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidapplication.entertainmentmedia.Data.Movie;
import com.androidapplication.entertainmentmedia.R;
import com.androidapplication.entertainmentmedia.Utilities.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;


public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;
    private TextView movieTitle;
    private ImageView moviePoster;
    private TextView movieYear;
    private TextView movieDirector;
    private TextView movieActors;
    private TextView movieType;
    private TextView movieRating;
    private TextView movieWriters;
    private TextView moviePlot;
    private TextView movieBoxOffice;
    private TextView movieRuntime;

    private ToggleButton buttonFollow;
    private Button buttonRate;

    private RequestQueue queue;
    private String movieId;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private SharedPreferences.Editor prefEditor;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);

        queue = Volley.newRequestQueue(this);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        movieId = movie.getImdbId();


        setUpUserInterface();
        setUpButtons();
        getMovieDetails(movieId);

    }


    private void setUpUserInterface() {

        movieTitle = (TextView) findViewById(R.id.movieTitleDetailsTextView);
        moviePoster = (ImageView) findViewById(R.id.moviePosterDetailsImageVeiw);
        movieYear = (TextView) findViewById(R.id.movieYearDetailsTextView);
        movieDirector = (TextView) findViewById(R.id.moviedirectorDetailsTextView);
        movieType = (TextView) findViewById(R.id.movieTypeDetailsTextView);
        movieRating = (TextView) findViewById(R.id.movieRatingDetailsTextView);
        movieWriters = (TextView) findViewById(R.id.movieWritersDetailsTextView);
        moviePlot = (TextView) findViewById(R.id.moviePlotDetailsTextView);
        movieBoxOffice = (TextView) findViewById(R.id.movieBoxOfficeDetailsTextView);
        movieRuntime = (TextView) findViewById(R.id.movieRunTimeDetailsTextView);

    }

    private void setUpButtons()
    {
        buttonFollow = (ToggleButton)findViewById(R.id.button_follow);
        buttonRate = (Button)findViewById(R.id.button_rate);

        SharedPreferences sharedPrefs = getSharedPreferences("com.androidapplication.entertainmentmedia", MODE_PRIVATE);

        buttonFollow.setChecked(sharedPrefs.getBoolean(movie.getTitle(), false));
        buttonFollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                prefEditor = getSharedPreferences("com.androidapplication.entertainmentmedia", MODE_PRIVATE).edit();
                if (isChecked)
                {
                    Toast.makeText(getBaseContext(), ("You are now following " + movie.getTitle() + "!"), Toast.LENGTH_SHORT).show();

                    prefEditor.putBoolean(movie.getTitle(), true);
                    prefEditor.commit();
                }
                else
                {
                    Toast.makeText(getBaseContext(), ("You are no longer following " + movie.getTitle() + "!"), Toast.LENGTH_SHORT).show();

                    prefEditor.putBoolean(movie.getTitle(), false);
                    prefEditor.commit();
                }
            }
        });

        buttonRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(MovieDetailsActivity.this);
                view = getLayoutInflater().inflate(R.layout.rating_view, null);

                dialogBuilder.setView(view);
                dialog = dialogBuilder.create();
                dialog.show();


                final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
                Button submitButton = (Button) view.findViewById(R.id.submitButton);

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prefEditor = getSharedPreferences("com.androidapplication.entertainmentmedia", MODE_PRIVATE).edit();

                        prefEditor.putInt("r" + movie.getTitle(), ratingBar.getNumStars());
                        prefEditor.commit();

                        Toast.makeText(getBaseContext(), ("Your rating has been saved"), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void getMovieDetails(String movieId) {
        /**********************MAY HAVE TO EDIT THE CODE ***********************/
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + movieId + Constants.URL_RIGHT, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(response.has("Ratings")){
                        JSONArray ratingJSONArray = response.getJSONArray("Ratings");

                        String source = null;
                        String value = null;
                        if(ratingJSONArray.length() > 0){
                            JSONObject ratingJSONObject = ratingJSONArray.getJSONObject(ratingJSONArray.length() -1);
                            source = ratingJSONObject.getString("Source");
                            value = ratingJSONObject.getString("Value");

                            movieRating.setText(source + " : " + value);
                        }
                        else{
                            movieRating.setText("Ratings: N/A");
                        }

                        movieTitle.setText(response.getString("Title"));
                        movieYear.setText("Released: " + response.getString("Released"));
                        movieDirector.setText("Director: " + response.getString("Director"));
                        movieWriters.setText("Writer: " + response.getString("Writer"));
                        moviePlot.setText("Plot: " + response.getString("Plot"));
                        movieRuntime.setText("Runtime: " + response.getString("Runtime"));
                       // movieActors.setText("Actors: " + response.getString("Actors"));
                        movieBoxOffice.setText("BoxOffice: " + response.getString("BoxOffice"));

                        Picasso.with(getApplicationContext())
                                .load(response.getString("Poster"))
                                .into(moviePoster);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ", error.getMessage());
            }
        });

        queue.add(jsonObjectRequest);
    }
}

