package com.example.tamsdineout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class MusicRoomBooking extends AppCompatActivity {

    private ImageView table1,table2,table3,table4,table5,table6,table7,table8,table9;
    private TextView tbl1,tbl2, tbl3, tbl4,tbl5,tbl6,tbl7,tbl8,tbl9;
    private String date,time,userId,timeFormat;
    private FirebaseFirestore mFireStore;
    private String userName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_room_booking);
        table1=findViewById(R.id.table1);
        table2=findViewById(R.id.table2);
        table3=findViewById(R.id.table3);
        table4=findViewById(R.id.table4);
        table5=findViewById(R.id.table5);
        table6=findViewById(R.id.table6);
        table7=findViewById(R.id.table7);
        table8=findViewById(R.id.table8);
        table9=findViewById(R.id.table9);
        tbl1=findViewById(R.id.tbl1);
        tbl2=findViewById(R.id.tbl2);
        tbl3=findViewById(R.id.tbl3);
        tbl4=findViewById(R.id.tbl4);
        tbl5=findViewById(R.id.tbl5);
        tbl6=findViewById(R.id.tbl6);
        tbl7=findViewById(R.id.tbl7);
        tbl8=findViewById(R.id.tbl8);
        tbl9=findViewById(R.id.tbl9);

        TextView textDate = findViewById(R.id.t2);
        TextView textTime = findViewById(R.id.t3);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        userId= mUser.getUid();
        date=getIntent().getStringExtra("date");
        time=getIntent().getStringExtra("timeSlot");

        textDate.setText(date);

        switch (time){
            case "1":
                textTime.setText("6:00 PM");
            timeFormat="6:00 PM";
                break;
            case "2":
                textTime.setText("6:30 PM");
                timeFormat="6:30 PM";
                break;
            case "3":
                textTime.setText("7:00 PM");
                timeFormat="7:00 PM";
                break;
            case "4":
                textTime.setText("7:30 PM");
                timeFormat="7:30 PM";
                break;
            case "5":
                textTime.setText("8:00 PM");
                timeFormat="8:00 PM";
                break;
            case "6":
                textTime.setText("8:30 PM");
                timeFormat="8:30 PM";
                break;
            case "7":
                textTime.setText("9:00 PM");
                timeFormat="9:00 PM";
                break;
            case "8":
                textTime.setText("9:30 PM");
                timeFormat="9:30 PM";
                break;
        }

        getBookings();


        table1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDetails(1);
            }
        });
        table2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDetails(2);
            }
        });
        table3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDetails(3);
            }
        });
        table4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDetails(4);
            }
        });
        table5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDetails(5);
            }
        });
        table6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDetails(6);
            }
        });
        table7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDetails(7);
            }
        });
        table8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDetails(8);
            }
        });
        table9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBookingDetails(9);
            }
        });

        SharedPreferences shared = getSharedPreferences("PREFS", MODE_PRIVATE);
        userName= (shared.getString("name", "User"));
    }

    private void bookTable(int tableNo){

            String slotE="slot"+ time;



            final String userI=String.valueOf(tableNo);

            String table="book"+userI;

        mFireStore=FirebaseFirestore.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference documentReference = firebaseDatabase.getReference("Music Room").child(slotE);


        documentReference.child(date).child(table).setValue(userI).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MusicRoomBooking.this,"Table Booked",Toast.LENGTH_LONG).show();
                userBooking(userI);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MusicRoomBooking.this,"Table Not Booked",Toast.LENGTH_LONG).show();
            }
        });

        int time2=Integer.parseInt(time);
        time2++;
        String slotE1="slot"+ time2;


        FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance();
        DatabaseReference documentReference2 = firebaseDatabase2.getReference("Music Room").child(slotE1);
        documentReference2.child(date).child(table).setValue(userI).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MusicRoomBooking.this,"Table Booked",Toast.LENGTH_LONG).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MusicRoomBooking.this,"Table Not Booked",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getBookings(){

        String slotE="slot"+ time;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Music Room").child(slotE).child(date);
        reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            long count=dataSnapshot.getChildrenCount();
            Toast.makeText(MusicRoomBooking.this, ""+ count, Toast.LENGTH_LONG).show();
            for(int i=1;i < 10;i++){
              String ret="book"+i;
                Toast.makeText(MusicRoomBooking.this, ""+ret, Toast.LENGTH_LONG).show();
              if(dataSnapshot.child(ret).exists()){
                  reflectBooking(i);
                  Toast.makeText(MusicRoomBooking.this, ""+ i, Toast.LENGTH_LONG).show();
              }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(MusicRoomBooking.this, "Error Occurred", Toast.LENGTH_LONG).show();
        }
    });
}

