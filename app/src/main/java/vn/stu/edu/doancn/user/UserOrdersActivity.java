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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                userOrderViewHolder.username_order.setText(usersOrders.getName());
                userOrderViewHolder.nguoinhan_order.setText(usersOrders.getName());
                userOrderViewHolder.phonenumber_order.setText(usersOrders.getPhone());
                userOrderViewHolder.totalprice_order.setText(usersOrders.getTotalPrice());
                userOrderViewHolder.address_city_order.setText(usersOrders.getAddress() + " "+ usersOrders.getCity());
                userOrderViewHolder.datetime_order.setText(usersOrders.getDate() + " " + usersOrders.getName());
                userOrderViewHolder.trangthai_order.setText(usersOrders.getState());

                userOrderViewHolder.btnDeleteOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference uorderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
                        uorderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                    if(snapshot1.getValue().equals() = Prevalent.currentOnlineUser.getUsers()){

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_orders, parent, false);
                return new UserOrderViewHolder(view);
            }
        };
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