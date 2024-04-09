package com.example.asm_api.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm_api.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Khởi tạo image view và animation view
        ImageView imageView = findViewById(R.id.imageView);
        ImageView animationView = findViewById(R.id.animationView);

        // Bắt đầu animation
        animationView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));

        // Handler để chuyển sang LoginActivity sau 10 giây
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000); // 10 seconds delay
    }
}