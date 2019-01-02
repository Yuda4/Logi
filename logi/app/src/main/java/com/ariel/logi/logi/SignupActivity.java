package com.ariel.logi.logi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    String type;
    private EditText inputEmail, inputPassword, inputName, inputPhoneNumber;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        //get firebase database instance
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputName = (EditText) findViewById(R.id.name);
        inputPhoneNumber = (EditText) findViewById(R.id.phone_number);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        final AlphaAnimation buttonClicked = new AlphaAnimation(0.2F, 0.8F);

        final RadioGroup rg = (RadioGroup) findViewById(R.id.type_radio_group);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClicked);
                RadioButton selectedButton = (RadioButton) findViewById(rg.getCheckedRadioButtonId());

                type = selectedButton.getText().toString();
                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                final String name = inputName.getText().toString().trim();
                final String phoneNumber = inputPhoneNumber.getText().toString().trim();

                if (TextUtils.isEmpty(email.trim())) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter your name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phoneNumber.length() != 10 ) {
                    Toast.makeText(getApplicationContext(), "Invalid phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 10 || !isOnlyNumbers(phoneNumber)) {
                    Toast.makeText(getApplicationContext(), "Invalid phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // Write to database
                                    String userUid = auth.getCurrentUser().getUid();
                                    mDatabase.child("users").child(userUid).child("email").setValue(email);
                                    mDatabase.child("users").child(userUid).child("name").setValue(name);
                                    mDatabase.child("users").child(userUid).child("phone").setValue(phoneNumber);
                                    if(type.toLowerCase().equals("null")){
                                        type = "customer";
                                    }else {
                                        mDatabase.child("users").child(userUid).child("type").setValue(type.toLowerCase());
                                    }
                                    mDatabase.child("users").child(userUid).child("address").setValue("Please fill");
                                    mDatabase.child("users").child(userUid).child("city").setValue("Please fill");
                                    mDatabase.child("users").child(userUid).child("country").setValue("Please fill");
                                    mDatabase.child("users").child(userUid).child("zip_code").setValue(1);
                                    if(type.equalsIgnoreCase("courier"))
                                        mDatabase.child("users").child(userUid).child("storage_id").setValue("0");
                                    mDatabase.child("users").child(userUid).child("image_uri").setValue("default");

                                    Log.i(TAG, "Add data to database succeded!");
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public static boolean isOnlyNumbers(String str){
        try{
            Integer.parseInt(str);
        }catch(NumberFormatException exception){
            return false;
        }
        return true;
    }
}
