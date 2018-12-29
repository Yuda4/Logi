package com.ariel.logi.logi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.ariel.DeliverySystem.Delivery;
import com.ariel.User.Courier;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CourierActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference mDatabaseDelivery;
    private DatabaseReference mDatabaseUser;
    private DatabaseReference mDatabaseQuery;

    private ArrayList<Delivery> dbDeliveryContent;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private Courier courier;

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle drawerToggle;

    private RecyclerView recyclerViewDelivered;
    private RecyclerView rcvDelivered, rcvInProcess;
    private RecyclerViewManagerDelivery adapter;

    private Spinner spinnerCourier;
    private ArrayAdapter<CharSequence> adapterCourierSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        courier = new Courier();

        //get firebase database instance
        mDatabaseDelivery = FirebaseDatabase.getInstance().getReference("deliveries");

        mDatabaseUser = FirebaseDatabase.getInstance().getReference("users").child(Objects
                .requireNonNull(auth.getCurrentUser()).getUid());

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutCourier);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_courier);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        rcvDelivered = (RecyclerView) findViewById(R.id.recycler_view_delivered);

        initRecyclerItems();
        initRecyclerViewDelivered();


        spinnerCourier = (Spinner) findViewById(R.id.spinner_courier_filter);
        adapterCourierSpinner = ArrayAdapter.createFromResource(this, R.array.courier_filter_delivery, android.R.layout.simple_list_item_1);
        adapterCourierSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourier.setAdapter(adapterCourierSpinner);
        spinnerCourier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Query query;
                String itemSelected = (String)parent.getItemAtPosition(position);
                switch(itemSelected){
                    case "In Process":
                        query = mDatabaseDelivery.child(courier.getStorage_id())
                                .orderByChild("status").equalTo("in process");

                        query.addListenerForSingleValueEvent(eventListenerCourier);
                        break;
                    case "Delivered":
                       query = mDatabaseDelivery.child(courier.getStorage_id())
                                .orderByChild("status").equalTo("delivered");

                        query.addListenerForSingleValueEvent(eventListenerCourier);
                        break;
                    case "By Date":

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initRecyclerItems(){
        //Recyclerview db
        dbDeliveryContent = new ArrayList<>();
    }


    private void initRecyclerViewDelivered(){
        recyclerViewDelivered = findViewById(R.id.recycler_view_delivered);
        adapter = new RecyclerViewManagerDelivery(this, dbDeliveryContent);
        recyclerViewDelivered.setAdapter(adapter);
        recyclerViewDelivered.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

        mDatabaseUser.addValueEventListener(courierEventListenerCourier);

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

        auth.addAuthStateListener(authListener);

        // Read from the database
        mDatabaseDelivery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ShowDeliveredData(dataSnapshot);
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


    private void ShowDeliveredData(DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists() && courier != null && dataSnapshot.child(courier.getStorage_id()).exists()){
            for (DataSnapshot ds: dataSnapshot.child(courier.getStorage_id()).getChildren()){
                Delivery delivery = ds.getValue(Delivery.class);
                dbDeliveryContent.add(delivery);
            }
        }

    }

    ValueEventListener courierEventListenerCourier = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            if(dataSnapshot.exists()){
                courier.setStorage_id(dataSnapshot.child("storage_id").getValue(String.class));
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Failed to read value
        }
    };

    ValueEventListener eventListenerCourier = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                dbDeliveryContent.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Delivery delivery = ds.getValue(Delivery.class);
                    dbDeliveryContent.add(delivery);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



}
