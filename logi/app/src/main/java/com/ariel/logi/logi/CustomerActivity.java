package com.ariel.logi.logi;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ariel.DeliverySystem.Delivery;
import com.ariel.User.Customer;
import com.ariel.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CustomerActivity extends AppCompatActivity implements OnItemSelectedListener {
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private Button btnOut;
    private ListView dlvrsShow;
    private Spinner spinProc, spinDate;
    private Date textDate;
    private User usr;
    private Delivery dlvr;
    private ArrayAdapter<CharSequence> adpTmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custmer);

        btnOut = (Button) findViewById(R.id.btn_signout);
        spinProc = (Spinner) findViewById(R.id.spin_proc);
        spinDate = (Spinner) findViewById(R.id.spin_date);
        dlvrsShow = (ListView) findViewById(R.id.dlvrs_show);
        adpTmp = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        auth = FirebaseAuth.getInstance();

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(CustomerActivity.this, LoginActivity.class));
            }
        });


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
                    startActivity(new Intent(CustomerActivity.this, LoginActivity.class));
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
                usr = ShowData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(CustomerActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //adding the proc_spinner the relevant deliveries
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //should give the adapter the 1st delivery of the customer
        adapter.add(usr.getDelivryId[0]);
        //should give the adapter the 2nd delivery of the customer and so on
        adapter.add(usr.getDelivryId[1]);
        spinProc.setAdapter(adapter);
        spinProc.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //showing details about delivery
                adpTmp.clear();
                Delivery del = new Delivery(usr.getDelivryId[0]);
                adpTmp.add(del.getDetails());
                dlvrsShow.set.setAdapter(adpTmp);
                dlvrsShow.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dlvrsShow.setVisibility(View.GONE);
            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDate.setAdapter(adapter2);
        spinDate.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView)spinProc.getSelectedView();
                String text = textView.getText().toString();
                Intent Intent = new Intent(CustomerActivity.this, DatingActivity.class);
                intent.putExtra(text, sessionId);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private User ShowData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds: dataSnapshot.getChildren()){
            String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            User user = new User();
            user.setEmail(ds.child(userID).getValue(User.class).getEmail());
            return user;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
