package com.ariel.logi.logi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ariel.DeliverySystem.Delivery;
import com.ariel.Storage.Product;
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

    private ArrayList<User> dbCourier;
    private ArrayList<Product> dbProduct;
    private ArrayList<Delivery> dbDelivery;

    private Button btnProduct ,btnPending, btnCourier, btnInProcess, btnDelivered;
    private  RecyclerView rcwProduct ,rcwPending, rcwCourier, rcwInProcess, rcwDelivered;
    private TextView noProduct ,noPending, noCourier, noInProcess, noDelivered;
    private TextView icProduct, icDelivery, icCourier;

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

        icCourier = (TextView) findViewById(R.id.ic_courier);
        icDelivery = (TextView) findViewById(R.id.ic_delivery);
        icProduct = (TextView) findViewById(R.id.ic_product);

        noProduct = (TextView) findViewById(R.id.no_products);
        noPending = (TextView) findViewById(R.id.no_panding_approval);
        noCourier = (TextView) findViewById(R.id.no_Courier);
        noInProcess = (TextView) findViewById(R.id.no_in_process);
        noDelivered = (TextView) findViewById(R.id.no_delivered);

        noProduct.setVisibility(View.GONE);
        noPending.setVisibility(View.GONE);
        noCourier.setVisibility(View.GONE);
        noInProcess.setVisibility(View.GONE);
        noDelivered.setVisibility(View.GONE);

        btnProduct = (Button) findViewById(R.id.btn_products);
        btnPending = (Button) findViewById(R.id.btn_pending_approval);
        btnCourier = (Button) findViewById(R.id.btn_courier);
        btnInProcess = (Button) findViewById(R.id.btn_in_process);
        btnDelivered = (Button) findViewById(R.id.btn_delivered);

        //recyclerView init
        rcwProduct = (RecyclerView) findViewById(R.id.recycler_view_manager_products);
        rcwPending = (RecyclerView) findViewById(R.id.recycler_view_manager_panding);
        rcwCourier = (RecyclerView) findViewById(R.id.recycler_view_manager_courier);
        rcwInProcess = (RecyclerView) findViewById(R.id.recycler_view_manager_process);
        rcwDelivered = (RecyclerView) findViewById(R.id.recycler_view_manager_delivered);

        rcwProduct.setVisibility(View.GONE);
        rcwPending.setVisibility(View.GONE);
        rcwCourier.setVisibility(View.GONE);
        rcwInProcess.setVisibility(View.GONE);
        rcwDelivered.setVisibility(View.GONE);

        // float button
        FabSpeedDial fabAdd = (FabSpeedDial) findViewById(R.id.float_add_manager);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get firebase database instance
        mDatabaseDelivery = FirebaseDatabase.getInstance().getReference("deliveries");
        mDatabaseProduct = FirebaseDatabase.getInstance().getReference("products");
        mDatabaseCourier =  FirebaseDatabase.getInstance().getReference("users");

        //
        initRecyclerItems();
        initProductsRecyclerView();

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
                if(!btnProduct.isActivated()){
                    rcwProduct.setVisibility(View.VISIBLE);
                    if(dbProduct.isEmpty())
                        noProduct.setVisibility(View.VISIBLE);
                }else {
                    rcwProduct.setVisibility(View.GONE);
                    rcwPending.setVisibility(View.GONE);
                    rcwCourier.setVisibility(View.GONE);
                    rcwInProcess.setVisibility(View.GONE);
                    rcwDelivered.setVisibility(View.GONE);
                }
                btnProduct.setActivated(!btnProduct.isActivated());
                if(btnCourier.isActivated()) btnCourier.setActivated(false);
                if(btnDelivered.isActivated()) btnDelivered.setActivated(false);
                if(btnInProcess.isActivated()) btnInProcess.setActivated(false);
                if(btnPending.isActivated()) btnPending.setActivated(false);
                noPending.setVisibility(View.GONE);
                noCourier.setVisibility(View.GONE);
                noInProcess.setVisibility(View.GONE);
                noDelivered.setVisibility(View.GONE);
            }
        }));

        btnProduct.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btnProduct.isActivated()){
                    rcwProduct.setVisibility(View.VISIBLE);
                    if(dbProduct.isEmpty())
                        noProduct.setVisibility(View.VISIBLE);
                }else {
                    rcwProduct.setVisibility(View.GONE);
                    rcwPending.setVisibility(View.GONE);
                    rcwCourier.setVisibility(View.GONE);
                    rcwInProcess.setVisibility(View.GONE);
                    rcwDelivered.setVisibility(View.GONE);
                }
                btnProduct.setActivated(!btnProduct.isActivated());
                if(btnCourier.isActivated()) btnCourier.setActivated(false);
                if(btnDelivered.isActivated()) btnDelivered.setActivated(false);
                if(btnInProcess.isActivated()) btnInProcess.setActivated(false);
                if(btnPending.isActivated()) btnPending.setActivated(false);
                noPending.setVisibility(View.GONE);
                noCourier.setVisibility(View.GONE);
                noInProcess.setVisibility(View.GONE);
                noDelivered.setVisibility(View.GONE);
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
        RecyclerView recyclerView = findViewById(R.id.recycler_view_manager_products);
        RecyclerViewManagerProduct adapter = new RecyclerViewManagerProduct(this, dbProduct);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    }

    private void ShowProductsData(DataSnapshot dataSnapshot) {
        //for (DataSnapshot ds: dataSnapshot.getChildren()){
        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        if(dataSnapshot.exists()){
            for (DataSnapshot ds: dataSnapshot.child(userID).getChildren()){
                Product product = ds.getValue(Product.class);
                dbProduct.add(product);
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
