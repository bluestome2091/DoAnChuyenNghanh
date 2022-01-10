package vn.stu.edu.doancn.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.admin.AdminNewOrdersActivity;
import vn.stu.edu.doancn.admin.AdminShowUserProductsActivity;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private TextInputEditText shippment_name, shippment_phone_number, shippment_address, shippment_city;
    private TextInputLayout lbproduct_name, lbproduct_phone, lbproduct_address, lbproduct_city;
    private ImageButton btnconfirm_final_orderExit;
    private Button btnconfirm_final_order;
    private String totalPrice = "";
    private TextView txtTT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalPrice = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Tổng tiền: " + totalPrice + " VND", Toast.LENGTH_SHORT).show();

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

        btnconfirm_final_orderExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmFinalOrderActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void Check() {
        if(TextUtils.isEmpty(shippment_name.getText().toString())){
            Toast.makeText(this, "Vui lòng nhập tên.", Toast.LENGTH_SHORT).show();
            lbproduct_name.setError("Chưa nhập tên");
        } else if(TextUtils.isEmpty(shippment_phone_number.getText().toString())){
            Toast.makeText(this, "Vui lòng nhập số điện thoại.", Toast.LENGTH_SHORT).show();
            lbproduct_phone.setError("Chưa nhập số điện thoại");
            lbproduct_name.setErrorEnabled(false);
        } else if(TextUtils.isEmpty(shippment_address.getText().toString())){
            Toast.makeText(this, "Vui lòng nhập địa chỉ nhà.", Toast.LENGTH_SHORT).show();
            lbproduct_address.setError("Chưa nhập địa chỉ nhà");
            lbproduct_name.setErrorEnabled(false);
            lbproduct_phone.setErrorEnabled(false);
        } else if(TextUtils.isEmpty(shippment_city.getText().toString())){
            Toast.makeText(this, "Vui lòng nhập tên thành phố.", Toast.LENGTH_SHORT).show();
            lbproduct_city.setError("Chưa nhập thành phố");
            lbproduct_name.setErrorEnabled(false);
            lbproduct_phone.setErrorEnabled(false);
            lbproduct_address.setErrorEnabled(false);
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
        final  DatabaseReference checkOrdersRef = FirebaseDatabase.getInstance().getReference().child("CardList").child("AdminsView")
                .child(Prevalent.currentOnlineUser.getUsers()).child("Products");
        checkOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Đơn hàng trước của bạn đang chờ xử lý, vui lòng đợi", Toast.LENGTH_SHORT).show();
                }
                else {
                    final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                            .child(saveCurrentDate + " " + saveCurrentTime);

                    final HashMap<String, Object> ordersMap =  new HashMap<>();
                    ordersMap.put("totalPrice", totalPrice);
                    ordersMap.put("name", shippment_name.getText().toString());
                    ordersMap.put("phone", shippment_phone_number.getText().toString());
                    ordersMap.put("address", shippment_address.getText().toString());
                    ordersMap.put("city", shippment_city.getText().toString());
                    ordersMap.put("date", saveCurrentDate);
                    ordersMap.put("time", saveCurrentTime);
                    ordersMap.put("state", "Đang xử lý");
                    ordersMap.put("id",Prevalent.currentOnlineUser.getUsers());

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
                                                    Toast.makeText(ConfirmFinalOrderActivity.this, "Đơn hàng của bạn đã được đặt thành công.", Toast.LENGTH_SHORT).show();

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addControls() {
        lbproduct_name=findViewById(R.id.lbproduct_name) ;
        lbproduct_phone=findViewById(R.id.lbproduct_phone);
        lbproduct_address=findViewById(R.id.lbproduct_address);
        lbproduct_city=findViewById(R.id.lbproduct_city);

        shippment_name=findViewById(R.id.shippment_name);
        shippment_phone_number=findViewById(R.id.shippment_phone_number);
        shippment_address=findViewById(R.id.shippment_address);
        shippment_city=findViewById(R.id.shippment_city);
        btnconfirm_final_order=findViewById(R.id.btnconfirm_final_order);
        btnconfirm_final_orderExit=findViewById(R.id.btnconfirm_final_orderExit);
        txtTT=findViewById(R.id.txtTT);

        txtTT.setText(totalPrice + " VND");
    }
}