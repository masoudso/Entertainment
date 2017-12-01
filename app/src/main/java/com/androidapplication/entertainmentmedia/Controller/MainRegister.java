package com.androidapplication.entertainmentmedia.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidapplication.entertainmentmedia.R;
import com.androidapplication.entertainmentmedia.Utilities.API;

public class MainRegister extends AppCompatActivity {

    private enum regResult {
        SUCCESS,
        INVALID_USERNAME,
        INVALID_PASSWORD,
        INVALID_EMAIL;
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
                regResult res = createUser(viewUsername.getText().toString(), viewEmail.getText().toString(), viewPassword.getText().toString());

                switch(res)
                {
                    case SUCCESS:
                        Toast.makeText(getBaseContext(), "Your account has been registered.", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case INVALID_USERNAME:
                        Toast.makeText(getBaseContext(), "Invalid username.", Toast.LENGTH_SHORT).show();
                        break;
                    case INVALID_EMAIL:
                        Toast.makeText(getBaseContext(), "Invalid email address.", Toast.LENGTH_SHORT).show();
                        break;
                    case INVALID_PASSWORD:
                        Toast.makeText(getBaseContext(), "Invalid password.", Toast.LENGTH_SHORT).show();
                        break;
                }
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
    private regResult createUser(String username, String email, String password)
    {
        return regResult.SUCCESS;
    }

    private void getAPI()
    {
        api = (API) getIntent().getSerializableExtra("API");
    }

}
