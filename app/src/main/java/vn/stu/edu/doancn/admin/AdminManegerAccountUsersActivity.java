package vn.stu.edu.doancn.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import vn.stu.edu.doancn.adapter.AdapterAccountUser;
import vn.stu.edu.doancn.model.Users;

public class AdminManegerAccountUsersActivity extends AppCompatActivity {
    private DatabaseReference AdminRef;
    private ListView lvUser;
    private ArrayList<Users> dsUsers;
    private RecyclerView recyclerViewAccountUser;
    private AdapterAccountUser adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maneger_account_users);
        addControls();
    }
    private void addControls() {
        AdminRef = FirebaseDatabase.getInstance().getReference().child("Users");
        lvUser = findViewById(R.id.lvAccoutUser);
        dsUsers = new ArrayList<>();
        adapter = new AdapterAccountUser(this, R.layout.viewuseraccount, dsUsers);
        lvUser.setAdapter(adapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        AdminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsUsers.clear();
                for(DataSnapshot n : snapshot.getChildren()){
                    String user = n.child("Users").getValue().toString();
                    String name = n.child("Name").getValue().toString();
                    String password = n.child("Password").getValue().toString();
                    String sdt = n.child("Phonenumber").getValue().toString();
                    String dc  = n.child("address").getValue().toString();
                    dsUsers.add(new Users(user, password, sdt, name,null, dc));
                };
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminManegerAccountUsersActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}