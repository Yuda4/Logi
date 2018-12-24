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

import com.ariel.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "ProfileActivity";

    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private  NavigationView navigationView;
    private TextView nameTextView, emailTextView;

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

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get firebase database instance
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

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

        // Read from the database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ShowData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(ProfileActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Failed to read value.", error.toException());
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

    private void ShowData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds: dataSnapshot.getChildren()){
            String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            User user = new User();
            user.setEmail(ds.child(userID).getValue(User.class).getEmail());
            emailTextView.setText(user.getEmail());
            Toast.makeText(ProfileActivity.this, "Your email is " + user.getEmail(), Toast.LENGTH_SHORT ).show();
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
                // startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                Toast.makeText(ProfileActivity.this, "Home Press!", Toast.LENGTH_SHORT).show();
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
