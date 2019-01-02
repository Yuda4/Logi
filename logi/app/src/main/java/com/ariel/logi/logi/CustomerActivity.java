package com.ariel.logi.logi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "CustomerActivity";

    private CircleImageView profileImg;
    private ArrayList<Delivery> aDelivery,aDeliveryPend;
    private DatabaseReference mDatabaseDeliveries;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener authListener;
    public static FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Button btnshowDel, btndateDel;
    private NavigationView navigationView;
    private RecyclerView DelsRecyclerView, DateRecyclerView;
    private RecyclerViewAdapter recShow;
    private RecyclerViewDate recSet;
    private DatePickerDialog datePicker;
    private Calendar calendar;
    private Uri imageUri;
    private TextView nameTextView, emailTextView;
    private static final int IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custmer);

        auth = FirebaseAuth.getInstance();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutItem);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_Setting);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        profileImg = (CircleImageView) findViewById(R.id.profile_image);
        nameTextView = (TextView) findViewById(R.id.name_textView);
        btnshowDel = (Button) findViewById(R.id.show_button);
        btndateDel = (Button) findViewById(R.id.date_button);
        //btnSetDate = (ImageButton) findViewById(R.id.recycler_setD_img);

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        nameTextView.setText(user.getDisplayName());
        nameTextView.setText(user.getEmail());
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
        aDelivery = new ArrayList<>();
        aDeliveryPend = new ArrayList<>();
        //initItems();
        initRecyclerView();
        //initDates();
        initRecyclerViewDates();
        mDatabaseDeliveries = FirebaseDatabase.getInstance().getReference("deliveries");
        String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        // Read from the database
        mDatabaseDeliveries.addValueEventListener(valueEventListenerDelivery);
        Query queryPend = mDatabaseDeliveries;

        queryPend.addListenerForSingleValueEvent(valueEventListenerDelivery);

        DelsRecyclerView.setVisibility(View.GONE);
        DateRecyclerView.setVisibility(View.GONE);

        btnshowDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btnshowDel.isActivated()){
                    DelsRecyclerView.setVisibility(View.VISIBLE);
                    DateRecyclerView.setVisibility(View.GONE);
                }else{
                    DelsRecyclerView.setVisibility(View.GONE);
                }

                btnshowDel.setActivated(!btnshowDel.isActivated());
                if(btndateDel.isActivated()) btndateDel.setActivated(false);

            }
        });

        btndateDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btndateDel.isActivated()){
                    DateRecyclerView.setVisibility(View.VISIBLE);
                    DelsRecyclerView.setVisibility(View.GONE);

                }else{
                    DateRecyclerView.setVisibility(View.GONE);
                }
                btndateDel.setActivated(!btndateDel.isActivated());
                if(btnshowDel.isActivated()) btnshowDel.setActivated(false);
            }
        });
    }

    private void initItems(){

    }

    private void initRecyclerView(){
        DelsRecyclerView = (RecyclerView) findViewById(R.id.recveiwDels);
        recShow = new RecyclerViewAdapter(this, aDelivery);
        DelsRecyclerView.setAdapter(recShow);
        DelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initRecyclerViewDates(){
        DateRecyclerView = (RecyclerView) findViewById(R.id.recveiwDates);
        recSet = new RecyclerViewDate(this, aDeliveryPend);
        DateRecyclerView.setAdapter(recSet);
        DateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    ValueEventListener valueEventListenerDelivery = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            ShowData(dataSnapshot);
            ShowDataPend(dataSnapshot);
            recShow.notifyDataSetChanged();
            recSet.notifyDataSetChanged();        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Failed to read value
            Toast.makeText(CustomerActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Failed to read value.", error.toException());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
    }

    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();
        }
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
                Toast.makeText(CustomerActivity.this, "Home Press!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                startActivity(new Intent(CustomerActivity.this, ProfileActivity.class));
                break;
            case R.id.setting:
                startActivity(new Intent(CustomerActivity.this, SettingsActivity.class));
                break;
            case R.id.logout:
                signOut();
                finish();
                break;
        }
        return false;
    }

    private void ShowData(DataSnapshot dataSnapshot) {
        aDelivery.clear();
        if(dataSnapshot.exists()){
            for (DataSnapshot ds: dataSnapshot.getChildren()){
                for(DataSnapshot dsnp: ds.getChildren()){
                    Delivery delivery = dsnp.getValue(Delivery.class);
                    if(!delivery.getStatus().equals("pending")&&delivery.getCustomer_email().equals(auth.getCurrentUser().getEmail()))
                        aDelivery.add(delivery);
                }
            }
        }
    }

    private void ShowDataPend(DataSnapshot dataSnapshot) {
        aDeliveryPend.clear();
        if(dataSnapshot.exists()){
            for (DataSnapshot ds: dataSnapshot.getChildren()){
                for(DataSnapshot dsnp: ds.getChildren()){
                    Delivery delivery = dsnp.getValue(Delivery.class);
                    if(delivery.getStatus().equals("pending")&&delivery.getCustomer_email().equals(auth.getCurrentUser().getEmail()))
                        aDeliveryPend.add(delivery);
                }
            }
        }
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }
}
