package vn.stu.edu.doancn.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.stu.edu.doancn.R;

public class AdminShowUserProductsActivity extends AppCompatActivity {

    private RecyclerView show_product;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String userID="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_user_products);

        userID=getIntent().getStringExtra("id");
        cartListRef= FirebaseDatabase.getInstance().getReference().child("CartList").child("AdminsView").child(userID);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<>
    }
}