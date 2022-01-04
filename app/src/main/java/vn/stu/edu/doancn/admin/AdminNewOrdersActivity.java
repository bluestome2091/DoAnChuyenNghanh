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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                                    String uID = getRef(i).getKey();
                                    RemoverOrder(uID);
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
        btnAddNewOrdersExit=findViewById(R.id.btnAddNewOrdersExit);
        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }
}