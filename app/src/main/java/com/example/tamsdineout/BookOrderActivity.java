package com.example.tamsdineout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookOrderActivity extends AppCompatActivity {

    private Button myMeal,pizzaMeal,drinksMeal,cuisineMeal,proceedOrder;
    private TextView list1Text,list2Text,list3Text,list4Text,totalItems,totalPrice;
    private ListView list1,list2,list3,list4;
    private LinearLayout list1Layout,list2Layout,list3Layout,list4Layout,priceSummaryLayout;
    private List<itemMyMeal> myOrder;
    private MenuMealAdapter myOrderAdapter;
    private MenuCartAdapter myOrderAdapterCart;
    private String itemNameString,priceRegularString,priceMediumString,priceLargeString,priceTotalString,inputPriceString,elegantNoString;
    private int elegantNo;
    private int priceRegularInt,priceMediumInt,priceLargeInt,priceTotalInt;
    private int total=0;
    private int listIndex=0;
    private String[] listMeal=new String[50];
    private String[] listQuantity=new String[50];
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userId;
    private FirebaseFirestore firebaseFirestore;
    private boolean stat=false;
    private CollectionReference documentReference;
    private ImageView myMealImg,pizzaImg,drinksImg,cuisineImg;



    //Menu Record
    private String[] pizzaVeg={ "CHEESE MARGARITA", "PANEER TADKA", "JALEPINOS SPECIAL", "FILMY CORN", "VEGETABLE TADKA", "SPECIAL ITALIANO PANEER", "GARLIC BREAD"};
    private String[] pizzaPriceVeg={"120/190/250", "140/230/290", "190/250/310", "150/230/295", "130/190/230", "160/240/350", "45/95"};

    private String[] pizzaNonVeg={ "GRILLED CHICKEN", "FILMY SAUSAGE", "SPECIAL ITALIANO CHICKEN", "GRILLED BARBACUE", "CHICKEN FRIES"};
    private String[] pizzaPriceNonVeg={ "180/270/350","150/230/290","210/290/360","180/260/310","99/149"};

    private String[] nameCocktail={"BALSAMIC GRANADE", "ALMOND SIDECAR", "MAI TAI", "PERONI", "BLUE MOON", "LEFFE"};
    private String[] priceCocktail={"685","685","670", "320",  "370", "385"};

    private String[] nameMocktail={"COCO MOJO", "APPLE LICHI MOJITO", "THE VALENTINE", "HAWAIN COLADA"};
    private String[] priceMocktail={"250","199","230", "240"};

    private String[] mainCuisineVeg={ "MUTTOR METHI MALAI","PANEER JALFREZI","VEG KADHAI","MUSHROOM MASALA","PANEER BUTTER MASALA"};
    private String[] mainCuisineVegPrice={"320", "350", "310","280","240"};

    private String[] mainCuisineNonVeg={"CHICKEN HANDI","MURGH MARATHA","CHICKEN  MAKKAHNWALA"};
    private String[] mainCuisineNonVegPrice={"470", "430","460"};

    private String[] mainCuisineIBName={"CHEESE NAAN","BUTTER ROTI/NAAN","LACCHA PAROTHA"};
    private String[] mainCuisineIBPrice={"120","140", "70"};

    private String[] mainCuisineRiceName={"MURG RICE","PRAWNS BIRYANI","JEERA RICE","SHEZWAAN RICE","HAYDRABADI RICE","CHICKEN BIRYANI"};
    private String[] mainCuisineRicePrice={"430","460","230","240","290","360"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_order);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        userId=mUser.getUid();

        Button menuAcc = findViewById(R.id.menu);
        Button locAcc = findViewById(R.id.locate);
        Button bookTAcc = findViewById(R.id.bookTable);
        Button profileAcc = findViewById(R.id.orders);

        myMeal=findViewById(R.id.myMenu);
        pizzaMeal=findViewById(R.id.pizza);
        drinksMeal=findViewById(R.id.drinks);
        cuisineMeal=findViewById(R.id.cuisine);
        proceedOrder=findViewById(R.id.proceed);

        list1=findViewById(R.id.list1);
        list2=findViewById(R.id.list2);
        list3=findViewById(R.id.list3);
        list4=findViewById(R.id.list4);

        list1Text=findViewById(R.id.ttl1);
        list2Text=findViewById(R.id.ttl2);
        list3Text=findViewById(R.id.ttl3);
        list4Text=findViewById(R.id.ttl4);
        totalItems=findViewById(R.id.qty);
        totalPrice=findViewById(R.id.ttPrice);

        list1Layout=findViewById(R.id.ll1);
        list2Layout=findViewById(R.id.ll2);
        list3Layout=findViewById(R.id.ll3);
        list4Layout=findViewById(R.id.ll4);
        priceSummaryLayout=findViewById(R.id.r6);


        list2Layout.setVisibility(View.GONE);
        list3Layout.setVisibility(View.GONE);
        list4Layout.setVisibility(View.GONE);

        myMealImg=findViewById(R.id.mealImg);
        pizzaImg=findViewById(R.id.pizzaImg);
        drinksImg=findViewById(R.id.drinksImg);
        cuisineImg=findViewById(R.id.cuisineImg);

        changeView(1);

        myOrder=new ArrayList<>();
        myOrder.clear();

        showMyOrder();


        menuAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myOrder.isEmpty()) {
                    Intent intent = new Intent(BookOrderActivity.this, MenuCardActivity.class);
                    startActivity(intent);
                }else {
                    showCancelCart(1);
                }

            }
        });
        locAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myOrder.isEmpty()) {
                    Intent intent = new Intent(BookOrderActivity.this, LocationActivity.class);
                    startActivity(intent);
                }else {
                    showCancelCart(2);
                }

            }
        });
        bookTAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myOrder.isEmpty()) {
                    Intent intent = new Intent(BookOrderActivity.this, BookTableActivity.class);
                    startActivity(intent);
                }else {
                    showCancelCart(3);
                }

            }
        });
        profileAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myOrder.isEmpty()) {
                    Intent intent = new Intent(BookOrderActivity.this, MainActivity.class);
                    startActivity(intent);

                }else {
                 showCancelCart(4);
                }

            }
        });

        proceedOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(total==0){
                    Toast.makeText(BookOrderActivity.this,"Cart Cant Be Empty",Toast.LENGTH_LONG).show();
                }
                else{
                    showMyOrder();
                    showMyCart();
                }
            }
        });

        myMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(1);
                showMyOrder();
            }
        });

        pizzaMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(2);
                setPizzaMeal();
            }
        });

        drinksMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(3);
                setDrinksMeal();
            }
        });

        cuisineMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(4);
                setCuisineMeal();
            }
        });


    }


    void showCancelCart(final int acc){
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(BookOrderActivity.this).inflate(R.layout.cleancartdialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(BookOrderActivity.this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        Button cancel,remove;

        remove=dialogView.findViewById(R.id.remove);
        cancel=dialogView.findViewById(R.id.cancel);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOrder.clear();
                switch (acc){
                    case 1:Intent intent1 = new Intent(BookOrderActivity.this, MenuCardActivity.class);
                        startActivity(intent1);
                        break;
                    case 2: Intent intent2 = new Intent(BookOrderActivity.this, LocationActivity.class);
                        startActivity(intent2);
                        break;
                    case 3: Intent intent3 = new Intent(BookOrderActivity.this, BookTableActivity.class);
                        startActivity(intent3);
                        break;
                    case 4: Intent intent = new Intent(BookOrderActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void showMyCart(){
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(BookOrderActivity.this).inflate(R.layout.mycartdialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(BookOrderActivity.this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


        ListView list;
        TextView itemPrice,itemQty;
        final Button placeOrder,cancelOrder;

        list=dialogView.findViewById(R.id.listCart);
        itemQty=dialogView.findViewById(R.id.qty);
        itemPrice=dialogView.findViewById(R.id.ttPrice);
        placeOrder=dialogView.findViewById(R.id.placeOrder);
        cancelOrder=dialogView.findViewById(R.id.cancelOrder);

        myOrderAdapterCart=new MenuCartAdapter(this,R.layout.menu_custom,myOrder);
        list.setAdapter(myOrderAdapterCart);
        int no= list.getAdapter().getCount();
        String itemCount=String.valueOf(no);
        itemQty.setText(itemCount);
        String ttl=String.valueOf(total);
        itemPrice.setText(ttl);


        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(placeOrder(total)){
                   alertDialog.dismiss();
               }
               else{
                   alertDialog.dismiss();
               }
            }
        });
        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public boolean placeOrder(int price) {


        String[] finalListOrder = new String[50];
        String[] finalListQuantity = new String[50];
        for (int i = 0; i < listIndex; i++) {
            finalListOrder[i] = listMeal[i];
        }
        for (int i = 0; i < listIndex; i++) {
            finalListQuantity[i] = listQuantity[i];
        }
        String priceStr = String.valueOf(price);

        Map<String,mealString> myMeal =new HashMap<>();

        for(int i=0;i<listIndex;i++){
            String n="Item No"+i;
            myMeal.put(n,new mealString(finalListOrder[i],finalListQuantity[i]));
        }


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        placeMealOrder meal=new placeMealOrder(priceStr,"unPaid",formattedDate,myMeal);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Orders").document(userId).set(meal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                      Toast.makeText(BookOrderActivity.this,"Your Order Is Placed!",Toast.LENGTH_LONG).show();
                      for(int i=0;i<listMeal.length;i++){
                          listMeal[i]=null;
                      }
                      listIndex=0;
                      myOrder.clear();
                      myOrderAdapter.notifyDataSetChanged();
                      list1.setAdapter(myOrderAdapter);
                      total=0;
                      summary();
                      stat=true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BookOrderActivity.this,"Couldn't Place Your Order !",Toast.LENGTH_LONG).show();
                stat=false;
            }
        });

        return stat;


    }

    private void changeLayoutParameters(final int ch){
        if(ch==1){
            int width=LinearLayout.LayoutParams.MATCH_PARENT;
            int height=dpToPx(450);

            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(width,height);
            list1Layout.setLayoutParams(lp);

        }
        else{
            int width=LinearLayout.LayoutParams.MATCH_PARENT;
            int height=dpToPx(200);

            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(width,height);
            list1Layout.setLayoutParams(lp);

        }
    }

    private void showMyOrder(){

        list2Layout.setVisibility(View.GONE);
        list3Layout.setVisibility(View.GONE);
        list4Layout.setVisibility(View.GONE);

        changeLayoutParameters(1);


        list1Text.setText("My Orders ");
        changeView(1);

        myOrderAdapter=new MenuMealAdapter(this,R.layout.menu_custom,myOrder);
        list1.setAdapter(myOrderAdapter);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        summary();
    }

    private void setPizzaMeal(){
        list2Layout.setVisibility(View.VISIBLE);
        list3Layout.setVisibility(View.GONE);
        list4Layout.setVisibility(View.GONE);
        changeLayoutParameters(2);

        List<itemsMenu> pizzaMenuVeg=new ArrayList<>();
        pizzaMenuVeg.clear();
        List<itemsMenu> pizzaMenuNonVeg=new ArrayList<>();
        pizzaMenuNonVeg.clear();
        list1Text.setText("VEG PIZZA");
        list2Text.setText("NON-VEG PIZZA");



        for(int i=0;i<pizzaVeg.length;i++){
            pizzaMenuVeg.add(new itemsMenu(pizzaVeg[i],pizzaPriceVeg[i]));
        }
        for(int i=0;i<pizzaNonVeg.length;i++){
            pizzaMenuNonVeg.add(new itemsMenu(pizzaNonVeg[i],pizzaPriceNonVeg[i]));
        }

        MenuAdapter pizzaMealAdapterVeg=new MenuAdapter(this,R.layout.menu_custom,pizzaMenuVeg);
        list1.setAdapter(pizzaMealAdapterVeg);

        MenuAdapter pizzaMealAdapterNonVeg=new MenuAdapter(this,R.layout.menu_custom,pizzaMenuNonVeg);
        list2.setAdapter(pizzaMealAdapterNonVeg);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToCartDialogue(1,pizzaVeg[position],pizzaPriceVeg[position]);
            }
        });
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToCartDialogue(1,pizzaNonVeg[position],pizzaPriceNonVeg[position]);
            }
        });

    }

    private void setDrinksMeal(){
        list2Layout.setVisibility(View.VISIBLE);
        list3Layout.setVisibility(View.GONE);
        list4Layout.setVisibility(View.GONE);
        changeLayoutParameters(2);

        List<itemsMenu> drinksCocktail=new ArrayList<>();
        drinksCocktail.clear();
        List<itemsMenu> drinksMocktail=new ArrayList<>();
        drinksMocktail.clear();
        list1Text.setText("COCKTAILS");
        list2Text.setText("MOCKTAILS");



        for(int i=0;i<nameCocktail.length;i++){
            drinksCocktail.add(new itemsMenu(nameCocktail[i],priceCocktail[i]));
        }
        for(int i=0;i<nameMocktail.length;i++){
            drinksMocktail.add(new itemsMenu(nameMocktail[i],priceMocktail[i]));
        }

        MenuAdapter drinksCocktailAdapter=new MenuAdapter(this,R.layout.menu_custom,drinksCocktail);
        list1.setAdapter(drinksCocktailAdapter);

        MenuAdapter drinksMocktailAdapter=new MenuAdapter(this,R.layout.menu_custom,drinksMocktail);
        list2.setAdapter(drinksMocktailAdapter);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToCartDialogue(2,nameCocktail[position],priceCocktail[position]);
            }
        });
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToCartDialogue(2,nameMocktail[position],priceMocktail[position]);
            }
        });

    }

    private void setCuisineMeal(){

        list2Layout.setVisibility(View.VISIBLE);
        list3Layout.setVisibility(View.VISIBLE);
        list4Layout.setVisibility(View.VISIBLE);
        changeLayoutParameters(2);


        List<itemsMenu> vegCuisine=new ArrayList<>();
        vegCuisine.clear();
        List<itemsMenu> nonVegCuisine=new ArrayList<>();
        nonVegCuisine.clear();
        List<itemsMenu> indianBreadsCuisne=new ArrayList<>();
        indianBreadsCuisne.clear();
        List<itemsMenu> riceCuisine=new ArrayList<>();
        riceCuisine.clear();
        list1Text.setText("VEG");
        list2Text.setText("NON-VEG");
        list3Text.setText("INDIAN BREADS");
        list4Text.setText("RICE");



        for(int i=0;i<mainCuisineVeg.length;i++){
            vegCuisine.add(new itemsMenu(mainCuisineVeg[i],mainCuisineVegPrice[i]));
        }
        for(int i=0;i<mainCuisineNonVeg.length;i++){
            nonVegCuisine.add(new itemsMenu(mainCuisineNonVeg[i],mainCuisineNonVegPrice[i]));
        }
        for(int i=0;i<mainCuisineIBName.length;i++){
            indianBreadsCuisne.add(new itemsMenu(mainCuisineIBName[i],mainCuisineIBPrice[i]));
        }
        for(int i=0;i<mainCuisineRiceName.length;i++){
            riceCuisine.add(new itemsMenu(mainCuisineRiceName[i],mainCuisineRicePrice[i]));
        }
        MenuAdapter vegCuisineAdapter=new MenuAdapter(this,R.layout.menu_custom,vegCuisine);
        list1.setAdapter(vegCuisineAdapter);

        MenuAdapter nonVegCuisineAdapter=new MenuAdapter(this,R.layout.menu_custom,nonVegCuisine);
        list2.setAdapter(nonVegCuisineAdapter);

        MenuAdapter indianBreadsAdapter=new MenuAdapter(this,R.layout.menu_custom,indianBreadsCuisne);
        list3.setAdapter(indianBreadsAdapter);

        MenuAdapter riceCuisineAdapter=new MenuAdapter(this,R.layout.menu_custom,riceCuisine);
        list4.setAdapter(riceCuisineAdapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToCartDialogue(3,mainCuisineVeg[position],mainCuisineVegPrice[position]);
            }
        });
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToCartDialogue(3,mainCuisineNonVeg[position],mainCuisineNonVegPrice[position]);
            }
        });
        list3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToCartDialogue(3,mainCuisineIBName[position],mainCuisineIBPrice[position]);
            }
        });
        list4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToCartDialogue(3,mainCuisineRiceName[position],mainCuisineRicePrice[position]);
            }
        });


    }

    private void summary(){
       int no= list1.getAdapter().getCount();
        String itemCount=String.valueOf(no);
        String itemT=String.valueOf(total);
        totalPrice.setText(itemT);
        totalItems.setText(itemCount);
    }


    private void changeView(int t){
       switch (t){
           case 1:myMealImg.setBackgroundColor(Color.rgb(211, 67, 78));
               pizzaImg.setBackgroundColor(Color.rgb(255, 255, 255));
               drinksImg.setBackgroundColor(Color.rgb(255, 255, 255));
               cuisineImg.setBackgroundColor(Color.rgb(255, 255, 255));
               break;
           case 2:pizzaImg.setBackgroundColor(Color.rgb(211, 67, 78));
               myMealImg.setBackgroundColor(Color.rgb(255, 255, 255));
               drinksImg.setBackgroundColor(Color.rgb(255, 255, 255));
               cuisineImg.setBackgroundColor(Color.rgb(255, 255, 255));
               break;
           case 3:drinksImg.setBackgroundColor(Color.rgb(211, 67, 78));
               pizzaImg.setBackgroundColor(Color.rgb(255, 255, 255));
               myMealImg.setBackgroundColor(Color.rgb(255, 255, 255));
               cuisineImg.setBackgroundColor(Color.rgb(255, 255, 255));
               break;
           case 4:cuisineImg.setBackgroundColor(Color.rgb(211, 67, 78));
               pizzaImg.setBackgroundColor(Color.rgb(255, 255, 255));
               drinksImg.setBackgroundColor(Color.rgb(255, 255, 255));
               myMealImg.setBackgroundColor(Color.rgb(255, 255, 255));
               break;
       }


    }

    public  void addToCartDialogue(final int type,final String name,final String price){
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(BookOrderActivity.this).inflate(R.layout.additemtocart, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(BookOrderActivity.this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        final TextView itemName,priceRegular,priceMedium,priceLarge,priceTotal;
        Button addToMeal;
        final ElegantNumberButton quantityItem;
        final RadioButton checkRegular,checkMedium,checkLarge;
        ImageView imageItem;
        final int typeInt;
        String[] result=new String[3];

        itemName=dialogView.findViewById(R.id.itemName);
        priceRegular=dialogView.findViewById(R.id.priceSmall);
        priceMedium=dialogView.findViewById(R.id.priceMedium);
        priceLarge=dialogView.findViewById(R.id.priceLarge);
        priceTotal=dialogView.findViewById(R.id.priceTotal);
        addToMeal=dialogView.findViewById(R.id.addToMeal);
        quantityItem=dialogView.findViewById(R.id.quantityB);
        checkRegular=dialogView.findViewById(R.id.regular);
        checkMedium=dialogView.findViewById(R.id.medium);
        checkLarge=dialogView.findViewById(R.id.large);
        imageItem=dialogView.findViewById(R.id.itemImage);

        itemNameString=name;
        inputPriceString=price;
        typeInt=type;

        itemName.setText(itemNameString);
        elegantNoString="1";

        if(typeInt==1){//pizza
            imageItem.setImageResource(R.drawable.pizza);
           result=inputPriceString.split("/");
           priceRegularString=result[0];
           priceMediumString=result[1];
           try{
               priceLargeString=result[2];
               priceLarge.setText(priceLargeString);
               priceLargeInt=Integer.parseInt(priceLargeString);
           }catch (ArrayIndexOutOfBoundsException e){
               priceLargeString="";
               checkLarge.setVisibility(View.GONE);
               priceLarge.setVisibility(View.GONE);
           }
           priceRegular.setText(priceRegularString);
           priceMedium.setText(priceMediumString);
           checkRegular.setChecked(true);
           checkMedium.setChecked(false);
           checkLarge.setChecked(false);
           priceTotalString=priceRegularString;
           priceTotal.setText(priceTotalString);
           priceRegularInt=Integer.parseInt(priceRegularString);
           priceMediumInt=Integer.parseInt(priceMediumString);

        }
        else {
            if(typeInt==2){
                imageItem.setImageResource(R.drawable.drinks);
            }
            else{
                imageItem.setImageResource(R.drawable.cuisin);
            }
            checkMedium.setVisibility(View.GONE);
            checkLarge.setVisibility(View.GONE);
            priceLarge.setVisibility(View.GONE);
            priceMedium.setVisibility(View.GONE);
            checkRegular.setChecked(true);
            priceRegular.setText(price);
            priceTotalString=price;
            priceTotal.setText(priceTotalString);
            priceRegularInt=Integer.parseInt(price);
        }

        checkRegular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMedium.setChecked(false);
                checkLarge.setChecked(false);
                quantityItem.setNumber("1");
                priceTotal.setText(priceRegularString);

            }
        });
        checkMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRegular.setChecked(false);
                checkLarge.setChecked(false);
                quantityItem.setNumber("1");
                priceTotal.setText(priceMediumString);

            }
        });
        checkLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMedium.setChecked(false);
                checkRegular.setChecked(false);
                quantityItem.setNumber("1");
                priceTotal.setText(priceLargeString);

            }
        });

        quantityItem.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                elegantNoString=quantityItem.getNumber();
                elegantNo=Integer.parseInt(elegantNoString);

                if(checkRegular.isChecked()){
                    priceTotalInt=elegantNo*priceRegularInt;
                    priceTotalString=String.valueOf(priceTotalInt);
                    priceTotal.setText(priceTotalString);

                }else if(checkMedium.isChecked()){
                    priceTotalInt=elegantNo*priceMediumInt;
                    priceTotalString=String.valueOf(priceTotalInt);
                    priceTotal.setText(priceTotalString);
                }
                else{
                    priceTotalInt=elegantNo*priceLargeInt;
                    priceTotalString=String.valueOf(priceTotalInt);
                    priceTotal.setText(priceTotalString);
                }
            }
        });

        addToMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemMyMeal e=new itemMyMeal(typeInt,name,elegantNoString,priceTotalString);
                myOrder.add(e);
                int pr=Integer.parseInt(priceTotalString);
                total+=pr;
                String ttl=String.valueOf(total);
                totalPrice.setText(ttl);

                listMeal[listIndex]=name;
                listQuantity[listIndex]=elegantNoString;
                listIndex=listIndex+1;

                showMyOrder();

                alertDialog.dismiss();
            }
        });

    }
    static class MenuCartAdapter extends ArrayAdapter<itemMyMeal> {

        private Context context;
        private List<itemMyMeal> mealList;

        MenuCartAdapter(Context context, int resource, List<itemMyMeal> mealList) {
            super(context, resource, mealList);

            this.context = context;
            this.mealList = mealList;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.meal_customdialog, null);
            ImageView typeImg, cancelOrder;
            TextView name, qty, price;
            int type;
            int priceInt;

            typeImg = view.findViewById(R.id.typeimg);
            name = view.findViewById(R.id.name);
            qty = view.findViewById(R.id.qty);
            price = view.findViewById(R.id.price);
            cancelOrder = view.findViewById(R.id.cancel);

            final itemMyMeal meal = mealList.get(position);

            type = meal.getType();
            switch (type) {
                case 1:
                    typeImg.setImageResource(R.drawable.pizza);
                    break;
                case 2:
                    typeImg.setImageResource(R.drawable.drinks);
                    break;
                case 3:
                    typeImg.setImageResource(R.drawable.cuisin);
                    break;
            }

            name.setText(meal.getName());
            qty.setText(meal.getQuantity());
            price.setText(meal.getPrice());
            cancelOrder.setVisibility(View.GONE);

            return view;
        }
    }


    class MenuMealAdapter extends ArrayAdapter<itemMyMeal> {

        private Context context;
        private List<itemMyMeal> mealList;

        MenuMealAdapter(Context context, int resource, List<itemMyMeal> mealList) {
            super(context, resource, mealList);

            this.context=context;
            this.mealList=mealList;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.meal_customdialog,null);
            ImageView typeImg,cancelOrder;
            TextView name,qty,price;
            int type;
            int priceInt;

            typeImg=view.findViewById(R.id.typeimg);
            name=view.findViewById(R.id.name);
            qty=view.findViewById(R.id.qty);
            price=view.findViewById(R.id.price);
            cancelOrder=view.findViewById(R.id.cancel);

             final itemMyMeal meal=mealList.get(position);

             type=meal.getType();
             switch(type){
                 case 1: typeImg.setImageResource(R.drawable.pizza);
                     break;
                 case 2:typeImg.setImageResource(R.drawable.drinks);
                     break;
                 case 3:typeImg.setImageResource(R.drawable.cuisin);
                     break;
            }

            name.setText(meal.getName());
            qty.setText(meal.getQuantity());
            price.setText(meal.getPrice());

            cancelOrder.setImageResource(R.drawable.ic_cancel_black_24dp);

            cancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 int pr=Integer.parseInt(meal.getPrice());
                 showDialogue(position,pr);
                }
            });

            return view;
        }

        void showDialogue(final int pos, final int price){
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(BookOrderActivity.this).inflate(R.layout.removeorderdialog, viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(BookOrderActivity.this);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            Button cancel,remove;

            remove=dialogView.findViewById(R.id.remove);
            cancel=dialogView.findViewById(R.id.cancel);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   myOrder.remove(pos);
                   myOrderAdapter.notifyDataSetChanged();
                    myOrderAdapterCart.notifyDataSetChanged();
                   total-=price;
                   String ttl=String.valueOf(total);
                   totalPrice.setText(ttl);
                    listMeal[listIndex]=null;
                    listQuantity[listIndex]=null;
                    listIndex=listIndex-1;
                   summary();
                   alertDialog.dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }
    }
}

