package com.example.tamsdineout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuCardActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_card);

        Button locAcc = findViewById(R.id.locate);
        Button orderAcc = findViewById(R.id.orders);
        Button bookTAcc = findViewById(R.id.bookTable);
        Button profileAcc = findViewById(R.id.profile);

        viewPager=findViewById(R.id.viewpager);
        adapter= new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        profileAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuCardActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
        locAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuCardActivity.this,LocationActivity.class);
                startActivity(intent);

            }
        });
        bookTAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuCardActivity.this,BookTableActivity.class);
                startActivity(intent);

            }
        });
        orderAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuCardActivity.this,BookOrderActivity.class);
                startActivity(intent);

            }
        });
    }
}
