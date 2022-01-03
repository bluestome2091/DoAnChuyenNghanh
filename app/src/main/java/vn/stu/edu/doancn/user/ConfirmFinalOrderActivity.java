package vn.stu.edu.doancn.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.R;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText shippment_name, shippment_phone_number, shippment_address, shippment_city;
    private Button btnconfirm_final_order;
    private String totalPrice = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalPrice = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price = $" + totalPrice, Toast.LENGTH_SHORT).show();

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnconfirm_final_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });


    }

    private void Check() {
        if(TextUtils.isEmpty(shippment_name.getText().toString())){
            Toast.makeText(this, "Please provide your full name", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(shippment_phone_number.getText().toString())){
            Toast.makeText(this, "Please provide your phone number", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(shippment_address.getText().toString())){
            Toast.makeText(this, "Please provide your address", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(shippment_city.getText().toString())){
            Toast.makeText(this, "Please provide your city name", Toast.LENGTH_SHORT).show();
        } else{
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getUsers());

        final HashMap<String, Object> ordersMap =  new HashMap<>();
        ordersMap.put("totalPrice", totalPrice);
        ordersMap.put("name", shippment_name.getText().toString());
        ordersMap.put("phone", shippment_phone_number.getText().toString());
        ordersMap.put("address", shippment_address.getText().toString());
        ordersMap.put("city", shippment_city.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");

        orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("CartList").child("Users")
                            .child(Prevalent.currentOnlineUser.getUsers()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Your final order has been placed successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void addControls() {
        shippment_name=findViewById(R.id.shippment_name);
        shippment_phone_number=findViewById(R.id.shippment_phone_number);
        shippment_address=findViewById(R.id.shippment_address);
        shippment_city=findViewById(R.id.shippment_city);
        btnconfirm_final_order=findViewById(R.id.btnconfirm_final_order);
    }
}