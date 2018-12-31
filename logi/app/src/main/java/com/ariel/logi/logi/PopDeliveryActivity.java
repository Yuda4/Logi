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

import com.ariel.Storage.Product;
import com.ariel.User.Courier;

import java.util.ArrayList;

public class PopDeliveryActivity extends AppCompatActivity {

    Button btnNewDelivery;
    private EditText inputCstmName,inputCstmPhone, inputCstmAddress, inputCstmCity, inputDlivId;
    private Spinner spinnerProducts, spinnerCouriers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_delivery);

        ArrayList<String> productsName = getIntent().getStringArrayListExtra("productName");
        ArrayList<String> couriersName = getIntent().getStringArrayListExtra("courierName");


        spinnerProducts = (Spinner) findViewById(R.id.spinner_products);
        ArrayAdapter<String> adapterProductsSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, productsName);
        adapterProductsSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducts.setAdapter(adapterProductsSpinner);

        spinnerCouriers = (Spinner) findViewById(R.id.spinner_couriers);
        ArrayAdapter<String> adapterCouriersSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, couriersName);
        adapterCouriersSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCouriers.setAdapter(adapterCouriersSpinner);


        btnNewDelivery = (Button) findViewById(R.id.btn_createNewDelivery);

        inputCstmName = (EditText) findViewById(R.id.inputCustomerName);
        inputCstmPhone = (EditText) findViewById(R.id.inputCustomerPhone);
        inputCstmAddress = (EditText) findViewById(R.id.inputCustomerAddress);
        inputCstmCity = (EditText) findViewById(R.id.inputCustomerCity);
        inputDlivId = (EditText) findViewById(R.id.inputDeliveryId);

        btnNewDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
