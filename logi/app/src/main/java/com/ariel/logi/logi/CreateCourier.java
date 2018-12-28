package com.ariel.logi.logi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ariel.User.Manager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CreateCourier extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth, courierAuth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    private EditText inputEmail, inputName, inputPhoneNumber;
    private Button registerCourier;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_courier);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        courierAuth = FirebaseAuth.getInstance();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutCreateCourier);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_create_courier);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        registerCourier = (Button) findViewById(R.id.registerCourierBttn);
        //input values of manager
        inputEmail = (EditText) findViewById(R.id.email);
        inputName = (EditText) findViewById(R.id.name);
        inputPhoneNumber = (EditText) findViewById(R.id.phone_number);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        final AlphaAnimation buttonClicked = new AlphaAnimation(0.2F, 0.8F);

        registerCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClicked); // button animation pressed
                final String email = inputEmail.getText().toString().trim();
                final String name = inputName.getText().toString().trim();
                final String phoneNumber = inputPhoneNumber.getText().toString().trim();
                final String password = "123456"; //Defualt Password - a user can change it

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter your name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(getApplicationContext(), "Enter phone!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phoneNumber.length() != 10 ) {
                    Toast.makeText(getApplicationContext(), "Invalid phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);
                final Intent i = new Intent(CreateCourier.this, MainActivity.class);
                //create user

                courierAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateCourier.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(CreateCourier.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(CreateCourier.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write to database

                            String courierUid = courierAuth.getUid();
                            mDatabase.child(courierUid).child("email").setValue(email);
                            mDatabase.child(courierUid).child("name").setValue(name);
                            mDatabase.child(courierUid).child("phone").setValue(phoneNumber);
                            mDatabase.child(courierUid).child("type").setValue("courier");
                            mDatabase.child(courierUid).child("address").setValue("Please fill");
                            mDatabase.child(courierUid).child("city").setValue("Please fill");
                            mDatabase.child(courierUid).child("country").setValue("Please fill");
                            mDatabase.child(courierUid).child("zip_code").setValue(0);
                            courierAuth.signOut();

                            //startActivity(new Intent(CreateCourier.this, ManagerActivity.class));


                        }
                    }
                });

    }
});

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.home:
                Toast.makeText(CreateCourier.this, "Home Press!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                startActivity(new Intent(CreateCourier.this, ProfileActivity.class));
                finish();
                break;
            case R.id.setting:
                startActivity(new Intent(CreateCourier.this, SettingsActivity.class));
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

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

}
