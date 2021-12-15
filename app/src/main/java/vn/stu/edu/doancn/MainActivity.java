package vn.stu.edu.doancn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.model.Users;
import vn.stu.edu.doancn.user.HomeActivity;

public class MainActivity extends AppCompatActivity {
    Button joinNowButton, loginButton;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        create();
        Paper.init(this);
        final String userAccount = Paper.book().read(Prevalent.UserKey);
        final String password = Paper.book().read(Prevalent.UserPassword);
        if (userAccount != "" && password != "") {
            if(!TextUtils.isEmpty(userAccount) && !TextUtils.isEmpty(password))
            {
               AllowAccess(userAccount, password);
                loadingBar.setTitle("Create Account");
                loadingBar.setMessage("Please wait, while we are checking!!!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    private void AllowAccess(String user, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(user).exists())
                {
                    Users   usersData = snapshot.child("Users").child(user).getValue(Users.class);
                    if(usersData.getPassword().equals(password))
                    {
                        Toast.makeText(MainActivity.this, "Login Successfully....", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Wrong Password or Account not exists", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void create() {
        joinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        loginButton = (Button) findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);
    }
}