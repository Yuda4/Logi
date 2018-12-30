package com.ariel.logi.logi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ariel.Storage.Product;
import com.ariel.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PopCourierActivity extends AppCompatActivity {

    Button btnNewCourier;
    private EditText inputCrName, inputCrEmail, inputCrPhone,inputCrPassword;
    String storage_id;
    String crName, crEmail, crPhone, crPassword;
    private FirebaseAuth managerAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_courier);

        Intent intentStorageId = getIntent();
        storage_id = intentStorageId.getStringExtra("storage_id");

        btnNewCourier = (Button) findViewById(R.id.btn_createNewCourier);

        inputCrName = (EditText) findViewById(R.id.inputName);
        inputCrEmail = (EditText) findViewById(R.id.inputEmail);
        inputCrPassword = (EditText) findViewById(R.id.inputPassword);
        inputCrPhone = (EditText) findViewById(R.id.inputPhone);

        managerAuth = FirebaseAuth.getInstance();

        btnNewCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                crName = inputCrName.getText().toString();
                crEmail = inputCrEmail.getText().toString();
                crPhone = inputCrPhone.getText().toString();
                crPassword = inputCrPassword.getText().toString();

                User user = new User(crName, crEmail, crPhone);
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                        .child(storage_id );

                if (TextUtils.isEmpty(crEmail)) {
                    Toast.makeText(getApplicationContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(crName)) {
                    Toast.makeText(getApplicationContext(), "Invalid name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(crPhone) || crPhone.length() != 10 || !isOnlyNumbers(crPhone)) {
                    Toast.makeText(getApplicationContext(), "Invalid phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (inputCrPassword.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                managerAuth.createUserWithEmailAndPassword(crEmail, crPassword)
                        .addOnCompleteListener(PopCourierActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(PopCourierActivity.this, "createUserWithEmail:onComplete:"
                                        + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(PopCourierActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // Write to database

                                    String courierUid = managerAuth.getUid();
                                    ref.child(courierUid).child("email").setValue(crEmail);
                                    ref.child(courierUid).child("name").setValue(crName);
                                    ref.child(courierUid).child("phone").setValue(crPhone);
                                    ref.child(courierUid).child("type").setValue("courier");
                                    ref.child(courierUid).child("address").setValue("Please fill");
                                    ref.child(courierUid).child("city").setValue("Please fill");
                                    ref.child(courierUid).child("country").setValue("Please fill");
                                    ref.child(courierUid).child("zip_code").setValue(0);
                                    ref.child(courierUid).child("storage_id").setValue(storage_id);
                                    managerAuth.signOut();

                                    startActivity(new Intent(PopCourierActivity.this, MainActivity.class));
                                }
                            }
                        });


                //ref.child(user.getProduct_id()).setValue(product);
                finish();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.9), (int)(height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

    }


    public static boolean isOnlyNumbers(String str){
        try{
            Integer.parseInt(str);
        }catch(NumberFormatException exception){
            return false;
        }
        return true;
    }

    public void signOut() {
        managerAuth.signOut();
    }
}
