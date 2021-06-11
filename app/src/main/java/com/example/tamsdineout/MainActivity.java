package com.example.tamsdineout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser mUser;
    private String userName, payment;
    private String userEmail;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView name, email, orderDate, orderPayment,titleDashboard;
    private DocumentReference documentReference;
    private List<myOrderItems> meal;
    private ImageView img;
    private LinearLayout myActivityLayout;
    private RelativeLayout mealActivityL,noActivityLayout;
    private int stat=0;
    private int tableStat=0;

    private String tableDate,tableTime,tablePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        name = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);

        Button signOut = findViewById(R.id.button1);
        Button menuAcc = findViewById(R.id.menu);
        Button locAcc = findViewById(R.id.locate);
        Button bookTAcc = findViewById(R.id.bookTable);
        Button orderAcc = findViewById(R.id.orders);
        Button myAcc=findViewById(R.id.myActivity);
        final Button myOrders = findViewById(R.id.btnMyOrders);

        myActivityLayout=findViewById(R.id.l5);
        noActivityLayout=findViewById(R.id.r9);
        mealActivityL=findViewById(R.id.r4);

        myActivityLayout.setVisibility(View.GONE);
        img = findViewById(R.id.imgUser);


        orderDate = findViewById(R.id.getDate);
        orderPayment = findViewById(R.id.getPrice);
        titleDashboard=findViewById(R.id.fieldName);

        meal = new ArrayList<>();


        menuAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuCardActivity.class);
                startActivity(intent);

            }
        });
        locAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);

            }
        });
        bookTAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tableStat==0) {
                    Intent intent = new Intent(MainActivity.this, BookTableActivity.class);
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("field", email.getText().toString());
                    intent.putExtra("table",tableStat);
                    startActivity(intent);
                }
                else if(tableStat==1){
                    Intent intent = new Intent(MainActivity.this, BookTableActivity.class);
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("field", email.getText().toString());
                    intent.putExtra("table",tableStat);
                    intent.putExtra("bookedTime",tableTime);
                    intent.putExtra("bookedDate",tableDate);
                    intent.putExtra("bookedPayment",tablePayment);
                    startActivity(intent);

                }

            }
        });
        orderAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookOrderActivity.class);
                startActivity(intent);

            }
        });

        myAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(stat==0){
                    stat=1;
                    myActivityLayout.setVisibility(View.VISIBLE);
                }
                else{
                    stat=0;
                    myActivityLayout.setVisibility(View.GONE);
                }

            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadProfile() == 1) {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);
                    mGoogleSignInClient.signOut();
                    FirebaseAuth.getInstance().signOut();
                } else if (loadProfile() == 2) {
                    LoginManager.getInstance().logOut();
                    FirebaseAuth.getInstance().signOut();
                } else if (loadProfile() == 3) {
                    FirebaseAuth.getInstance().signOut();
                }
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);


            }
        });
        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyCart();
            }
        });


        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String named=name.getText().toString();
                SharedPreferences settings=getSharedPreferences("PREFS",MODE_PRIVATE);
                SharedPreferences.Editor editor=settings.edit();
                editor.putString("name",named);
                editor.apply();
            }
        },5000);



    }

    public void getOrder(final String userId) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection("Orders").document(userId);


        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               // DocumentSnapshot documentSnapshot = task.getResult();
                placeMealOrder getMeal = task.getResult().toObject(placeMealOrder.class);
                try {
                    assert getMeal != null;
                    String date = getMeal.getDate();
                    String price = getMeal.getPrice();
                    String status = getMeal.getStatus();
                    Map<String, mealString> mealL = getMeal.getMap();

                    orderDate.setText(date);
                    payment = "Rs. " + price + " (" + status + " ) ";
                    orderPayment.setText(payment);

                    mealString[] m = new mealString[20];

                    for (int i = 0; i < mealL.size(); i++) {
                        String item = "Item No" + i;
                        m[i] = mealL.get(item);
                    }

                    for (int i = 0; i < mealL.size(); i++) {
                        assert m[i] != null;
                        meal.add(new myOrderItems(m[i].mealName, m[i].mealQty));
                    }
                    noActivityLayout.setVisibility(View.GONE);
                    mealActivityL.setVisibility(View.VISIBLE);

                } catch (NullPointerException n) {
                    documentReference=FirebaseFirestore.getInstance().collection("Bookings").document(userId);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                tableDate=documentSnapshot.getString("date");
                                tableTime=documentSnapshot.getString("time");
                                tablePayment=documentSnapshot.getString("payment");

                                String dateFinal=tableDate+" "+tableTime;

                                orderDate.setText(dateFinal);
                                orderPayment.setText(tablePayment);
                                titleDashboard.setText("MY RESERVATIONS");

                                tableStat=1;

                                noActivityLayout.setVisibility(View.INVISIBLE);
                            }
                            else {
                                mealActivityL.setVisibility(View.INVISIBLE);
                                noActivityLayout.setVisibility(View.VISIBLE);
                                tableStat=0;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mealActivityL.setVisibility(View.INVISIBLE);

                        }
                    });
                }

            }
        });
}

    public void showMyCart(){
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.mycartdialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


        ListView list;
        TextView itemPrice,itemQty;
        RelativeLayout relativeLayout;

        list=dialogView.findViewById(R.id.listCart);
        itemQty=dialogView.findViewById(R.id.qty);
        itemPrice=dialogView.findViewById(R.id.ttPrice);
        relativeLayout=dialogView.findViewById(R.id.l4);
        relativeLayout.setVisibility(View.GONE);

        itemPrice.setText(payment);

        MenuCartAdapter myOrderAdapterCart = new MenuCartAdapter(MainActivity.this, R.layout.menu_custom, meal);
        list.setAdapter(myOrderAdapterCart);

        int no= list.getAdapter().getCount();
        String itemCount=String.valueOf(no);
        itemQty.setText(itemCount);

    }
    public void getUserData(final String userEidSystem){

        DocumentReference documentReference=FirebaseFirestore.getInstance().collection("Users").document(userEidSystem);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String nameUser = documentSnapshot.getString("name");
                    String phoneUser = documentSnapshot.getString("phoneNo");
                    String emailUser = documentSnapshot.getString("emailId");

                    name.setText(nameUser);
                    assert emailUser != null;
                    if (emailUser.equals("null")) {
                        email.setText(phoneUser);
                    } else {
                        email.setText(emailUser);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
    private void getUserFBProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String emailId = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            userName=first_name+" "+last_name;
                            userEmail=emailId;

                            name.setText(userName);
                            email.setText(userEmail);
                         //   new DownloadImageTask((ImageView) findViewById(R.id.imgUser)).execute(image_url);
                            Picasso.with(getApplicationContext())
                                    .load(image_url)
                                    .into(img);

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
    private void getUserGglProfile(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String url=acct.getPhotoUrl().toString();

            name.setText(personName);
            email.setText(personEmail);
          //  new DownloadImageTask((ImageView) findViewById(R.id.imgUser)).execute(url);
            Picasso.with(getApplicationContext())
                    .load(url)
                    .into(img);
        }
    }
    public int loadProfile(){
        int type;
        Profile profile = Profile.getCurrentProfile();
       if(GoogleSignIn.getLastSignedInAccount(MainActivity.this)!=null){
           type=1; //google sign in
       }
       else if(profile!=null){
           type=2;//facebook sign in
       }
       else{
           type=3;
       }
       return type;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mUser==null){
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        else{
            String userId = mUser.getUid();
            if(loadProfile()==1){
                getUserGglProfile();
            }
            else if(loadProfile()==2){
                getUserFBProfile(AccessToken.getCurrentAccessToken());
            }
            else if(loadProfile()==3){
                getUserData(userId);
            }

            getOrder(userId);
        }
    }


    static class MenuCartAdapter extends ArrayAdapter<myOrderItems> {

        private Context context;
        private List<myOrderItems> mealList;

        MenuCartAdapter(Context context, int resource, List<myOrderItems> mealList) {
            super(context, resource, mealList);

            this.context = context;
            this.mealList = mealList;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.menu_custom, null);
            TextView name,qty;

            name = view.findViewById(R.id.name);
            qty=view.findViewById(R.id.price);

            final myOrderItems meal = mealList.get(position);

            name.setText(meal.getName());
            qty.setText(meal.getQuantity());

            return view;
        }
    }

}
