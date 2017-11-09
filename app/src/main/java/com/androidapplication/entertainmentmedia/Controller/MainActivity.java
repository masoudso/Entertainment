package com.androidapplication.entertainmentmedia.Controller;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidapplication.entertainmentmedia.Data.Movie;
import com.androidapplication.entertainmentmedia.Model.MovieRecycleViewAdapter;
import com.androidapplication.entertainmentmedia.R;
import com.androidapplication.entertainmentmedia.Utilities.Constants;
import com.androidapplication.entertainmentmedia.Utilities.Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieRecycleViewAdapter movieRecycleViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList = new ArrayList<>();


        Preference preference = new Preference(MainActivity.this);
        String search = preference.getSearch();
        //getMovies(search);
        movieList = getMovies(search);

        movieRecycleViewAdapter = new MovieRecycleViewAdapter(this, movieList);
        recyclerView.setAdapter(movieRecycleViewAdapter);
        movieRecycleViewAdapter.notifyDataSetChanged();
    }


    //get movies
    public List<Movie> getMovies(String searchTerm){
        movieList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL_LEFT + searchTerm + Constants.URL_RIGHT, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    JSONArray moviesArray = response.getJSONArray("Search");

                    for(int i = 0; i < moviesArray.length(); i++)
                    {
                        JSONObject moviesObj = moviesArray.getJSONObject(i);

                        Movie movie = new Movie();
                        movie.setTitle(moviesObj.getString("Title"));
                        movie.setYear("Year Released: " + moviesObj.getString("Year"));
                        movie.setMovieType("Type: " +moviesObj.getString("Type"));
                        movie.setPoster( moviesObj.getString("Poster"));
                        movie.setImdbId(moviesObj.getString("imdbID"));

                        movieList.add(movie);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
    });
        queue.add(jsonObjectRequest);

        return movieList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
