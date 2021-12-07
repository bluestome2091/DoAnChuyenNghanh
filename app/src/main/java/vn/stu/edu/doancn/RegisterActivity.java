package vn.stu.edu.doancn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText edtUser, edtPassword, edtConfirmPassword, edtPhuoneNumber, edtName;
    Button btnConfirm;
    ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        create();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String user = edtUser.getText().toString();
        String phone = edtPhuoneNumber.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmpassword = edtConfirmPassword.getText().toString();
        String name = edtName.getText().toString();
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(RegisterActivity.this, "please write your useraccount", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(RegisterActivity.this, "please write your phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "please write your password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmpassword)) {
            Toast.makeText(RegisterActivity.this, "please confirm your password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(RegisterActivity.this, "Check your name", Toast.LENGTH_SHORT).show();
        } else if (!password.equalsIgnoreCase(confirmpassword)) {
            Toast.makeText(RegisterActivity.this, "Confirm password not correct", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking!!!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidatephoneNumber(user, password, phone, name);
        }


    }

    private void ValidatephoneNumber(String user, String password, String phone, String name) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(user).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Users", user);
                    userdataMap.put("Password", password);
                    userdataMap.put("Phonenumber", phone);
                    userdataMap.put("Name", name);
                    RootRef.child("Users").child(user).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Your Account is created", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } else {
                    Toast.makeText(RegisterActivity.this, "This user is exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please use another User", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void create() {
        edtConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        edtPassword = (EditText) findViewById(R.id.editTextPassword2);
        edtPhuoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        edtUser = (EditText) findViewById(R.id.editTextUser);
        edtName = (EditText) findViewById(R.id.editTextName);
        btnConfirm = (Button) findViewById(R.id.buttonConfirm);
        loadingBar = new ProgressDialog(this);
    }

}