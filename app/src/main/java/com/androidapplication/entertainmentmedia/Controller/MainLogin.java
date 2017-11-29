package com.androidapplication.entertainmentmedia.Controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidapplication.entertainmentmedia.R;

public class MainLogin extends AppCompatActivity {

    private int counter;

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
        if(username.equals("admin") && password.equals("admin")) {
            Toast.makeText(getApplicationContext(),
                    "Redirecting...",Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_LONG).show();

            attemptsCounter.setBackgroundColor(Color.RED);
            counter--;
            attemptsCounter.setText(Integer.toString(counter));

            if (counter == 0) {
                registerButton.setEnabled(false);
            }
        }
    }

}