public void userBooking(String num){
     mFireStore = FirebaseFirestore.getInstance();

     userBookedTable bookedTable=new userBookedTable("MusicRoom",date,timeFormat,num,"UnPaid");

    mFireStore.collection("Bookings").document(userId).set(bookedTable).addOnCompleteListener(MusicRoomBooking.this, new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
                Toast.makeText(MusicRoomBooking.this, "Loading Please Wait", Toast.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MusicRoomBooking.this, BookTableActivity.class);
                        startActivity(intent);
                    }
                }, 4000);
            } else {
                Toast.makeText(MusicRoomBooking.this, "Please Try Again", Toast.LENGTH_LONG).show();
            }
        }
    });
    }

 public void reflectBooking(int bookedTable){
        String booked="BOOKED";
            switch (bookedTable){
                    case 1:
                        tbl1.setText(booked);
                        table1.setEnabled(false);
                        tbl1.setTextColor(Color.RED);
                        break;
                    case 2:
                        tbl2.setText(booked);
                        table2.setEnabled(false);
                        tbl2.setTextColor(Color.RED);
                        break;
                    case 3:
                        tbl3.setText(booked);
                        table3.setEnabled(false);
                        tbl3.setTextColor(Color.RED);
                        break;
                    case 4:
                        tbl4.setText(booked);
                        table4.setEnabled(false);
                        tbl4.setTextColor(Color.RED);
                        break;
                    case 5:
                        tbl5.setText(booked);
                        table5.setEnabled(false);
                        tbl5.setTextColor(Color.RED);
                        break;
                    case 6:
                        tbl6.setText(booked);
                        table6.setEnabled(false);
                        tbl6.setTextColor(Color.RED);
                        break;
                    case 7:
                        tbl7.setText(booked);
                        table7.setEnabled(false);
                        tbl7.setTextColor(Color.RED);
                        break;
                    case 8:
                        tbl8.setText(booked);
                        table8.setEnabled(false);
                        tbl8.setTextColor(Color.RED);
                        break;
                    case 9:
                        tbl9.setText(booked);
                        table9.setEnabled(false);
                        tbl9.setTextColor(Color.RED);
                        break;
                    default:
                        Toast.makeText(MusicRoomBooking.this, "An Unexpected Error Occurred", Toast.LENGTH_LONG).show();
            }
        }

        @SuppressLint("SetTextI18n")
        private void showBookingDetails(final int tableNumber){
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(MusicRoomBooking.this).inflate(R.layout.bookedtableconfirmation, viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(MusicRoomBooking.this);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            TextView name,venue,ambiance,tableNo,payment;
            Button proceed;

            proceed=dialogView.findViewById(R.id.reserve);


            name=dialogView.findViewById(R.id.userName);
            venue=dialogView.findViewById(R.id.bookedVenue);
            ambiance=dialogView.findViewById(R.id.userPlace);
            tableNo=dialogView.findViewById(R.id.bookedTable);
            payment=dialogView.findViewById(R.id.paymentStat);

            String table = String.valueOf(tableNumber);
            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookTable(tableNumber);
                    alertDialog.dismiss();
                }
            });
            name.setText(userName);
            venue.setText(date+" , "+timeFormat);
            ambiance.setText("MUSICAL NIGHT,TAM's DINEOUT");
            tableNo.setText(table);
            payment.setText("UNPAID");
    }
 }

