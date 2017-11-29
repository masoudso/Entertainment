package com.androidapplication.entertainmentmedia.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.androidapplication.entertainmentmedia.R;

public class MainRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

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

            }
        });
    }

}
