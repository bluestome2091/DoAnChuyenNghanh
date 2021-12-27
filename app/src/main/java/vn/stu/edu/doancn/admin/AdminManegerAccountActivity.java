package vn.stu.edu.doancn.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.ViewHolder.AdminAccountViewHolder;
import vn.stu.edu.doancn.ViewHolder.ProductViewHolder;
import vn.stu.edu.doancn.adapter.AdapterAccountAdmin;
import vn.stu.edu.doancn.model.Products;
import vn.stu.edu.doancn.model.Users;

public class AdminManegerAccountActivity extends AppCompatActivity {
    private DatabaseReference AdminRef;
    private ListView lvAdmin;
    private FloatingActionButton btnThem;
    private ArrayList<Users> dsAdmin;
    private RecyclerView recyclerViewAccountAdmin;
    private AdapterAccountAdmin adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addControls();
    }

    private void addControls() {
        setContentView(R.layout.activity_admin_maneger_account);
        btnThem = findViewById(R.id.fabThem);
        AdminRef = FirebaseDatabase.getInstance().getReference().child("Admins");
        lvAdmin = findViewById(R.id.lvAccoutAdmin);
        dsAdmin = new ArrayList<>();
        adapter = new AdapterAccountAdmin(this, R.layout.viewadminaccount, dsAdmin);
        lvAdmin.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AdminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsAdmin.clear();
                for(DataSnapshot n : snapshot.getChildren()){
                    String user = n.child("Users").getValue().toString();
                    String name = n.child("Name").getValue().toString();
                    String password = n.child("Password").getValue().toString();
                    String sdt = n.child("Phonenumber").getValue().toString();
                    dsAdmin.add(new Users(user, password, sdt, name,null, null));
                };
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}