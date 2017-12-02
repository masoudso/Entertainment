package com.androidapplication.entertainmentmedia.Controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidapplication.entertainmentmedia.R;
import com.androidapplication.entertainmentmedia.Utilities.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainLogin extends AppCompatActivity {

    private int counter;
    private static final String TASKS_URL = "https://cop4331.herokuapp.com/api/sessions";

    private TextView attemptsCounter;
    private Button loginButton;
    private EditText viewUsername;
    private EditText viewPassword;
    private Button registerButton;

    private API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        getAPI();
        setUpInterface();
    }

    private void setUpInterface()
    {
        attemptsCounter = (TextView)findViewById(R.id.newText);
        loginButton = (Button)findViewById(R.id.button);
        viewUsername = (EditText)findViewById(R.id.enterName);
        viewPassword = (EditText)findViewById(R.id.password);
        registerButton = (Button)findViewById(R.id.button_register);

        counter = 3;
        attemptsCounter.setText(Integer.toString(counter));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (loginUser(viewUsername.getText().toString(), viewPassword.getText().toString()))
            {
                //Login successful, return the API to mainActivity
                Intent retIntent = new Intent();
                retIntent.putExtra("API", api);
                setResult(RESULT_OK, retIntent);
                finish();
            }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainLogin.this, MainRegister.class);
                MainLogin.this.startActivityForResult(intent, 1);

                //Registration successful, return the API to mainActivity
                Intent retIntent = new Intent();
                retIntent.putExtra("API", api);
                setResult(RESULT_OK, retIntent);
                finish();
            }
        });
    }

    private boolean loginUser(String username, String password)
    {
        if (username.isEmpty() || password.isEmpty())
            return false;

        //loginButton.setEnabled(false);

        authTask task = new authTask(username, password);
        task.execute();

        return true;
    }

    private class authTask extends AsyncTask<String, Void, Void> {

        JSONObject responseData;
        Map<String, String> postData;

        public authTask(String username, String password) {
            postData = new HashMap<>();
            postData.put("username", username);
            postData.put("password", password);
        }

        @Override
        protected Void doInBackground(String... strings) {
            api.login(postData);
            return null;
        }

        protected void onPostExecute()
        {

        }
    }

    private void getAPI()
    {
        api = (API) getIntent().getSerializableExtra("API");
    }

    //Receive API from finished register activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                api = (API) data.getSerializableExtra("API");
            }
        }
    }

}
