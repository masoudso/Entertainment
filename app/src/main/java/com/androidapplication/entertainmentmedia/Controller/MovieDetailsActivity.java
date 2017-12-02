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
import com.androidapplication.entertainmentmedia.Utilities.API;
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
    private TextView movieCrew;
    private TextView movieCatagory;
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

    private API api;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);

        queue = Volley.newRequestQueue(this);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        movieId = movie.getImdbId();

        getAPI();
        setUpUserInterface();
        setUpButtons();
        getMovieDetails(movieId);

    }


    private void setUpUserInterface() {

        movieTitle = (TextView) findViewById(R.id.movieTitleDetailsTextView);
        moviePoster = (ImageView) findViewById(R.id.moviePosterDetailsImageVeiw);
        movieYear = (TextView) findViewById(R.id.movieYearDetailsTextView);
        movieCrew = (TextView) findViewById(R.id.movieCrewDetailsTextView);
        movieCatagory = (TextView) findViewById(R.id.movieCatagoryDetailsTextView);
        moviePlot = (TextView) findViewById(R.id.moviePlotDetailsTextView);

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

        movieTitle.setText(movie.getTitle());
        movieYear.setText(movie.getYear());
        moviePlot.setText(movie.getPlot());
        //movieRuntime.setText("Runtime: " + response.getString("Runtime"));
        movieCrew.setText(movie.getActors());
        movieCatagory.setText(movie.getGenre());
        //movieBoxOffice.setText("BoxOffice: " + response.getString("BoxOffice"));
        //movieDirector.setText("Director: " + response.getString("Director"));
        //movieWriters.setText("Writer: " + response.getString("Writer"));

        Picasso.with(getApplicationContext())
                .load(movie.getPoster())
                .into(moviePoster);

    }

    private void getAPI()
    {
        api = (API) getIntent().getSerializableExtra("API");
    }
}

