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
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.Prevalent.PrevalentAdmin;
import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.ViewHolder.AdminOrdersViewHolder;
import vn.stu.edu.doancn.ViewHolder.UserOrderViewHolder;
import vn.stu.edu.doancn.adapter.AdapterOrderUser;
import vn.stu.edu.doancn.adapter.AdapterProductAdmin;
import vn.stu.edu.doancn.admin.AdminCategoryActivity;
import vn.stu.edu.doancn.admin.AdminNewOrdersActivity;
import vn.stu.edu.doancn.model.AdminOrders;
import vn.stu.edu.doancn.model.Products;
import vn.stu.edu.doancn.model.UsersOrders;

public class UserOrdersActivity extends AppCompatActivity {
    private RecyclerView show_orders;
    private DatabaseReference userorderRef;
    ImageButton btnShowOrdersExit;
    ArrayList <UsersOrders> dsHoaDon;
    AdapterOrderUser adapter;
    ListView lv;

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
        userorderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsHoaDon.clear();
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
                        dsHoaDon.add(new UsersOrders(address, city, date, name, phone, state, time, totalPrice, id));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        lv = findViewById(R.id.show_orders);
        dsHoaDon = new ArrayList<>();
        adapter =new AdapterOrderUser(this, R.layout.user_item_orders, dsHoaDon);
        lv.setAdapter(adapter);
    }
}