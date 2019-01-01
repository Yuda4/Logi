package com.ariel.logi.logi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ariel.Storage.Product;
import com.ariel.User.Courier;
import com.ariel.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PopCourierActivity extends AppCompatActivity {

    Button btnNewCourier;
    private EditText inputCrEmail;
    String storage_id;
    String crEmail;
    private FirebaseAuth managerAuth;
    private DatabaseReference ref;
    private Courier courier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_courier);

        Intent intentStorageId = getIntent();
        storage_id = intentStorageId.getStringExtra("storage_id");

        btnNewCourier = (Button) findViewById(R.id.btn_createNewCourier);

        inputCrEmail = (EditText) findViewById(R.id.inputEmail);

        managerAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("users");

        courier = new Courier();

        btnNewCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crEmail = inputCrEmail.getText().toString();
                if (TextUtils.isEmpty(crEmail.trim())) {
                    Toast.makeText(getApplicationContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Query changeStorageId = ref.orderByChild("email").equalTo(crEmail);
                changeStorageId.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        courier = dataSnapshot.getValue(Courier.class);
                        if (dataSnapshot.exists())
                            for(DataSnapshot ds: dataSnapshot.getChildren()) {
                                ref.child(Objects.requireNonNull(ds.getKey())).child("storage_id").setValue(storage_id);
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                finish();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.9), (int)(height*.2));

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
