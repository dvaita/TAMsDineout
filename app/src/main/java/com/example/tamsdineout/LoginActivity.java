package com.example.tamsdineout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout;

    private TextView editPhone,editOtp;

    private SignInButton googleButton;
    private Button sendOtp,verifyOtp;

    private String userName,userEmail,personName,personEmail,completePhoneNo,mAuthCredentials;
    private String TAG = "Firebase";

    private static final int RC_SIGN_IN =123 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        sendOtp=findViewById(R.id.btn_otp);
        googleButton=findViewById(R.id.btn_google);
        LoginButton loginButton = findViewById(R.id.btn_fb);
        Button emailButton = findViewById(R.id.btn_email);

        relativeLayout=findViewById(R.id.r3);
        linearLayout=findViewById(R.id.ri1);

        editOtp=findViewById(R.id.edit_otp);
        editPhone=findViewById(R.id.edit_phone);
        verifyOtp=findViewById(R.id.btn_verify);


        relativeLayout.setVisibility(View.GONE);
        verifyOtp.setVisibility(View.GONE);

        createRequest();

        //Email Login Intent
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,EmailLoginActivity.class);
                startActivity(intent);
            }
        });

        //FaceBook Login Intent

//        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                // App code
            }
            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        //phone auth

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber;
                phoneNumber=editPhone.getText().toString();
                if(phoneNumber.length()==10){
                    completePhoneNo="+91" + phoneNumber;
                    completePhoneNo.length();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            completePhoneNo,
                            60,
                            TimeUnit.SECONDS,
                            LoginActivity.this,
                            mCallbacks
                    );
                }
                else {
                    Toast.makeText(LoginActivity.this,"Enter the Valid Phone Number2",Toast.LENGTH_LONG).show();
                }

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //super.onCodeSent(s, forceResendingToken);
                mAuthCredentials=s;
                relativeLayout.setVisibility(View.VISIBLE);
                verifyOtp.setVisibility(View.VISIBLE);
                sendOtp.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
            }
        };

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpEntered=editOtp.getText().toString();
                if(otpEntered.isEmpty()){
                    Toast.makeText(LoginActivity.this,"PLEASE ENTERED THE OTP SENT ON \n",Toast.LENGTH_LONG).show();
                }
                else {
                    PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(mAuthCredentials,otpEntered);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }
        });
    }

    //

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            //String id = object.getString("id");
                           // String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            userName=first_name+" "+last_name;
                            userEmail=email;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(LoginActivity.this,"Successful",Toast.LENGTH_LONG).show();
                            getUserProfile(AccessToken.getCurrentAccessToken());
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    checkUserExist("facebook",userName,"null",userEmail);
                                }
                            },5000);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void updateUI(FirebaseUser user) {
        if(user!=null) {
            Toast.makeText(LoginActivity.this, "You are Logged In", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void createRequest(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                            if (acct != null) {
                                 personName = acct.getDisplayName();
                                 personEmail = acct.getEmail();
                            }
                            checkUserExist("google",personName,"null",personEmail);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this,"Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

        private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "LOADING PLEASE WAIT", Toast.LENGTH_LONG).show();

                                checkUserExist("phone", "null", completePhoneNo, "null");
                            }  else {
                                Toast.makeText(LoginActivity.this, "FAILED TO LOG YOU IN", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

   private void checkUserExist(final String type,final String name,final String phone,final String email){
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
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Intent intent=new Intent(LoginActivity.this,UserInformationActivity.class);
                            intent.putExtra("type",type);
                            intent.putExtra("phoneNo",phone);
                            intent.putExtra("name",name);
                            intent.putExtra("emailId",email);
                            startActivity(intent);
                        }
                    }catch (NullPointerException ignored){}
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
           finish();
                }
            });
    }

}
