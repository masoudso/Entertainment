package com.androidapplication.entertainmentmedia.Controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidapplication.entertainmentmedia.R;
import com.androidapplication.entertainmentmedia.Utilities.API;

import java.util.HashMap;
import java.util.Map;

public class MainRegister extends AppCompatActivity {

    private enum regResult {
        SUCCESS,
        INVALID_USERNAME,
        INVALID_PASSWORD,
        INVALID_EMAIL,
        UNKNOWN_ERROR;
    }

    private EditText viewUsername;
    private EditText viewEmail;
    private EditText viewPassword;

    private API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        getAPI();
        setUpInterface();
        setUpButtons();
    }

    private void setUpButtons()
    {
        Button buttonLogin = (Button)findViewById(R.id.buttonExistingAccount);
        Button buttonRegisterAccount = (Button)findViewById(R.id.buttonRegisterAccount);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainRegister.this, MainLogin.class);
                MainRegister.this.startActivity(intent);
            }
        });

        buttonRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(viewUsername.getText().toString(), viewEmail.getText().toString(), viewPassword.getText().toString());
            }
        });
    }

    private void setUpInterface()
    {
        viewUsername = (EditText)findViewById(R.id.registerUsername);
        viewEmail = (EditText)findViewById(R.id.registerEmail);
        viewPassword = (EditText)findViewById(R.id.registerPassword);
    }

    //Validates the user input and attempts to create a new user
    //Returns SUCCESS if account is created, returns INVALID_* if account fails to create
    private void createUser(String username, String email, String password)
    {
        if (username.isEmpty())
            return;

        if (email.isEmpty())
            return;

        if (password.isEmpty())
            return;

        regTask task = new regTask(username, password);
        task.execute();
    }

    private class regTask extends AsyncTask<String, Void, Void> {

        Map<String, String> postData;

        public regTask(String username, String password) {
            postData = new HashMap<>();
            postData.put("username", username);
            postData.put("password", password);
        }

        @Override
        protected Void doInBackground(String... strings) {
            Log.d("user", postData.get("username"));
            api.register(postData);
            return null;
        }

        protected void onPostExecute(Void doInBackground)
        {
            if (api.getLoginStatus()) {
                Toast.makeText(getBaseContext(), "Your account has been registered.", Toast.LENGTH_SHORT).show();
                Intent retIntent = new Intent();
                retIntent.putExtra("API", api);
                setResult(RESULT_OK, retIntent);
                finish();
            }
        }
    }

    private void getAPI()
    {
        api = (API) getIntent().getSerializableExtra("API");
    }

}
