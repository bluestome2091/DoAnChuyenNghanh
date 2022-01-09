package vn.stu.edu.doancn.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import vn.stu.edu.doancn.ViewHolder.AdminOrdersViewHolder;
import vn.stu.edu.doancn.ViewHolder.UserOrderViewHolder;
import vn.stu.edu.doancn.admin.AdminCategoryActivity;
import vn.stu.edu.doancn.admin.AdminNewOrdersActivity;
import vn.stu.edu.doancn.model.AdminOrders;
import vn.stu.edu.doancn.model.UsersOrders;

public class UserOrdersActivity extends AppCompatActivity {
    private RecyclerView show_orders;
    private DatabaseReference userorderRef;
    ImageButton btnShowOrdersExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        userorderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        addControls();
        addEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<UsersOrders> options = new FirebaseRecyclerOptions.Builder<UsersOrders>().setQuery(userorderRef, UsersOrders.class)
                .build();

        FirebaseRecyclerAdapter<UsersOrders, UserOrderViewHolder> adapter = new FirebaseRecyclerAdapter<UsersOrders, UserOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserOrderViewHolder userOrderViewHolder, int i, @NonNull UsersOrders usersOrders) {
                if (usersOrders.getId().equals(Prevalent.currentOnlineUser.getUsers())) {
                    userOrderViewHolder.username_order.setText(usersOrders.getName());
                    userOrderViewHolder.nguoinhan_order.setText(usersOrders.getName());
                    userOrderViewHolder.phonenumber_order.setText(usersOrders.getPhone());
                    userOrderViewHolder.totalprice_order.setText(usersOrders.getTotalPrice() + " VND");
                    userOrderViewHolder.address_city_order.setText(usersOrders.getAddress() + " " + usersOrders.getCity());
                    userOrderViewHolder.datetime_order.setText(usersOrders.getDate() + " " + usersOrders.getTime());
                    userOrderViewHolder.trangthai_order.setText(usersOrders.getState());

                    if (usersOrders.getState().equals("Đang giao hàng")) {
                        userOrderViewHolder.btnDeleteOrder.setText("Đã nhận");
                    } else {
                        userOrderViewHolder.btnDeleteOrder.setText("Hủy đơn hàng");
                    }
                    userOrderViewHolder.btnDeleteOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (usersOrders.getState().equals("Đang giao hàng")) {
                                Toast.makeText(UserOrdersActivity.this, "Đã nhận hàng", Toast.LENGTH_SHORT).show();
                                DatabaseReference historyorder = FirebaseDatabase.getInstance().getReference().child("HistoryOrder").child(usersOrders.getDate() + " " + usersOrders.getTime());
                                HashMap<String, Object> historyorderMap = new HashMap<>();
                                historyorderMap.put("address", usersOrders.getAddress());
                                historyorderMap.put("city", usersOrders.getCity());
                                historyorderMap.put("date", usersOrders.getDate());
                                historyorderMap.put("id", usersOrders.getId());
                                historyorderMap.put("name", usersOrders.getName());
                                historyorderMap.put("phone", usersOrders.getPhone());
                                historyorderMap.put("state", "Đã nhận hàng");
                                historyorderMap.put("time", usersOrders.getTime());
                                historyorderMap.put("totalPrice", usersOrders.getTotalPrice());


                                historyorder.updateChildren(historyorderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseDatabase.getInstance().getReference().child("Ordres").child(usersOrders.getDate() + " " + usersOrders.getTime())
                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        DatabaseReference removeruserorderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                                                                .child(usersOrders.getDate() + " " + usersOrders.getTime());
                                                        removeruserorderRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(UserOrdersActivity.this, "Đã lưu vào lịch sử đơn hàng", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        Intent intent = new Intent(UserOrdersActivity.this, UserOrderHistoryActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                                DatabaseReference historyuserorderProduct = FirebaseDatabase.getInstance().getReference().child("CartList").child("AdminsView")
                                        .child(Prevalent.currentOnlineUser.getUsers()).child("Products");
                                historyuserorderProduct.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String userID = getRef(i).getKey();
                                        DatabaseReference historyProductRef = FirebaseDatabase.getInstance().getReference().child("HistoryProduct");
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                            HashMap<String, Object> historyuserorderProductMap = new HashMap<>();
                                            historyuserorderProductMap.put("name", snapshot1.child("name").getValue());
                                            historyuserorderProductMap.put("quatity", snapshot1.child("quatity").getValue());
                                            historyuserorderProductMap.put("price", snapshot1.child("price").getValue());
                                            historyuserorderProductMap.put("discount", snapshot1.child("discount").getValue());
                                            historyuserorderProductMap.put("pid", snapshot1.child("pid").getValue());
                                            historyuserorderProductMap.put("time", snapshot1.child("time").getValue());
                                            historyuserorderProductMap.put("user", snapshot1.child("user").getValue());

                                            historyProductRef.child(usersOrders.getDate() + " " + usersOrders.getTime()).child("Products").child(usersOrders.getDate() + " " + snapshot1.child("time").getValue())
                                            .updateChildren(historyuserorderProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        FirebaseDatabase.getInstance().getReference().child("CartList").child("AdminsView").child(Prevalent.currentOnlineUser.getUsers())
                                                                .child("Products").removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){

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


                            } else if (usersOrders.getState().equals("Đang xử lý")) {
                                String userorderID = getRef(i).getKey();
                                RemoverUserOrder(userorderID);
                                Toast.makeText(UserOrdersActivity.this, "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }

            @NonNull
            @Override
            public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_orders, parent, false);
                return new UserOrderViewHolder(view);
            }
        };
        show_orders.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoverUserOrder(String userorderID) {
        userorderRef.child(userorderID).removeValue();
    }

    private void addEvents() {
        btnShowOrdersExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserOrdersActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addControls() {
        btnShowOrdersExit = findViewById(R.id.btnShowOrdersExit);
        show_orders = findViewById(R.id.show_orders);
        show_orders.setLayoutManager(new LinearLayoutManager(this));
    }
}