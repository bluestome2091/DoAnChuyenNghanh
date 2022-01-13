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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.ViewHolder.AdminOrdersViewHolder;
import vn.stu.edu.doancn.ViewHolder.OrderHistoryProductViewHolder;
import vn.stu.edu.doancn.adapter.AdapterOrderUserHistory;
import vn.stu.edu.doancn.model.UserOrderHistory;
import vn.stu.edu.doancn.model.UsersOrders;

public class UserOrderHistoryActivity extends AppCompatActivity {

    private RecyclerView show_orders_history;
    private DatabaseReference orderhistoryRef;
    ImageButton btnOrderHistoryExit;
    ArrayList<UserOrderHistory> dsHoaDonLichsu;
    AdapterOrderUserHistory adapter;
    ListView lv;

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
        orderhistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsHoaDonLichsu.clear();
                for (DataSnapshot n : snapshot.getChildren()){
                    String id = n.child("id").getValue().toString();
                    if (id.equals(Prevalent.currentOnlineUser.getUsers())) {
                        String address = n.child("address").getValue().toString();
                        String name = n.child("name").getValue().toString();
                        String phone = n.child("phone").getValue().toString();
                        String date = n.child("date").getValue().toString();
                        String city = n.child("city").getValue().toString();
                        String state = n.child("state").getValue().toString();
                        String time = n.child("time").getValue().toString();
                        String totalPrice = n.child("totalPrice").getValue().toString();
                        dsHoaDonLichsu.add(new UserOrderHistory(address, city, date, name, phone, state, time, totalPrice, id));

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        lv = findViewById(R.id.show_orders_history);
        dsHoaDonLichsu = new ArrayList<>();
        adapter=new AdapterOrderUserHistory(this, R.layout.orders_history_item, dsHoaDonLichsu);
        lv.setAdapter(adapter);
    }
}