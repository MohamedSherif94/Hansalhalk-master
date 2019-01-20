package com.hansalhalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.androidnetworking.AndroidNetworking;
import com.hansalhalk.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        AndroidNetworking.initialize(getApplicationContext());

        // get data from share SharePreference
        SharedPreferences sp = getSharedPreferences("token_file", MODE_PRIVATE);
        String token = sp.getString("token", null);

        if ( token != null){

            sendToHomeActivity();

        }else {

            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, com.hansalhalk.SecondActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000); // 1 second
        }
    }


    private void sendToHomeActivity() {
        Intent startIntent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(startIntent);
        finish();
    }
}
