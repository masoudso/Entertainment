package com.androidapplication.entertainmentmedia.Controller;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        final TextView tx1 = (TextView)findViewById(R.id.newText);
        final Button b1 = (Button)findViewById(R.id.button);
        final EditText ed1 = (EditText)findViewById(R.id.enterName);
        final EditText ed2 = (EditText)findViewById(R.id.password);
        final Button b2 = (Button)findViewById(R.id.buttonA);

        counter = 0;
        tx1.setVisibility(View.GONE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed1.getText().toString().equals("admin") &&
                        ed2.getText().toString().equals("admin")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_LONG).show();

                    tx1.setVisibility(View.VISIBLE);
                    tx1.setBackgroundColor(Color.RED);
                    counter--;
                    tx1.setText(Integer.toString(counter));

                    if (counter == 0) {
                        b1.setEnabled(false);
                    }
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
