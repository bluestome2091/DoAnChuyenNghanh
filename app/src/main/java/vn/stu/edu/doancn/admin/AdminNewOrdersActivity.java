package vn.stu.edu.doancn.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;

import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.ViewHolder.AdminOrdersViewHolder;
import vn.stu.edu.doancn.adapter.AdapterProductAdmin;
import vn.stu.edu.doancn.model.AdminOrders;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    ImageButton btnAddNewOrdersExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        addControls();
        addEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, int i, @NonNull AdminOrders adminOrders) {
                adminOrdersViewHolder.orders_username.setText("Name: " + adminOrders.getName());
                adminOrdersViewHolder.orders_phonenumber.setText("Phone: " + adminOrders.getPhone());
                adminOrdersViewHolder.orders_totalprice.setText("Total Price: " + String.valueOf(adminOrders.getTotalPrice()) + "VND");
                adminOrdersViewHolder.orders_datetime.setText("Order at: " + adminOrders.getDate() + " " + adminOrders.getTime());
                adminOrdersViewHolder.orders_address_city.setText("Shipping Address: " + adminOrders.getAddress() + "-" + adminOrders.getCity());

                adminOrdersViewHolder.btnshow_all_products.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID = getRef(i).getKey();

                        Intent intent = new Intent(AdminNewOrdersActivity.this, AdminShowUserProductsActivity.class);
                        intent.putExtra("id", uID);
                        startActivity(intent);
                    }
                });

                adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence sequence[] = new CharSequence[]{
                                "Yes", "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Have you shipped this order  products ?");
                        builder.setItems(sequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    DatabaseReference AdminViewHistory = FirebaseDatabase.getInstance().getReference().child("CartList").child("AdminsView")
                                            .child(getRef(i).getKey().toString()).child("Products");
                                    AdminViewHistory.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            DatabaseReference historyProduct = FirebaseDatabase.getInstance().getReference();
                                            for (DataSnapshot n : snapshot.getChildren()) {
                                                HashMap<String, Object> showproduct = new HashMap<>();
                                                showproduct.put("date", n.child("date").getValue());
                                                showproduct.put("time", n.child("time").getValue());
                                                showproduct.put("name", n.child("name").getValue());
                                                showproduct.put("pid", n.child("pid").getValue());
                                                showproduct.put("quatity", n.child("quatity").getValue());
                                                showproduct.put("user", n.child("user").getValue());
                                                showproduct.put("discount", n.child("discount").getValue());

                                                historyProduct.child("HistoryProduct").child("Products").child(adminOrders.getDate() + " " + adminOrders.getTime()).child(n.getKey()).updateChildren(showproduct)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                } else {
                                                                    Toast.makeText(AdminNewOrdersActivity.this, "Kết nối bị lỗi", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference();
                                    historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            HashMap<String, Object> historyOrder = new HashMap<>();
                                            historyOrder.put("name", adminOrders.getName());
                                            historyOrder.put("phone", adminOrders.getPhone());
                                            historyOrder.put("address", adminOrders.getAddress());
                                            historyOrder.put("gia", adminOrders.getTotalPrice());
                                            historyOrder.put("date", adminOrders.getDate());
                                            historyOrder.put("time", adminOrders.getTime());
                                            historyOrder.put("city", adminOrders.getCity());
                                            historyOrder.put("state", "Đã giao");

                                            historyRef.child("HistoryOrder").child(adminOrders.getDate() + " " + adminOrders.getTime()).updateChildren(historyOrder)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                String uID = getRef(i).getKey();
                                                                RemoverOrder(uID);
                                                                Toast.makeText(AdminNewOrdersActivity.this, "Tiến hành giao thành công", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(AdminNewOrdersActivity.this, "Kết nối bị lỗi", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                } else {
                                    finish();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return new AdminOrdersViewHolder(view);
            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoverOrder(String uID) {
        ordersRef.child(uID).removeValue();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("CartList").child("AdminsView").child(uID).child("Products");
        reference.removeValue();
        Toast.makeText(AdminNewOrdersActivity.this, "Đã xử lý", Toast.LENGTH_SHORT).show();
    }

    private void addEvents() {
        btnAddNewOrdersExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addControls() {
        btnAddNewOrdersExit = findViewById(R.id.btnAddNewOrdersExit);
        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }
}