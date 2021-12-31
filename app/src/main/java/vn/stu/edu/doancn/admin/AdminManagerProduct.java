package vn.stu.edu.doancn.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.adapter.AdapterProductAdmin;
import vn.stu.edu.doancn.model.Products;

public class AdminManagerProduct extends AppCompatActivity {
    private DatabaseReference adminref;
    private ListView lv;
    private ImageButton btnExit;
    private ArrayList<Products> dssp;
    private AdapterProductAdmin adapter;
    private FloatingActionButton fabThem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager_product);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminManagerProduct.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fabThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminManagerProduct.this, AdminAddProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        adminref = FirebaseDatabase.getInstance().getReference().child("Products");
        lv = findViewById(R.id.lvManagerProduct);
        btnExit = findViewById(R.id.btnManagerProductExit);
        dssp =  new ArrayList<>();
        adapter = new AdapterProductAdmin(this, R.layout.viewmanagerproduct,dssp);
        lv.setAdapter(adapter);
        fabThem = findViewById(R.id.fabThemProduct);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adminref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dssp.clear();
                for (DataSnapshot n : snapshot.getChildren()){
                    String ma = (String) n.getKey();
                    String count = n.child("count").getValue().toString();
                    String name = n.child("name").getValue().toString();
                    String price = n.child("price").getValue().toString();
                    String date = n.child("date").getValue().toString();
                    String image = n.child("image").getValue().toString();
                    String description = n.child("description").getValue().toString();
                    String pid = n.child("pid").getValue().toString();
                    String time = n.child("time").getValue().toString();
                    String size = n.child("size").getValue().toString();
                    dssp.add(new Products(ma, count, name, null, price, date, image, description, pid, time, size));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}