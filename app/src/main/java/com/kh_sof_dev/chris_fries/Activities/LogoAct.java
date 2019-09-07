package com.kh_sof_dev.chris_fries.Activities;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.kh_sof_dev.chris_fries.R;


public class LogoAct extends AppCompatActivity {

    private static int SPLASH_TIME = 3000;
    private TextView logo_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_a_logo);

final FirebaseAuth  auth= FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    startActivity(new Intent(LogoAct.this,SignUP_phoneNumer.class));
                    finish();


            }
        }, SPLASH_TIME);

    }
}
