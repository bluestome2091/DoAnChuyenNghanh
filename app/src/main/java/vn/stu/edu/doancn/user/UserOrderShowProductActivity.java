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
import vn.stu.edu.doancn.ViewHolder.AdminHistoryProductViewHolder;
import vn.stu.edu.doancn.ViewHolder.UserShowProductViewHolder;
import vn.stu.edu.doancn.admin.AdminHistoryOrdersActivity;
import vn.stu.edu.doancn.admin.AdminShowProductHistoryActivity;
import vn.stu.edu.doancn.model.Products;
import vn.stu.edu.doancn.model.UserOrderHistory;
import vn.stu.edu.doancn.model.UserShowProductHistory;

public class UserOrderShowProductActivity extends AppCompatActivity {

    private RecyclerView show_user_product_history;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference uershowproductRef;
    private ImageButton btnShowUserProductsHistoryExit;
    private String uID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_show_product);

        uID = getIntent().getStringExtra("oID");
        uershowproductRef= FirebaseDatabase.getInstance().getReference().child("HistoryProduct").child(uID);

        addControls();
        addEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<UserShowProductHistory> options = new FirebaseRecyclerOptions.Builder<UserShowProductHistory>()
                .setQuery(uershowproductRef, UserShowProductHistory.class).build();
        FirebaseRecyclerAdapter<UserShowProductHistory, UserShowProductViewHolder> adapter = new FirebaseRecyclerAdapter<UserShowProductHistory, UserShowProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserShowProductViewHolder userShowProductViewHolder, int i, @NonNull UserShowProductHistory userShowProductHistory) {
                userShowProductViewHolder.txtuser_history_productname.setText(userShowProductHistory.getName());
                userShowProductViewHolder.txtuser_history_product_quatity.setText(userShowProductHistory.getQuatity());
                userShowProductViewHolder.txtuser_history_product_price.setText(userShowProductHistory.getPrice() + " VND");
            }

            @NonNull
            @Override
            public UserShowProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_product_history_item, parent, false);
                UserShowProductViewHolder holder = new UserShowProductViewHolder(view);
                return holder;
            }
        };
        show_user_product_history.setAdapter(adapter);
        adapter.startListening();
    }

    private void addEvents() {
        btnShowUserProductsHistoryExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserOrderShowProductActivity.this, UserOrderHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        show_user_product_history=findViewById(R.id.show_user_product_history);
        show_user_product_history.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        show_user_product_history.setLayoutManager(layoutManager);
        btnShowUserProductsHistoryExit=findViewById(R.id.btnShowUserProductsHistoryExit);
    }
}