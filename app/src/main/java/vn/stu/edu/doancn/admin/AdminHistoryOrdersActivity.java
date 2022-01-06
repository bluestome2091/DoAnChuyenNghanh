package vn.stu.edu.doancn.admin;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.ViewHolder.AdminHistoryViewHolder;
import vn.stu.edu.doancn.model.AdminHistoryOrder;

public class AdminHistoryOrdersActivity extends AppCompatActivity {

    private RecyclerView show_History;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference historyListRef;
    private ImageButton btnShowHistoryExit;
    private String hID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_history_orders);

        hID=getIntent().getStringExtra("id");
        historyListRef = FirebaseDatabase.getInstance().getReference().child("HistoryOrder");

        addControls();
        addEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminHistoryOrder> options = new FirebaseRecyclerOptions.Builder<AdminHistoryOrder>()
                .setQuery(historyListRef, AdminHistoryOrder.class).build();

       FirebaseRecyclerAdapter<AdminHistoryOrder, AdminHistoryViewHolder> adapter =new FirebaseRecyclerAdapter<AdminHistoryOrder, AdminHistoryViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull AdminHistoryViewHolder adminHistoryViewHolder, int i, @NonNull AdminHistoryOrder adminHistoryOrder) {
               adminHistoryViewHolder.history_username.setText(adminHistoryOrder.getName());
               adminHistoryViewHolder.history_nguoinhan.setText(adminHistoryOrder.getName());
               adminHistoryViewHolder.history_phonenumber.setText(adminHistoryOrder.getPhone());
               adminHistoryViewHolder.history_totalprice.setText(adminHistoryOrder.getGia());
               adminHistoryViewHolder.history_address_city.setText(adminHistoryOrder.getAddress() + " - " + adminHistoryOrder.getCity());
               adminHistoryViewHolder.history_datetime.setText(adminHistoryOrder.getDate() + " - " + adminHistoryOrder.getTime());
               adminHistoryViewHolder.history_trangthai.setText(adminHistoryOrder.getState());

               adminHistoryViewHolder.btnshow_all_products_history.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent = new Intent(AdminHistoryOrdersActivity.this, AdminShowProductHistoryActivity.class);
                       intent.putExtra("id", adminHistoryOrder.getDate() + " " + adminHistoryOrder.getTime());
                       startActivity(intent);
                   }
               });
           }

           @NonNull
           @Override
           public AdminHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history, parent, false);
               return new AdminHistoryViewHolder(view);
           }
       };
       show_History.setAdapter(adapter);
       adapter.startListening();
    }

    private void addEvent() {
        btnShowHistoryExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHistoryOrdersActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addControls() {
        show_History=findViewById(R.id.show_History);
        show_History.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(this);
        show_History.setLayoutManager(layoutManager);
        btnShowHistoryExit=findViewById(R.id.btnShowHistoryExit);
    }
}