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
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.ViewHolder.AdminOrdersViewHolder;
import vn.stu.edu.doancn.ViewHolder.OrderHistoryProductViewHolder;
import vn.stu.edu.doancn.model.UserOrderHistory;

public class UserOrderHistoryActivity extends AppCompatActivity {

    private RecyclerView show_orders_history;
    private DatabaseReference orderhistoryRef;
    ImageButton btnOrderHistoryExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_history);

        orderhistoryRef = FirebaseDatabase.getInstance().getReference().child("HistoryOrder");

        addControls();
        addEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<UserOrderHistory>().setQuery(orderhistoryRef, UserOrderHistory.class).build();
        FirebaseRecyclerAdapter<UserOrderHistory, OrderHistoryProductViewHolder> adapter = new FirebaseRecyclerAdapter<UserOrderHistory, OrderHistoryProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderHistoryProductViewHolder orderHistoryProductViewHolder, int i, @NonNull UserOrderHistory userOrderHistory) {
                if(userOrderHistory.getId().equals(Prevalent.currentOnlineUser.getUsers())){
                    orderHistoryProductViewHolder.order_history_username.setText(userOrderHistory.getName());
                    orderHistoryProductViewHolder.order_history_address_city.setText(userOrderHistory.getAddress() + " " + userOrderHistory.getCity());
                    orderHistoryProductViewHolder.order_history_datetime.setText(userOrderHistory.getDate() + " " + userOrderHistory.getTime());
                    orderHistoryProductViewHolder.order_history_nguoinhan.setText(userOrderHistory.getName());
                    orderHistoryProductViewHolder.order_history_totalprice.setText(userOrderHistory.getTotalPrice() + " VND");
                    orderHistoryProductViewHolder.order_history_trangthai.setText(userOrderHistory.getState());
                    orderHistoryProductViewHolder.order_history_phonenumber.setText(userOrderHistory.getPhone());


                    orderHistoryProductViewHolder.btnshow_all_order_products_history.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String oID = getRef(i).getKey();
                            Intent intent = new Intent(UserOrderHistoryActivity.this, UserOrderShowProductActivity.class);
                            intent.putExtra("oID", oID);
                            startActivity(intent);
                        }
                    });

                }
            }

            @NonNull
            @Override
            public OrderHistoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_history_item, parent, false);
                return new OrderHistoryProductViewHolder(view);
            }
        };
        show_orders_history.setAdapter(adapter);
        adapter.startListening();
    }

    private void addEvents() {
        btnOrderHistoryExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserOrderHistoryActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addControls() {
        btnOrderHistoryExit=findViewById(R.id.btnOrderHistoryExit);
        show_orders_history=findViewById(R.id.show_orders_history);
        show_orders_history.setLayoutManager(new LinearLayoutManager(this));
    }
}