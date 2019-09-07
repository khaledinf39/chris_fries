package com.kh_sof_dev.chris_fries.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_sof_dev.chris_fries.Adapters.Products_adapter;
import com.kh_sof_dev.chris_fries.Clasess.GPSTracker;
import com.kh_sof_dev.chris_fries.Clasess.users;
import com.kh_sof_dev.chris_fries.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.kh_sof_dev.chris_fries.Clasess.Product;

public class MainActivity extends AppCompatActivity {
    private Button my_order,logout;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Products_adapter adapter;
    private List<Product> productList;
    private  String id;
    private RecyclerView recyclerView;
private TextView name,cod,phone,address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.progress).setVisibility(View.GONE);
                findViewById(R.id.noItem).setVisibility(View.VISIBLE);


            }
        }, 5000);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        productList=new ArrayList<>();
        my_order=findViewById(R.id.myOrders);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        my_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Odrer_activity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        adapter=new Products_adapter(this ,productList, null);
        recyclerView=(RecyclerView) findViewById(R.id.List_prod);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        fesh_data();

        userInfo();
        update_location();
    }
private void save_location(String Address){
    SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
    SharedPreferences.Editor Ed=sp.edit();
    Ed.putString("address", String.valueOf(Address));
    Ed.commit();

    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    reference.child("address").setValue(Address);
}
private users getUser_inf(){
    SharedPreferences sp=getSharedPreferences("user_info", MODE_PRIVATE);
    users user=new users();
    user.setName(sp.getString("name",getString(R.string.name)));
    user.setAddress(sp.getString("address",getString(R.string.name)));
    return user;
}
    private void update_location() {
        GPSTracker gps = new GPSTracker(MainActivity.this);
        // check if GPS enabled
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            gps.stopUsingGPS();
            // \n is for new line
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
getcityname(latitude,longitude);

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
//
    }
    private void getcityname(Double lat,Double lng){
        try {
            /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses;

            addresses = gcd.getFromLocation(lat,
                    lng, 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();// addresses.get(0).getLocality();
                Log.d("cityName",cityName);
                address.setText(cityName);
                save_location(cityName);

            }



        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void userInfo() {
    name=findViewById(R.id.user_name);
    phone=findViewById(R.id.phone);
    cod=findViewById(R.id.code);
    address=findViewById(R.id.address);
FirebaseAuth auth=FirebaseAuth.getInstance();

        phone.setText(auth.getCurrentUser().getPhoneNumber());
        cod.setText(auth.getCurrentUser().getUid());
        users user=getUser_inf();
        name.setText(user.getName());
        address.setText(user.getAddress());


    }

    private void fesh_data() {
        reference.child("Products").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (productList.size()==0){
                    findViewById(R.id.progress).setVisibility(View.GONE);
                }
                Product product=dataSnapshot.getValue(Product.class);
                product.setId(dataSnapshot.getKey());
                Log.d("prod_id 1",dataSnapshot.getKey());
                productList.add(product);
                Log.d("product :",product.getName()+"  "+product.getId());
                adapter.notifyDataSetChanged();
//

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





}
