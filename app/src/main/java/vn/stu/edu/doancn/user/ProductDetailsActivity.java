package vn.stu.edu.doancn.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.Prevalent.PrevalentAdmin;
import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.model.Products;

public class ProductDetailsActivity extends AppCompatActivity {

    Button btnadd_to_cart;
    ImageButton btnDetalsProductExit;
    ImageView product_image_details;
    ElegantNumberButton number_btn;
    TextView txtProductname_details, txtDescription_details, txtPrice_details;
    Products tamp;
    String state = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent intent = getIntent();
        tamp = (Products) intent.getSerializableExtra("id");
        String productID = null;
        if(tamp == null ){
            productID = intent.getStringExtra("pid");
        }else{
            productID = tamp.getId();
        }

        addControls(productID);
        addEvents(productID);
        getProductDetails(productID);
    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);
                    txtProductname_details.setText(products.getName());
                    txtPrice_details.setText(products.getPrice());
                    txtDescription_details.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(product_image_details);
                    product_image_details.setClipToOutline(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvents(String productID) {
        btnadd_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("Order Placed") || state.equals("Order Shipped")){
                    Toast.makeText(ProductDetailsActivity.this, "Bạn có thể thêm mua nhiều sản phẩm hơn sau khi đơn hàng của bạn được xác nhận giao hàng.", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CardList").child("AdminsView").child(Prevalent.currentOnlineUser.getUsers());
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (state.equals("false"))
                            {
                                Toast.makeText(ProductDetailsActivity.this, "Đơn hàng trước của bạn đang chờ xử lý, vui lòng đợi.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                addToCartList(productID);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        btnDetalsProductExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();
    }

    private void addToCartList(String productID) {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("name", txtProductname_details.getText().toString());
        cartMap.put("price", txtPrice_details.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quatity", number_btn.getNumber());
        cartMap.put("discount", "");
        cartMap.put("user", Prevalent.currentOnlineUser.getUsers());

        cartListRef.child("Users").child(Prevalent.currentOnlineUser.getUsers())
                .child("Products").child(saveCurrentDate + " " +saveCurrentTime).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    cartListRef.child("AdminsView").child(Prevalent.currentOnlineUser.getUsers())
                            .child("Products").child(saveCurrentDate + " " +saveCurrentTime).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProductDetailsActivity.this, "Thêm thành công.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

    }

    private void CheckOrderState() {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot n : snapshot.getChildren()){
                   if (n.child("id").getValue().equals(Prevalent.currentOnlineUser.getUsers())){
                       state = "false";
                       break;
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addControls(String productID) {
        product_image_details = findViewById(R.id.product_image_details);
        number_btn = findViewById(R.id.number_btn);
        txtProductname_details = findViewById(R.id.txtProductname_details);
        txtPrice_details = findViewById(R.id.txtPrice_details);
        txtDescription_details = findViewById(R.id.txtDescription_details);
        productID = getIntent().getStringExtra("pid");
        btnadd_to_cart = findViewById(R.id.btnadd_to_cart);
        btnDetalsProductExit = findViewById(R.id.btnDetalsProductExit);
    }
}