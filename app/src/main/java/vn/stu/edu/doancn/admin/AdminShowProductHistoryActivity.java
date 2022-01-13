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
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.ViewHolder.AdminHistoryProductViewHolder;
import vn.stu.edu.doancn.ViewHolder.AdminHistoryViewHolder;
import vn.stu.edu.doancn.ViewHolder.CartViewHolder;
import vn.stu.edu.doancn.model.AdminHistoryOrder;
import vn.stu.edu.doancn.model.AdminHistoryProduct;
import vn.stu.edu.doancn.model.AdminOrders;
import vn.stu.edu.doancn.model.Cart;

public class AdminShowProductHistoryActivity extends AppCompatActivity {
    private RecyclerView show_product_history;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference productListHistoryRef;
    private ImageButton btnShowProductsHistoryExit;
    private String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_product_history);

        userID=getIntent().getStringExtra("id");
        productListHistoryRef = FirebaseDatabase.getInstance().getReference().child("HistoryProduct").child(userID);
        
        addControls();
        addEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(productListHistoryRef, Cart.class).build();

        FirebaseRecyclerAdapter<Cart, AdminHistoryProductViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, AdminHistoryProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminHistoryProductViewHolder adminHistoryProductViewHolder, int i, @NonNull Cart cart) {
                adminHistoryProductViewHolder.txthistory_productname.setText(cart.getName());
                adminHistoryProductViewHolder.txthistory_product_price.setText(cart.getPrice() + " VND");
                adminHistoryProductViewHolder.txthistory_product_quatity.setText(cart.getQuatity());
            }

            @NonNull
            @Override
            public AdminHistoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_product, parent, false);
                AdminHistoryProductViewHolder holder = new AdminHistoryProductViewHolder(view);
                return holder;
            }
        };
        show_product_history.setAdapter(adapter);
        adapter.startListening();
    }

    private void addEvents() {
        btnShowProductsHistoryExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminShowProductHistoryActivity.this, AdminHistoryOrdersActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        show_product_history=findViewById(R.id.show_product_history);
        show_product_history.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(this);
        show_product_history.setLayoutManager(layoutManager);
        btnShowProductsHistoryExit=findViewById(R.id.btnShowProductsHistoryExit);
    }
}