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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "ProfileActivity";

    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private  NavigationView navigationView;
    private TextView nameTextView, emailTextView;

    private ArrayList<String> mLable;
    private ArrayList<String> mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutProfile);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_profile);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        nameTextView = (TextView) findViewById(R.id.name_textView);
        emailTextView = (TextView) findViewById(R.id.email_textView);


        mContent = new ArrayList<String>();
        mLable = new ArrayList<String>();
        initRecyclerItems();

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get firebase database instance
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    private void initRecyclerItems(){
        mLable.add("Name:");
        mContent.add("Please update your name");
        mLable.add("Email:");
        mContent.add("Please update your email");
        mLable.add("Phone:");
        mContent.add("Please update your phone");
        mLable.add("Type:");
        mContent.add("Customer");
        mLable.add("Country");
        mContent.add("Please update your country");
        mLable.add("City:");
        mContent.add("Please update your city");
        mLable.add("Street:");
        mContent.add("Please update your street");
        mLable.add("Zip Code:");
        mContent.add("Please update your zip code");

    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view_profile);
        RecyclerViewProfile adapter = new RecyclerViewProfile(this, mLable, mContent);
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
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ShowData(dataSnapshot);
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(ProfileActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Failed to read value.", error.toException());
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
        User user = new User();
        //for (DataSnapshot ds: dataSnapshot.getChildren()){
        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        user = dataSnapshot.child(userID).getValue(User.class);
        if (user != null) {
            user.setUserId(userID);
            mContent.set(0, user.getName());
            mContent.set(1, user.getEmail());
            mContent.set(2, user.getPhone());
            mContent.set(3, user.getType());
            mContent.set(4, user.getCountry());
            mContent.set(5, user.getCity());
            mContent.set(6, user.getAddress());
            mContent.set(7, Long.toString(user.getZip_code()));

            emailTextView.setText(user.getEmail());
            nameTextView.setText(user.getName());
        }
        //}
        if (user != null)
            Toast.makeText(ProfileActivity.this, "Your email is " + user.getEmail(), Toast.LENGTH_SHORT ).show();

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
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                // Toast.makeText(ProfileActivity.this, "Home Press!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                Toast.makeText(ProfileActivity.this, "Profile Press!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
                break;
            case R.id.logout:
                signOut();
                break;
        }
        return false;
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }
}
