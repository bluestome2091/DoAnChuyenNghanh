package vn.stu.edu.doancn.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

//import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.model.Products;

public class ProductDetailsActivity extends AppCompatActivity {

//    private FloatingActionButton add_product_to_cart;
    private ImageView product_image_details;
    private ElegantNumberButton number_btn;
    private TextView txtProductname_details, txtDescription_details, txtPrice_details;
    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        addControls();
        addEvents();
        getProductDetails(productID);
    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);

                    txtProductname_details.setText(products.getName());
                    txtPrice_details.setText(products.getPrice());
                    txtDescription_details.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(product_image_details);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvents() {
    }

    private void addControls() {
//        add_product_to_cart=findViewById(R.id.add_product_to_cart);
        product_image_details=findViewById(R.id.product_image_details);
        number_btn=findViewById(R.id.number_btn);
        txtProductname_details=findViewById(R.id.txtProductname_details);
        txtPrice_details=findViewById(R.id.txtPrice_details);
        txtDescription_details=findViewById(R.id.txtDescription_details);
        productID = getIntent().getStringExtra("pid");
    }
}