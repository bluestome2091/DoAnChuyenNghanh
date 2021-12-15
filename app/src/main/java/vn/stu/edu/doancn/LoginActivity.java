package vn.stu.edu.doancn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.Prevalent.PrevalentAdmin;
import vn.stu.edu.doancn.admin.AdminCategoryActivity;
import vn.stu.edu.doancn.model.Users;
import vn.stu.edu.doancn.user.HomeActivity;

public class LoginActivity extends AppCompatActivity {
    EditText edtuser, edtpassword;
    Button buttonLogin;
    TextView AdminLink, NotAdminLink;
    ProgressDialog loadingBar;
    String parentDbName = "Users";
    CheckBox chkBoxRememberme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        create();
        Paper.init(this);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAccount();
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLogin.setText("Login admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";

            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLogin.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";
            }
        });
    }
    public void create()
    {
        edtuser = (EditText) findViewById(R.id.editTextTextPersonName);
        edtpassword = (EditText) findViewById(R.id.editTextTextPassword);
        buttonLogin = (Button) findViewById(R.id.btn_login);
        loadingBar = new ProgressDialog(this);
        chkBoxRememberme = (CheckBox) findViewById(R.id.chkb_remember);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
    }
    private void LoginAccount() {
        String user = edtuser.getText().toString();
        String password = edtpassword.getText().toString();
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(LoginActivity.this, "please write your useraccount", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "please write your password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking!!!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            AllowAccessToAccount(user, password);
        }



    }

    private void AllowAccessToAccount(String user, String password) {
        if(chkBoxRememberme.isChecked())
        {
            Paper.book().write(Prevalent.UserKey, user);
            Paper.book().write(Prevalent.UserPassword, password);
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(user).exists())
                {
                    Users  usersData = snapshot.child(parentDbName).child(user).getValue(Users.class);
                    if(usersData.getPassword().equals(password))
                    {
                        if (parentDbName.equals("Admins"))
                        {
                            Toast.makeText(LoginActivity.this, "Login Successfully....", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                            PrevalentAdmin.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                        else if(parentDbName.equals("Users"))
                        {
                            Toast.makeText(LoginActivity.this, "Login Successfully....", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Wrong Password or Account not exists", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Wrong Password or Account not exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}