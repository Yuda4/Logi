package com.ariel.logi.logi;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ariel.DeliverySystem.Delivery;
import com.ariel.Storage.Product;
import com.ariel.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class PopDeliveryActivity extends AppCompatActivity {
    private static final String TAG = "PopDeliveryActivity";

    private FirebaseAuth auth;
    Button btnNewDelivery;
    private EditText inputCstmEmail ,inputCstmPhone, inputCstmAddress, inputCstmCity, inputDlivId;
    private Spinner spinnerProducts, spinnerCouriers;

    ArrayList<String> courierName;
    ArrayList<User> dbCourier;
    ArrayList<Product> dbProduct;

    User selectedCourier;
    Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_delivery);

        auth = FirebaseAuth.getInstance();

        spinnerProducts = (Spinner) findViewById(R.id.spinner_products);
        spinnerCouriers = (Spinner) findViewById(R.id.spinner_couriers);

        dbProduct = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dbProduct = bundle.getParcelableArrayList("dbProducts");
        }
        dbCourier = new ArrayList<>();
        courierName  = new ArrayList<>();
        ArrayList<String> productName  = new ArrayList<>();

        Query queryCourier = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("storage_id")
                .equalTo(Objects.requireNonNull(auth.getCurrentUser()).getUid());
        queryCourier.addListenerForSingleValueEvent(valueEventListenerCourier);

        if (dbProduct != null) {
            for (Product product: dbProduct ){
                productName.add(product.getName());
            }
            ArrayAdapter<String> adapterProductsSpinner = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, productName);
            adapterProductsSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProducts.setAdapter(adapterProductsSpinner);
        }

        btnNewDelivery = (Button) findViewById(R.id.btn_createNewDelivery);

        inputCstmEmail = (EditText) findViewById(R.id.inputCustomerEmail);
        inputCstmPhone = (EditText) findViewById(R.id.inputCustomerPhone);
        inputCstmAddress = (EditText) findViewById(R.id.inputCustomerAddress);
        inputCstmCity = (EditText) findViewById(R.id.inputCustomerCity);
        inputDlivId = (EditText) findViewById(R.id.inputDeliveryId);

        btnNewDelivery.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View view) {
                String courierName = spinnerCouriers.getSelectedItem().toString();
                String productName = spinnerProducts.getSelectedItem().toString();

                for(User c: dbCourier){
                    if(courierName.equalsIgnoreCase(c.getName()))
                        selectedCourier = c;
                }

                for(Product p: dbProduct){
                    if(productName.equalsIgnoreCase(p.getName()))
                        selectedProduct = p;
                }

                Delivery delivery = new Delivery();
                // customer details
                delivery.setCustomer_email(inputCstmEmail.getText().toString());
                delivery.setCustomer_phone(inputCstmPhone.getText().toString());
                delivery.setAddress(inputCstmAddress.getText().toString());
                delivery.setCity(inputCstmCity.getText().toString());
                // courier details
                delivery.setCourier_email(selectedCourier.getEmail());
                delivery.setCourier_phone(selectedCourier.getPhone());
                // products details
                delivery.setProduct_id(selectedProduct.getProduct_id());
                // delivery details
                delivery.setStatus("pending");
                delivery.setDate(getCurrentTimeStamp());
                delivery.setDelivery_id(inputDlivId.getText().toString());

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("deliveries")
                        .child(Objects.requireNonNull(auth.getCurrentUser()).getUid());
                ref.child(delivery.getDelivery_id()).setValue(delivery);
                finish();

            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.9), (int)(height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);


    }

    ValueEventListener valueEventListenerCourier = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ShowCourierData(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Failed to read value
            Toast.makeText(PopDeliveryActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Failed to read value.", error.toException());
        }
    };

    private void ShowCourierData(DataSnapshot dataSnapshot) {
        dbCourier.clear();

        if(dataSnapshot.exists()){
            for (DataSnapshot ds: dataSnapshot.getChildren()){
                User courier = ds.getValue(User.class);
                courier.setName(courier.getName());
                courier.setEmail(courier.getEmail());
                courier.setPhone(courier.getPhone());
                dbCourier.add(courier);
                courierName.add(courier.getName());
            }
        }

        ArrayAdapter<String> adapterCouriersSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courierName);
        adapterCouriersSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCouriers.setAdapter(adapterCouriersSpinner);
    }

    AdapterView.OnItemSelectedListener productSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            selectedProduct = dbProduct.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    AdapterView.OnItemSelectedListener courierSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            selectedCourier = dbCourier.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }
}
