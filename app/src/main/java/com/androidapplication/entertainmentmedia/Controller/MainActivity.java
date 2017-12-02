package com.androidapplication.entertainmentmedia.Controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidapplication.entertainmentmedia.Data.Movie;
import com.androidapplication.entertainmentmedia.Model.MovieRecycleViewAdapter;
import com.androidapplication.entertainmentmedia.R;
import com.androidapplication.entertainmentmedia.Utilities.API;
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
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;

    private API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = new API();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Preference preference = new Preference(MainActivity.this);
        String search = preference.getSearch();

        movieList = new ArrayList<>();

        //getMovies(search);
        movieList = getMovies(search);

        movieRecycleViewAdapter = new MovieRecycleViewAdapter(this, movieList);
        recyclerView.setAdapter(movieRecycleViewAdapter);
        movieRecycleViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu: this add items to the action bar if it present
        //getMenuInflater().inflate(R.menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem loginMenuItem = menu.findItem(R.id.button_login);

        if (api.getLoginStatus())
            loginMenuItem.setTitle("Login");
        else
            loginMenuItem.setTitle("Logout");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection  SimplifiableIfStatement
        if(id == R.id.new_search){
            showInputDialog();
        }

        if (id == R.id.button_login)
        {
            if (api.getLoginStatus()) {
                Intent intent = new Intent(MainActivity.this, MainLogin.class);
                intent.putExtra("API", api);
                MainActivity.this.startActivityForResult(intent, 1);
            }
            else{
                api.logout();
            }

        }

        return super.onOptionsItemSelected(item);
    }


    public void showInputDialog(){
        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        final EditText newSearchEdit = (EditText) view.findViewById(R.id.searchEdit);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preference preference = new Preference(MainActivity.this);

                if(!newSearchEdit.getText().toString().isEmpty()){

                    String search = newSearchEdit.getText().toString();
                    preference.setSearch(search);
                    movieList.clear();

                    getMovies(search);
                }
                dialog.dismiss();
            }
        });
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
                    movieRecycleViewAdapter.notifyDataSetChanged();

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

    //Receive API from finished login activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                api = (API) data.getSerializableExtra("API");
                invalidateOptionsMenu(); //Rename the login/logout button
            }
        }
    }


}
