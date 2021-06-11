package com.example.tamsdineout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

public class BookTableAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_table_animation);
        Button menuAcc = findViewById(R.id.menu);
        Button locAcc = findViewById(R.id.locate);
        Button orderAcc = findViewById(R.id.orders);
        Button profileAcc = findViewById(R.id.profile);
        Button book = findViewById(R.id.book);

        menuAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookTableAnimationActivity.this,MenuCardActivity.class);
                startActivity(intent);

            }
        });
        locAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookTableAnimationActivity.this,LocationActivity.class);
                startActivity(intent);

            }
        });
        profileAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookTableAnimationActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
        orderAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookTableAnimationActivity.this,BookOrderActivity.class);
                startActivity(intent);

            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookTableAnimationActivity.this,BookTableActivity.class);
                startActivity(intent);
            }
        });


        final VideoView videoView=findViewById(R.id.video);
        String path="android.resource://"+getPackageName()+"/"+R.raw.intro;
        videoView.setVideoPath(path);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });

        ImageView imageView=findViewById(R.id.slideimg);
        AnimationDrawable animationDrawable=(AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();

    }
}
