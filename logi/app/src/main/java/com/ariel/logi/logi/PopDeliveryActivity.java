package com.ariel.logi.logi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ariel.DeliverySystem.Delivery;
import com.ariel.Storage.Product;
import com.ariel.User.Courier;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PopDeliveryActivity extends AppCompatActivity {

    Button btnNewDelivery;
    private EditText inputCstmEmail ,inputCstmPhone, inputCstmAddress, inputCstmCity, inputDlivId;
    private Spinner spinnerProducts, spinnerCouriers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_delivery);

        ArrayList<Product> dbProduct;
        ArrayList<Courier> dbCourier;

        Bundle bundle = getIntent().getExtras();
        dbProduct = bundle.getParcelableArrayList("dbProducts");
        dbCourier = bundle.getParcelableArrayList("dbCouriers");

        ArrayList<String> courierName  = new ArrayList<>();
        ArrayList<String> productName  = new ArrayList<>();

        for (Product product: dbProduct ){
            productName.add(product.getName());
        }

        for (Courier courier: dbCourier ){
            courierName.add(courier.getStorage_id());
            //Toast.makeText(this, courierName.get()., Toast.LENGTH_SHORT).show();
        }


        spinnerProducts = (Spinner) findViewById(R.id.spinner_products);
        ArrayAdapter<String> adapterProductsSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, productName);
        adapterProductsSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducts.setAdapter(adapterProductsSpinner);

        spinnerCouriers = (Spinner) findViewById(R.id.spinner_couriers);
        ArrayAdapter<String> adapterCouriersSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courierName);
        adapterCouriersSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCouriers.setAdapter(adapterCouriersSpinner);


        btnNewDelivery = (Button) findViewById(R.id.btn_createNewDelivery);

        inputCstmEmail = (EditText) findViewById(R.id.inputCustomerEmail);
        inputCstmPhone = (EditText) findViewById(R.id.inputCustomerPhone);
        inputCstmAddress = (EditText) findViewById(R.id.inputCustomerAddress);
        inputCstmCity = (EditText) findViewById(R.id.inputCustomerCity);
        inputDlivId = (EditText) findViewById(R.id.inputDeliveryId);

        btnNewDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delivery delivery = new Delivery();
                //delivery.setC
                delivery.setCustomer_email(inputCstmEmail.getText().toString());
                delivery.setCustomer_phone(inputCstmPhone.getText().toString());
                delivery.setAddress(inputCstmAddress.getText().toString());


                /*Product product = new Product(inputPrdId.getText().toString(), inputPrdName.getText().toString()
                        , inputPrdDesc.getText().toString());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("products")
                        .child(storage_id );
                ref.child(product.getProduct_id()).setValue(product);*/
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

}
