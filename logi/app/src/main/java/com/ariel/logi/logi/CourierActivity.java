package com.ariel.logi.logi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.ariel.DeliverySystem.Delivery;
import com.ariel.User.Courier;
import com.ariel.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CourierActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Courier courier;
    private Button pendingBttn, inProcessBttn, deliveredBttn;

    private DatabaseReference mDatabaseDeliveries;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    private ArrayList<Delivery> inProcessContent;
    private ArrayList<Delivery> deliveredContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        courier = new Courier();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutCourier);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_courier);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        //get firebase database instance
        mDatabaseDeliveries = FirebaseDatabase.getInstance().getReference();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(CourierActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        pendingBttn = (Button) findViewById(R.id.btn_pending_approval);
        inProcessBttn = (Button) findViewById(R.id.btn_in_process);
        deliveredBttn = (Button) findViewById(R.id.btn_delivered);


        pendingBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pendingBttn.isActivated()){
                    //newEmail.setVisibility(View.VISIBLE);
                    //changeEmail.setVisibility(View.VISIBLE);
                }else {
                    //newEmail.setVisibility(View.GONE);
                    //changeEmail.setVisibility(View.GONE);
                }
                pendingBttn.setActivated(!pendingBttn.isActivated());
                /*if(btnChangePassword.isActivated()) btnChangePassword.setActivated(false);
                oldEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.GONE);
                remove.setVisibility(View.GONE);*/
            }
        });


    }


    private void initRecyclerItems(ArrayList<String> mLable, ArrayList<String> mContent){
        mLable.add("courier_email:");
        mContent.add("Enter courier's email");
        mLable.add("courier_phone:");
        mContent.add("Enter courier's phone");
        mLable.add("customer_email:");
        mContent.add("Enter customer's email");
        mLable.add("customer_phone:");
        mContent.add("Enter customer's phone");
        mLable.add("date:");
        mContent.add("Enter date of delivery");
        mLable.add("delivery_id:");
        mContent.add("Enter delivery's id");
        mLable.add("product_id:");
        mContent.add("Enter product's id");
        mLable.add("status:");
        mContent.add("Enter delivery's status");
    }

    private void initRecyclerViewInProcess(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view_in_process);
        RecyclerViewCourier adapter = new RecyclerViewCourier(this, inProcessContent);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initRecyclerViewDelivered(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view_delivered);
        RecyclerViewCourier adapter = new RecyclerViewCourier(this, deliveredContent);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);

        // Read from the database
        mDatabaseDeliveries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ShowData(dataSnapshot);

                initRecyclerViewInProcess();
                initRecyclerViewDelivered();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.home:
                Toast.makeText(CourierActivity.this, "Home Press!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                startActivity(new Intent(CourierActivity.this, ProfileActivity.class));
                break;
            case R.id.setting:
                startActivity(new Intent(CourierActivity.this, SettingsActivity.class));
                finish();
                break;
            case R.id.logout:
                signOut();
                finish();
                break;
        }
        return false;
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    private void ShowData(DataSnapshot dataSnapshot) {
        Delivery delivery = new Delivery();
        for (DataSnapshot ds: dataSnapshot.getChildren()){
            String storage_id = courier.getStorage_id();
            delivery = ds.child(storage_id).getValue(Delivery.class);
            if (delivery != null) {
                String status = delivery.getStatus();
                switch (status){
                    case "in process":
                       /* delivery.setUserId(userID);
                        inProcessContent.set(0, delivery.get);
                        inProcessContent.set(1, user.getEmail());
                        inProcessContent.set(2, user.getPhone());
                        inProcessContent.set(3, user.getType());
                        inProcessContent.set(4, user.getCountry());
                        inProcessContent.set(5, user.getCity());
                        inProcessContent.set(6, user.getAddress());
                        inProcessContent.set(7, Long.toString(user.getZip_code()));
                        startActivity(new Intent(CourierActivity.this, MainActivity.class));*/
                        // Toast.makeText(ProfileActivity.this, "Home Press!", Toast.LENGTH_SHORT).show();
                        break;
                    case "delivered":
                        break;
                    //startActivity(new Intent(CourierActivity.this, MainActivity.class));
                }


                //emailTextView.setText(user.getEmail());
                //nameTextView.setText(user.getName());
            }
        }
        /*if (delivery != null)
            Toast.makeText(CourierActivity.this, "Your email is " + delivery.getEmail(), Toast.LENGTH_SHORT ).show();*/

    }



}
