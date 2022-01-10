package vn.stu.edu.doancn.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.ViewHolder.CartViewHolder;
import vn.stu.edu.doancn.admin.AdminCategoryActivity;
import vn.stu.edu.doancn.admin.AdminNewOrdersActivity;
import vn.stu.edu.doancn.model.Cart;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnnext_process;
    private ImageButton btnTotalPriceExit;
    private TextView txttotal_price, txtmsg1;
    private int overTotalPrice = 0;
    private String saveID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        addControls();
        addEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("Users")
                .child(Prevalent.currentOnlineUser.getUsers()).child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                cartViewHolder.txtcart_product_quatity.setText(cart.getQuatity());
                cartViewHolder.txtcart_product_price.setText(cart.getPrice() + " VND");
                cartViewHolder.txtcart_product_name.setText(cart.getName());

                double oneTypeProductPrice = ((Double.parseDouble(cart.getPrice()))) * Double.parseDouble(cart.getQuatity());
                overTotalPrice = (int) (overTotalPrice + oneTypeProductPrice);

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Thay đổi", "Xóa sản phẩm"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Tùy chọn");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", cart.getPid());
                                    startActivity(intent);
                                }
                                if (which == 1) {
                                    cartListRef.child("Users").child(Prevalent.currentOnlineUser.getUsers()).child("Products")
                                            .child(cart.getDate() + " " + cart.getTime()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CartActivity.this, "Xóa thành công.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });

                                    cartListRef.child("AdminsView").child(Prevalent.currentOnlineUser.getUsers()).child("Products")
                                            .child(cart.getDate() + " " + cart.getTime()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CartActivity.this, "Đã xóa.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void CheckOrderState() {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getUsers());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String shpippingState = snapshot.child("state").getValue().toString();
                    String userName = snapshot.child("name").getValue().toString();

                    if (shpippingState.equals("shipped")) {
                        txttotal_price.setText("Xin chào " + userName + "\n đơn hàng được chuyển thành công.");
                        recyclerView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        txtmsg1.setText("Đang hàng đang được vận chuyển. Bạn sẽ sớm nhận được hàng.");
                        btnnext_process.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "Bạn có thể mua sản phẩm khi nhận được hàng của đơn đặt hàng trước.", Toast.LENGTH_SHORT).show();
                    } else if (shpippingState.equals("not shipped")) {
                        txttotal_price.setText("Shipped State = Not Shipped");
                        recyclerView.setVisibility(View.GONE);
                        txtmsg1.setVisibility(View.VISIBLE);
                        btnnext_process.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "Bạn có thể mua sản phẩm khi nhận được hàng của đơn đặt hàng trước.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvents() {
        btnnext_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                txttotal_price.setText("Total Price: " + String.valueOf(overTotalPrice) + " VND");
                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });

        btnTotalPriceExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addControls() {
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        btnnext_process = findViewById(R.id.btnnext_process);
        txttotal_price = findViewById(R.id.txttotal_price);
        txtmsg1 = findViewById(R.id.txtmsg1);
        btnTotalPriceExit=findViewById(R.id.btnTotalPriceExit);

    }
}