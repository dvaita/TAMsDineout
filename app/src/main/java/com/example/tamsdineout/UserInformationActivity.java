package com.example.tamsdineout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;

public class UserInformationActivity extends AppCompatActivity {
    private  TextView editName,editPhone,editEmail;
    private String phoneString;
    private String emailString;
    private String nameString;
    private String phone,email;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firebaseFirestore;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        editName=findViewById(R.id.edit_name);
        editPhone=findViewById(R.id.edit_phone);
        editEmail=findViewById(R.id.editEmail);
        Button btnSave = findViewById(R.id.btn_continue);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        userId= mUser.getUid();


        String typeOfUser = getIntent().getStringExtra("type");

        String emailIntent;
        String nameIntent;
        assert typeOfUser != null;
        switch (typeOfUser) {
            case "google":
            case "facebook":

                nameIntent = getIntent().getStringExtra("name");
                emailIntent = getIntent().getStringExtra("emailId");
                editName.setText(nameIntent);
                editEmail.setText(emailIntent);
                break;
            case "phone":
                //get phone
                String phoneIntent = getIntent().getStringExtra("phoneNo");
                editPhone.setText(phoneIntent);
                break;
            case "email":
                //get Name
                emailIntent = getIntent().getStringExtra("emailId");
                editEmail.setText(emailIntent);
                break;
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameString = editName.getText().toString();
                phone = editPhone.getText().toString();
                email = editEmail.getText().toString();
                if (nameString.isEmpty()) {
                    editName.setError("Enter the Name");
                } else if (phone.isEmpty()) {
                    showDialogue(2);
                } else if (email.isEmpty()) {
                    showDialogue(1);
                } else {
                    phoneString = phone;
                    emailString = email;
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("Users");

                    UserDataClass userDataClass = new UserDataClass(nameString, phoneString, emailString);
                    firebaseFirestore=FirebaseFirestore.getInstance();

                    firebaseFirestore.collection("Users").document(userId).set(userDataClass).addOnCompleteListener(UserInformationActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserInformationActivity.this, "Loading Please Wait", Toast.LENGTH_LONG).show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(UserInformationActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }, 4000);
                            } else {
                                Toast.makeText(UserInformationActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
    private void showDialogue(final int ch){ //ch==1 empty email ch==2 empty phone
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.askemptyfielddialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        Button contE,loadField;

        contE=dialogView.findViewById(R.id.continue1);
        loadField=dialogView.findViewById(R.id.missing_field);
        String addEmail="Add Email";
        String addPhone="Add Phone";

        if(ch==1){
            loadField.setText(addEmail);
        }
        else{
            loadField.setText(addPhone);
        }
        contE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch==1){
                    emailString="null";
                    editEmail.setText(emailString);
                    alertDialog.dismiss();
                }
                else{
                  phoneString="null";
                  editPhone.setText(phoneString);
                    alertDialog.dismiss();
                }
            }
        });
        loadField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ch==1){
                   editEmail.findFocus();
                   alertDialog.dismiss();
                }
                else{
                   editPhone.findFocus();
                    alertDialog.dismiss();
                }
            }
        });
    }
}


