package com.ariel.logi.logi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class ManagerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    FabSpeedDial fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutManager);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_manager);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        // float button
        fabAdd = (FabSpeedDial) findViewById(R.id.float_add_manager);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        //get firebase database instance
        mDatabase = FirebaseDatabase.getInstance().getReference();

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

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
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
                finish();
                break;
            case R.id.setting:
                startActivity(new Intent(ManagerActivity.this, SettingsActivity.class));
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


}
