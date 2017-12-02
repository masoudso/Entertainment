package com.androidapplication.entertainmentmedia.Controller;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        getMovies(search);

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
            loginMenuItem.setTitle("Logout");
        else
            loginMenuItem.setTitle("Login");

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
                logoutTask task = new logoutTask();
                task.execute();
            }
            else{
                Intent intent = new Intent(MainActivity.this, MainLogin.class);
                intent.putExtra("API", api);
                MainActivity.this.startActivityForResult(intent, 1);
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
    public void getMovies(String searchTerm){
        movieList.clear();

        searchTask task = new searchTask(searchTerm);
        task.execute();
    }

    public void parseMovie(JSONObject searchObject)
    {
        if (searchObject == null)
            return;

        JSONArray movies;

        try {
            movies = searchObject.getJSONObject("data").getJSONObject("search").getJSONArray("movies");

            for (int i = 0; i < movies.length(); i++) {
                Movie movie = new Movie();
                JSONObject movieObject = movies.getJSONObject(i);
                JSONArray genres = movieObject.getJSONArray("genres");
                JSONArray crews = movieObject.getJSONArray("crews");

                movie.setTitle(movieObject.getString("title"));
                movie.setYear(movieObject.getString("air_date_time").substring(0, 4));
                movie.setPlot(movieObject.getString("description"));
                movie.setPoster(movieObject.getString("pic_url"));

                StringBuilder genreStrBuilder = new StringBuilder();

                for (int j = 0; j < genres.length(); j++)
                {
                    genreStrBuilder.append(genres.getJSONObject(j).getString("name") + " ");

                    if (j != genres.length() - 1){
                        genreStrBuilder.append(", ");
                    }
                }

                StringBuilder crewStrBuilder = new StringBuilder();

                for (int j = 0; j < crews.length(); j++)
                {
                    crewStrBuilder.append(crews.getJSONObject(j).getString("fname") + " " +
                            crews.getJSONObject(j).getString("lname"));

                    if (j != crews.length() - 1){
                        crewStrBuilder.append(", ");
                    }
                }



                movie.setActors(crewStrBuilder.toString());
                movie.setGenre(genreStrBuilder.toString());

                movieList.add(movie);
            }
            movieRecycleViewAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
                e.printStackTrace();
                return;
        }
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

    private class logoutTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            api.logout();
            return null;
        }

        protected void onPostExecute(Void doInBackground)
        {
            Toast.makeText(getBaseContext(), "You have logged out.", Toast.LENGTH_SHORT).show();
            invalidateOptionsMenu();
        }
    }

    private class searchTask extends AsyncTask<String, Void, Void> {

        private String query;
        private JSONObject searchObject;

        private searchTask(String query) {
            this.query = query;
        }

        @Override
        protected Void doInBackground(String... strings) {
            searchObject = api.search(query);
            return null;
        }

        protected void onPostExecute(Void doInBackground)
        {
            parseMovie(searchObject);
        }
    }
}
