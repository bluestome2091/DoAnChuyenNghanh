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
import vn.stu.edu.doancn.ViewHolder.CartViewHolder;
import vn.stu.edu.doancn.adapter.AdapterProductAdmin;
import vn.stu.edu.doancn.model.Cart;

public class AdminShowUserProductsActivity extends AppCompatActivity {

    private RecyclerView show_product;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private ImageButton btnShowProductsExit;
    private String userID="";
    String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_user_products);

        userID=getIntent().getStringExtra("id");
        user = getIntent().getStringExtra("user");
        cartListRef= FirebaseDatabase.getInstance().getReference().child("CartList").child("AdminsView")
                .child(user).child("Products");

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnShowProductsExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminShowUserProductsActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addControls() {
        show_product=findViewById(R.id.show_product);
        show_product.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        show_product.setLayoutManager(layoutManager);
        btnShowProductsExit=findViewById(R.id.btnShowProductsExit);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef, Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                cartViewHolder.txtcart_product_name.setText(cart.getName());
                cartViewHolder.txtcart_product_price.setText("Price: " + cart.getPrice() + " VND");
                cartViewHolder.txtcart_product_quatity.setText("Quantity: " + cart.getQuatity());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);

                return holder;
            }
        };
        show_product.setAdapter(adapter);
        adapter.startListening();
    }
}