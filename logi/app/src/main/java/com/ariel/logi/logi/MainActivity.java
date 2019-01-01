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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        //get firebase database instance
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");

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

        initFCM();

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
        if(auth.getCurrentUser() != null){
            String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            if(dataSnapshot.exists()){
                user = dataSnapshot.child(userID).getValue(User.class);
                if (user != null) {
                    user.setUserId(userID);
                    switch (user.getType().toLowerCase()){
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
        }
    }


    private void sendRegistrationToServer(String token) {
        Log.d("MainActivity", "sendRegistrationToServer: sending token to server: " + token);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace('.', '_'))
                .child("messaging_token")
                .setValue(token);
    }

    private void initFCM(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("MainActivity", "initFCM: token: " + token);
        sendRegistrationToServer(token);

    }
}
