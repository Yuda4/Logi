package com.ariel.logi.logi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ariel.User.Customer;
import com.ariel.User.Manager;
import com.ariel.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private  NavigationView navigationView;
    TextView viewName;
    TextView viewEmail;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        viewName = (TextView) findViewById(R.id.navigator_name);
        viewEmail = (TextView) findViewById(R.id.navigator_email);
        user = new User();

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        //get firebase database instance
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");

        //get current user
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fuser = firebaseAuth.getCurrentUser();
                if (fuser == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
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
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ShowData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
                Log.w("MainActivity", "Failed to read value.", error.toException());
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


    private void ShowData(DataSnapshot dataSnapshot) {
        //for (DataSnapshot ds: dataSnapshot.getChildren()){
        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        if(dataSnapshot.exists()){
            user = dataSnapshot.child(userID).getValue(User.class);
            if (user != null) {
                user.setUserId(userID);
//                getIntent();
                switch (user.getType()){
                    case "manager":
                        startActivity(new Intent(MainActivity.this, ManagerActivity.class));
                        break;
                    case "courier":
                        startActivity(new Intent(MainActivity.this, CourierActivity.class));
                        break;
                    case "customer":
                        startActivity(new Intent(MainActivity.this, CustomerActivity.class));
                        break;
                }
            }
        }

        //}
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
                Toast.makeText(MainActivity.this, "Home Press!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
                break;
            case R.id.setting:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
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
