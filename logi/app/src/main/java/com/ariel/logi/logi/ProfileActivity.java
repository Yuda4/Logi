package com.ariel.logi.logi;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
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
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ariel.User.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "ProfileActivity";
    private static final int IMAGE_REQUEST = 1;

    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private StorageReference storageRef;
    private StorageTask uploadTask;

    private Uri imageUri;
    private CircleImageView profileImg;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private  NavigationView navigationView;
    private TextView nameTextView, emailTextView;

    private ArrayList<String> mLable;
    private ArrayList<String> mContent;
    private User user;

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
        profileImg = (CircleImageView) findViewById(R.id.profile_image);

        user = new User();

        mContent = new ArrayList<String>();
        mLable = new ArrayList<String>();
        initRecyclerItems();

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get firebase database instance
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");

        // get firebase storage instance
        storageRef = FirebaseStorage.getInstance().getReference("uploads");

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fuser = firebaseAuth.getCurrentUser();
                if (fuser == null) {
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

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = ProfileActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        if(imageUri != null){
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + '.' + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("image_uri", mUri);
                        mDatabaseUsers.updateChildren(map);

                        progressDialog.dismiss();
                    }else{
                        Toast.makeText(ProfileActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }else{
            Toast.makeText(ProfileActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(ProfileActivity.this,"Upload is in progress", Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }
        }
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
        if(dataSnapshot.exists() && dataSnapshot.child(userID).exists()){
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
                mContent.set(7, user.getZip_code().toString());

                emailTextView.setText(user.getEmail());
                nameTextView.setText(user.getName());

                if(user.getImage_uri().equalsIgnoreCase("default")){
                    profileImg.setImageResource(R.drawable.user_blank_512);
                }else{
                    Glide.with(ProfileActivity.this).load(user.getImage_uri()).into(profileImg);
                }
                Toast.makeText(ProfileActivity.this, "Wellcome " + user.getName(), Toast.LENGTH_SHORT ).show();
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
