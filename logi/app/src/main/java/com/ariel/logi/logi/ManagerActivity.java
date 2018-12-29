package com.ariel.logi.logi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ariel.DeliverySystem.Delivery;
import com.ariel.Storage.Product;
import com.ariel.User.Courier;
import com.ariel.User.User;

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

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class ManagerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "ManagerActivity";

    private DatabaseReference mDatabaseDelivery;
    private DatabaseReference mDatabaseProduct;
    private DatabaseReference mDatabaseCourier;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ArrayList<Courier> dbCourier;
    private ArrayList<Product> dbProduct;
    private ArrayList<Delivery> dbDelivery;

    private Button btnNewProduct, btnNewCourier, btnNewDelivery;
    private RecyclerView rcvProduct, rcvCourier, rcvDelivery;
    private TextView nothogToShow;
    private TextView icProduct, icDelivery, icCourier;
    private RelativeLayout rlDelivery, rlCourier, rlProduct;
    private RecyclerView recyclerViewProduct, recyclerViewDelivery, recyclerViewCourier;

    private Spinner spinnerDelivery;
    private ArrayAdapter<CharSequence> adapterDeliverySpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_manager);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutManager);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_manager);
        navigationView.setNavigationItemSelectedListener(this);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);

        drawerToggle.syncState();
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView.bringToFront();

        spinnerDelivery = (Spinner) findViewById(R.id.spinner_delivery);
        adapterDeliverySpinner = ArrayAdapter.createFromResource(this, R.array.filter_delivery, android.R.layout.simple_list_item_1);
        adapterDeliverySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDelivery.setAdapter(adapterDeliverySpinner);
        spinnerDelivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Toast.makeText(ManagerActivity.this, parent.getItemAtPosition(position) + " selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        icCourier = (TextView) findViewById(R.id.ic_toolbar_couriers);
        icDelivery = (TextView) findViewById(R.id.ic_toolbar_deliveries);
        icProduct = (TextView) findViewById(R.id.ic_toolbar_products);

        nothogToShow = (TextView) findViewById(R.id.no_item_to_show);
        nothogToShow.setVisibility(View.GONE);

        btnNewProduct = (Button) findViewById(R.id.btn_new_product);
        btnNewCourier = (Button) findViewById(R.id.btn_new_courier);
        btnNewDelivery = (Button) findViewById(R.id.btn_new_delivery);

        rlCourier = (RelativeLayout) findViewById(R.id.rl_courier);
        rlDelivery = (RelativeLayout) findViewById(R.id.rl_delivery);
        rlProduct = (RelativeLayout) findViewById(R.id.rl_product);

        rlProduct.setVisibility(View.GONE);
        rlCourier.setVisibility(View.GONE);

        //recyclerView init
        rcvProduct = (RecyclerView) findViewById(R.id.rcv_manager_product);
        rcvCourier = (RecyclerView) findViewById(R.id.rcv_manager_courier);
        rcvDelivery = (RecyclerView) findViewById(R.id.rcv_manager_delivery);

        rcvProduct.setVisibility(View.GONE);
        rcvCourier.setVisibility(View.GONE);


        initRecyclerItems();
        initProductsRecyclerView();
        initDeliveryRecyclerView();
        initCourierRecyclerView();

        if (rcvDelivery.getChildCount() == 0)
            nothogToShow.setVisibility(View.VISIBLE);
        else nothogToShow.setVisibility(View.GONE);

        // float button
        FabSpeedDial fabAdd = (FabSpeedDial) findViewById(R.id.float_add_manager);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get firebase database instance
        mDatabaseDelivery = FirebaseDatabase.getInstance().getReference("deliveries");
        mDatabaseProduct = FirebaseDatabase.getInstance().getReference("products");
        mDatabaseCourier =  FirebaseDatabase.getInstance().getReference("users");

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ManagerActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        fabAdd.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.action_delivery:
                        Toast.makeText(ManagerActivity.this, "Add Delivery Press!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_courier:

                        startActivity(new Intent(ManagerActivity.this, CreateCourier.class));
//                        Toast.makeText(ManagerActivity.this, "Add Courier Press!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_product:
                        Toast.makeText(ManagerActivity.this, "Add Product Press!", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }

            @Override
            public void onMenuClosed() {

            }
        });

        icProduct.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rcvProduct.getChildCount() == 0)
                    nothogToShow.setVisibility(View.VISIBLE);
                else nothogToShow.setVisibility(View.GONE);
                // Product show
                rcvProduct.setVisibility(View.VISIBLE);
                rlProduct.setVisibility(View.VISIBLE);
                // Courier Hide
                rcvCourier.setVisibility(View.GONE);
                rlCourier.setVisibility(View.GONE);
                // Delivery Hide
                rcvDelivery.setVisibility(View.GONE);
                rlDelivery.setVisibility(View.GONE);
            }
        }));

        icDelivery.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rcvDelivery.getChildCount() == 0)
                    nothogToShow.setVisibility(View.VISIBLE);
                else nothogToShow.setVisibility(View.GONE);
                // Product hide
                rcvProduct.setVisibility(View.GONE);
                rlProduct.setVisibility(View.GONE);
                // Courier Hide
                rcvCourier.setVisibility(View.GONE);
                rlCourier.setVisibility(View.GONE);
                // Delivery show
                rcvDelivery.setVisibility(View.VISIBLE);
                rlDelivery.setVisibility(View.VISIBLE);
            }
        }));

        icCourier.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rcvCourier.getChildCount() == 0)
                    nothogToShow.setVisibility(View.VISIBLE);
                else nothogToShow.setVisibility(View.GONE);
                // Product hide
                rcvProduct.setVisibility(View.GONE);
                rlProduct.setVisibility(View.GONE);
                // Courier show
                rcvCourier.setVisibility(View.VISIBLE);
                rlCourier.setVisibility(View.VISIBLE);
                // Delivery hide
                rcvDelivery.setVisibility(View.GONE);
                rlDelivery.setVisibility(View.GONE);

            }
        }));

    }

    private void initRecyclerItems(){
        //Recyclerview db
        dbCourier = new ArrayList<>();
        dbDelivery = new ArrayList<>();
        dbProduct = new ArrayList<>();
    }

    private void initProductsRecyclerView(){
        recyclerViewProduct = findViewById(R.id.rcv_manager_product);
        RecyclerViewManagerProduct adapter = new RecyclerViewManagerProduct(this, dbProduct);
        recyclerViewProduct.setAdapter(adapter);
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initDeliveryRecyclerView(){
        recyclerViewDelivery = findViewById(R.id.rcv_manager_delivery);
        RecyclerViewManagerDelivery adapter = new RecyclerViewManagerDelivery(this, dbDelivery);
        recyclerViewDelivery.setAdapter(adapter);
        recyclerViewDelivery.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initCourierRecyclerView(){
        recyclerViewCourier = findViewById(R.id.rcv_manager_courier);
        RecyclerViewManagerCourier adapter = new RecyclerViewManagerCourier(this, dbCourier);
        recyclerViewCourier.setAdapter(adapter);
        recyclerViewCourier.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
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
        mDatabaseProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ShowProductsData(dataSnapshot);
                initProductsRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(ManagerActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mDatabaseDelivery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ShowDeliveryData(dataSnapshot);
                initDeliveryRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(ManagerActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        mDatabaseCourier.addValueEventListener(valueEventListenerCourier);
        Query queryCourier = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("storage_id")
                .equalTo(userID);

        queryCourier.addListenerForSingleValueEvent(valueEventListenerCourier);
    }

    ValueEventListener valueEventListenerCourier = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            ShowCourierData(dataSnapshot);
            initCourierRecyclerView();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Failed to read value
            Toast.makeText(ManagerActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Failed to read value.", error.toException());
        }
    };

    private void ShowProductsData(DataSnapshot dataSnapshot) {
        //for (DataSnapshot ds: dataSnapshot.getChildren()){
        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        if(dataSnapshot.exists() && dataSnapshot.child(userID).exists()){
            for (DataSnapshot ds: dataSnapshot.child(userID).getChildren()){
                Product product = ds.getValue(Product.class);
                dbProduct.add(product);
            }
        }
        //}
    }

    private void ShowDeliveryData(DataSnapshot dataSnapshot) {
        //for (DataSnapshot ds: dataSnapshot.getChildren()){
        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        if(dataSnapshot.exists() && dataSnapshot.child(userID).exists()){
            for (DataSnapshot ds: dataSnapshot.child(userID).getChildren()){
                Delivery delivery = ds.getValue(Delivery.class);
                dbDelivery.add(delivery);
            }
        }
        //}
    }

    private void ShowCourierData(DataSnapshot dataSnapshot) {
        //for (DataSnapshot ds: dataSnapshot.getChildren()){
        dbCourier.clear();
        if(dataSnapshot.exists()){
            for (DataSnapshot ds: dataSnapshot.getChildren()){
                Courier courier = ds.getValue(Courier.class);
                dbCourier.add(courier);
            }
        }
        //}
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
                Toast.makeText(ManagerActivity.this, "Home Press!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                startActivity(new Intent(ManagerActivity.this, ProfileActivity.class));
                break;
            case R.id.setting:
                startActivity(new Intent(ManagerActivity.this, SettingsActivity.class));
                break;
            case R.id.logout:
                signOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }
}
