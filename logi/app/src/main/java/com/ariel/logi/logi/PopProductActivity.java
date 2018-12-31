package com.ariel.logi.logi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.ariel.Storage.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PopProductActivity extends Activity {

    Button btnNewProduct;
    private EditText inputPrdId, inputPrdDesc, inputPrdName;
    String storage_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_product);

        Intent intentStorageId = getIntent();
        storage_id = intentStorageId.getStringExtra("storage_id");

        btnNewProduct = (Button) findViewById(R.id.btn_createNewProduct);

        inputPrdId = (EditText) findViewById(R.id.inputProductId);
        inputPrdDesc = (EditText) findViewById(R.id.inputProductDesc);
        inputPrdName = (EditText) findViewById(R.id.inputProductName);

        btnNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = new Product(inputPrdId.getText().toString(), inputPrdName.getText().toString()
                        , inputPrdDesc.getText().toString());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("products")
                        .child(storage_id );
                ref.child(product.getProduct_id()).setValue(product);
                finish();
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.9), (int)(height*.4));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

    }
}
