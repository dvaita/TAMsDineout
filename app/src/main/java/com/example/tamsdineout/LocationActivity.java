package com.example.tamsdineout;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.GoogleMap;


public class LocationActivity extends AppCompatActivity  {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Button menuAcc = findViewById(R.id.menu);
        Button orderAcc = findViewById(R.id.orders);
        Button bookTAcc = findViewById(R.id.bookTable);
        Button profileAcc = findViewById(R.id.profile);















































        menuAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LocationActivity.this,MenuCardActivity.class);
                startActivity(intent);

            }
        });
        profileAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LocationActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
        bookTAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LocationActivity.this,BookTableActivity.class);
                startActivity(intent);

            }
        });
        orderAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LocationActivity.this,BookOrderActivity.class);
                startActivity(intent);

            }
        });
    }

}
