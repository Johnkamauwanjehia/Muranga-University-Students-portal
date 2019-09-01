package com.example.paddy.pro;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        imageView.setAnimation(zoomin);
        imageView.startAnimation(zoomin);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String fileName = getString(R.string.filnem);
                File file = new File(getApplicationContext().getApplicationInfo().dataDir + "/shared_prefs/"
                        + fileName + ".xml");
                if (file.exists()) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 4500);

    }



}
