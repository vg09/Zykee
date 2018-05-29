    package com.example.omarla.food2u_repo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

    public class Splashscreen extends AppCompatActivity {
private static int SPLASH_TIME_OUT=5000;
private ImageView imageView;
private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        textView=(TextView) findViewById(R.id.txtwlcm);
        imageView=(ImageView) findViewById(R.id.img);
        Animation myAnim= AnimationUtils.loadAnimation(this,R.anim.transition);
        textView.startAnimation(myAnim);
        imageView.startAnimation(myAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
