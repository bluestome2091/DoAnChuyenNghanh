package vn.stu.edu.doancn.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.adapter.AdapterAccountAdmin;
import vn.stu.edu.doancn.model.Users;

public class AdminManegerAccountActivity2 extends AppCompatActivity {
    private DatabaseReference AdminRef;
    private ListView lvAdmin;
    private FloatingActionButton btnThem;
    private ArrayList<Users> dsAdmin;
    private RecyclerView recyclerViewAccountAdmin;
    private AdapterAccountAdmin adapter;
    private FloatingActionButton fabThem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maneger_account2);
        addControls();
        addEvents();
    }
    private void addEvents() {
        fabThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminManegerAccountActivity2.this, CreateAccountAdminActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        btnThem = findViewById(R.id.fabThemAccount);
        AdminRef = FirebaseDatabase.getInstance().getReference().child("Admins");
        lvAdmin = findViewById(R.id.lvAccoutAdmin);
        fabThem = findViewById(R.id.fabThemAccount);
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
                Toast.makeText(AdminManegerAccountActivity2.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}