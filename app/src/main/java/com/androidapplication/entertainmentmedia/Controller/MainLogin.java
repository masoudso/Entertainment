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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

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
                loginUser(viewUsername.getText().toString(), viewPassword.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainLogin.this, MainRegister.class);
                MainLogin.this.startActivity(intent);
            }
        });
    }

    private void loginUser(String username, String password)
    {
        if (username.isEmpty() || password.isEmpty())
            return;

        //loginButton.setEnabled(false);

        //Put user credentials into object for API call
        Map<String, String> postData = new HashMap<>();
        postData.put("username", username);
        postData.put("password", password);

        authTask task = new authTask(postData);
        task.execute();

    }

    private class authTask extends AsyncTask<String, Void, Void> {

        JSONObject postData;
        JSONObject responseData;

        public authTask(Map<String, String> postData) {
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            try
            {
                URL url = new URL("https://cop4331.herokuapp.com/api/sessions");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                //Header type
                connection.setRequestProperty("Content-Type", "application/json");

                //Put the JSON object into message body
                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                //Don't know if this does anything
                //connection.connect();

                //Get response
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    InputStream stream = new BufferedInputStream(connection.getInputStream());

                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;

                    //Convert InputStream from response to JSONObject
                    try {
                        while ((inputStr = streamReader.readLine()) != null)
                            responseStrBuilder.append(inputStr);
                        responseData = new JSONObject(responseStrBuilder.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("Reponse", String.valueOf(responseCode));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute()
        {

        }
    }

}
