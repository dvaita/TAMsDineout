package com.example.tamsdineout;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class BookTableActivity extends AppCompatActivity {

    private String typeA;
    private int num=0;
    private int timeSlot;
    private String date;
    private TextView bookedDate, bookedPayment;
    private RelativeLayout dashboardLayout;
    private LinearLayout bookingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_table);
        Button menuAcc = findViewById(R.id.menu);
        Button locAcc = findViewById(R.id.locate);
        Button orderAcc = findViewById(R.id.orders);
        Button profileAcc = findViewById(R.id.profile);
        Button res = findViewById(R.id.res);
        Button bookT = findViewById(R.id.bookT);



         bookedDate = findViewById(R.id.getDate);
         bookedPayment = findViewById(R.id.getPrice);
         dashboardLayout = findViewById(R.id.ri1);
         bookingLayout = findViewById(R.id.ri2);

        getDashboard();




        menuAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookTableActivity.this,MenuCardActivity.class);
                startActivity(intent);

            }
        });
        locAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookTableActivity.this,LocationActivity.class);
                startActivity(intent);

            }
        });
        profileAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookTableActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });
        orderAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookTableActivity.this,BookOrderActivity.class);
                startActivity(intent);

            }
        });

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BookTableActivity.this,BookTableAnimationActivity.class);
                startActivity(intent);
            }
        });

        bookT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDateDialog();
            }
        });


    }
    private void getDashboard() {

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId=user.getUid();


        DocumentReference documentReference= FirebaseFirestore.getInstance().collection("Bookings").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                  String date=documentSnapshot.getString("date");
                  String time=documentSnapshot.getString("time");
                  String payment=documentSnapshot.getString("payment");
                  String tableNo=documentSnapshot.getString("number");
                  String type=documentSnapshot.getString("type");

                  String dateFinal=date+" "+time;

                  bookedDate.setText(dateFinal);
                  bookedPayment.setText(payment);

                  bookingLayout.setVisibility(View.INVISIBLE);
                }
                else {
                    dashboardLayout.setVisibility(View.INVISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dashboardLayout.setVisibility(View.INVISIBLE);
            }
        });



    }

    public void showAmbianceType(){
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(BookTableActivity.this).inflate(R.layout.selectnopeopledialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(BookTableActivity.this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        final ImageView preButton,nextButton,table;
        final TextView numberText;
        final Button proceed;

        final String[] range={"Music","Bar","Garden","Pool","Family"};


        preButton=dialogView.findViewById(R.id.preNo);
        nextButton=dialogView.findViewById(R.id.nextNo);
        numberText=dialogView.findViewById(R.id.totalNo);
        table=dialogView.findViewById(R.id.tableImg);
        proceed=dialogView.findViewById(R.id.btnProceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              typeA=range[num];
                alertDialog.dismiss();

                switch (typeA){
                    case "Music":Intent intent1=new Intent(BookTableActivity.this,MusicRoomBooking.class);
                    intent1.putExtra("timeSlot",String.valueOf(timeSlot));
                    intent1.putExtra("date",date);
                    startActivity(intent1);
                        break;
                    case "Bar":Intent intent2=new Intent(BookTableActivity.this,BarRoomBooking.class);
                        intent2.putExtra("timeSlot",String.valueOf(timeSlot));
                        intent2.putExtra("date",date);
                        startActivity(intent2);
                        break;
                    case "Garden":Intent intent3=new Intent(BookTableActivity.this,GardenRoomBooking.class);
                        intent3.putExtra("timeSlot",String.valueOf(timeSlot));
                        intent3.putExtra("date",date);
                        startActivity(intent3);
                        break;
//                    case "Pool":Intent intent4=new Intent(BookTableActivity.this,MusicRoomBooking.class);
//                    intent4.putExtra("timeSlot",timeSlot);
//                    intent4.putExtra("date",date);
//                        startActivity(intent4);
//                        break;
                    case "Family":Intent intent5=new Intent(BookTableActivity.this,FamilyRoomBooking.class);
                        intent5.putExtra("timeSlot",String.valueOf(timeSlot));
                        intent5.putExtra("date",date);
                        startActivity(intent5);
                        break;
                }
            }
        });


        table.setImageResource(R.drawable.band);
        numberText.setText(range[0]);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (num) {
                    case 0:
                        num = 1;
                        numberText.setText(range[num]);
                        table.setImageResource(R.drawable.bar);
                        break;
                    case 1: num = 2;
                        numberText.setText(range[num]);
                        table.setImageResource(R.drawable.garden);
                        break;
                    case 2: num = 3;
                        numberText.setText(range[num]);
                        table.setImageResource(R.drawable.swimmingpool);
                        break;
                    case 3: num = 4;
                        numberText.setText(range[num]);
                        table.setImageResource(R.drawable.acr);
                        break;
                    case 4:
                        numberText.setText(range[num]);
                        break;
                }
            }
        });

        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (num) {
                    case 0:
                        numberText.setText(range[num]);
                        break;
                    case 1:
                        num = 0;
                        numberText.setText(range[num]);
                        table.setImageResource(R.drawable.band);
                        break;
                    case 2:
                        num=1;
                        numberText.setText(range[num]);
                        table.setImageResource(R.drawable.bar);
                        break;
                    case 3:
                        num=2;
                        numberText.setText(range[num]);
                        table.setImageResource(R.drawable.garden);
                        break;
                    case 4:
                        num=3;
                        numberText.setText(range[num]);
                        table.setImageResource(R.drawable.swimmingpool);
                        break;
                }
            }
        });


    }

    private void showDateDialog() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(BookTableActivity.this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
                String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                String _pickedDate = year + "-" + _month + "-" + _date;
                Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd");

                date=simpleDateFormat.format(c.getTime());
                showTimeDialog();

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }
    private void showTimeDialog(){
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(BookTableActivity.this).inflate(R.layout.timedialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(BookTableActivity.this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        final RadioButton slot1,slot2,slot3,slot4,slot5,slot6,slot7,slot8;
        Button btnProceed;

        slot1=dialogView.findViewById(R.id.sixclock);
        slot2=dialogView.findViewById(R.id.sixthirtyclock);
        slot3=dialogView.findViewById(R.id.sevenclock);
        slot4=dialogView.findViewById(R.id.seventhirtyclock);
        slot5=dialogView.findViewById(R.id.eightclock);
        slot6=dialogView.findViewById(R.id.eightthirtyclock);
        slot7=dialogView.findViewById(R.id.nineclock);
        slot8=dialogView.findViewById(R.id.ninethirtyclock);

        btnProceed=dialogView.findViewById(R.id.select);

        slot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot2.setChecked(false);
                slot3.setChecked(false);
                slot4.setChecked(false);
                slot5.setChecked(false);
                slot6.setChecked(false);
                slot7.setChecked(false);
                slot8.setChecked(false);

                timeSlot=1;
            }
        });
        slot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot1.setChecked(false);
                slot3.setChecked(false);
                slot4.setChecked(false);
                slot5.setChecked(false);
                slot6.setChecked(false);
                slot7.setChecked(false);
                slot8.setChecked(false);

                timeSlot=2;
            }
        });
        slot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot2.setChecked(false);
                slot1.setChecked(false);
                slot4.setChecked(false);
                slot5.setChecked(false);
                slot6.setChecked(false);
                slot7.setChecked(false);
                slot8.setChecked(false);

                timeSlot=3;
            }
        });
        slot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot2.setChecked(false);
                slot3.setChecked(false);
                slot1.setChecked(false);
                slot5.setChecked(false);
                slot6.setChecked(false);
                slot7.setChecked(false);
                slot8.setChecked(false);

                timeSlot=4;
            }
        });
        slot5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot2.setChecked(false);
                slot3.setChecked(false);
                slot4.setChecked(false);
                slot1.setChecked(false);
                slot6.setChecked(false);
                slot7.setChecked(false);
                slot8.setChecked(false);

                timeSlot=5;
            }
        });
        slot6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot2.setChecked(false);
                slot3.setChecked(false);
                slot4.setChecked(false);
                slot5.setChecked(false);
                slot1.setChecked(false);
                slot7.setChecked(false);
                slot8.setChecked(false);

                timeSlot=6;
            }
        });
        slot7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot2.setChecked(false);
                slot3.setChecked(false);
                slot4.setChecked(false);
                slot5.setChecked(false);
                slot6.setChecked(false);
                slot1.setChecked(false);
                slot8.setChecked(false);

                timeSlot=7;
            }
        });
        slot8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot2.setChecked(false);
                slot3.setChecked(false);
                slot4.setChecked(false);
                slot5.setChecked(false);
                slot6.setChecked(false);
                slot7.setChecked(false);
                slot1.setChecked(false);

                timeSlot=8;
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
              showAmbianceType();
            }
        });
    }
}
