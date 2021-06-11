package com.example.tamsdineout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmailLoginActivity extends AppCompatActivity {
    private Button btnLogin,btnSignUp;
    private TextView editEmail,editPassword,editCPassword;
    private FirebaseAuth mAuth;
    private String mailString,passwordString,cPasswordString;
    private RelativeLayout relativeLayout,relativeLayout1;
    private int stat=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        editEmail=findViewById(R.id.editEmail);
        editPassword=findViewById(R.id.editPassword);
        editCPassword=findViewById(R.id.editCPassword);

        btnLogin=findViewById(R.id.btnLogin);
        btnSignUp =findViewById(R.id.btnSignUp);
        Button btnForgotPass = findViewById(R.id.btn_forgotPss);

        relativeLayout1=findViewById(R.id.typePass);
        relativeLayout=findViewById(R.id.typeCPass);

        mAuth=FirebaseAuth.getInstance();

        relativeLayout.setVisibility(View.INVISIBLE);
        mailString=editEmail.getText().toString();
        passwordString=editPassword.getText().toString();
        cPasswordString=editCPassword.getText().toString();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailString=editEmail.getText().toString();
                passwordString=editPassword.getText().toString();

                if(stat==2){
                    if (mailString.isEmpty()) {
                        editEmail.setError("Enter the Email Id");
                         }
                    else {
                        mAuth.sendPasswordResetEmail(mailString)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EmailLoginActivity.this, "Reset Mail Sent On the Email-Id mentioned above", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(EmailLoginActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(EmailLoginActivity.this, "Cant complete  your request! Please try again",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(EmailLoginActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                    }
                }
                else{
                    if (stat == 0) {
                            if (mailString.isEmpty()) {
                                editEmail.setError("Enter the Email Id");
                            } else if (passwordString.isEmpty()) {
                                editPassword.setError("Please Enter Password");
                            } else if (passwordString.length() < 8) {
                                editPassword.setError("8 Character Password Required");
                            }
                            mAuth.signInWithEmailAndPassword(mailString, passwordString)
                                    .addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(EmailLoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(EmailLoginActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(EmailLoginActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        } else if (stat == 1) {
                           cPasswordString=editCPassword.getText().toString();
                            if (mailString.isEmpty()) {
                                editEmail.setError("Enter the Email Id");
                            } else if (passwordString.isEmpty()) {
                                editPassword.setError("Please Enter Password");
                            } else if (passwordString.length() < 8) {
                                editPassword.setError("8 Character Password Required");
                            }
                            else {
                                if (passwordString.equals(cPasswordString)) {
                                    mAuth.createUserWithEmailAndPassword(mailString, passwordString)
                                            .addOnCompleteListener(EmailLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        checkUserExist(mailString);
                                                    } else {

                                                        Toast.makeText(EmailLoginActivity.this, "Authentication failed.",
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(EmailLoginActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                } else {
                                    editCPassword.setError("PASSWORD DO NOT MATCH");
                                }
                            }
                    }
                }
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
               if(stat==0) {
                   stat=1;
                   btnLogin.setText("Sign Up");
                   btnSignUp.setText("ALREADY USER? LOGIN HERE");
                   relativeLayout.setVisibility(View.VISIBLE);
               }
               else{
                   stat=0;
                   btnLogin.setText("Login");
                   relativeLayout.setVisibility(View.INVISIBLE);
               }
                relativeLayout1.setVisibility(View.VISIBLE);
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                stat=2;
                btnLogin.setText("Send Reset Password EMail");
                relativeLayout.setVisibility(View.INVISIBLE);
                relativeLayout1.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void checkUserExist(final String email){
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        String user= mUser.getUid();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(user);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        //main acc;
                        Intent intent = new Intent(EmailLoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent=new Intent(EmailLoginActivity.this,UserInformationActivity.class);
                        intent.putExtra("type", "email");
                        intent.putExtra("emailId",email);
                        startActivity(intent);
                    }
                }catch (NullPointerException ignored){ }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });
    }

}
