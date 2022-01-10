package vn.stu.edu.doancn.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import vn.stu.edu.doancn.MainActivity;
import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.RegisterActivity;

public class CreateAccountAdminActivity extends AppCompatActivity {
    TextInputEditText user, password, name, email, sdt;
    TextInputLayout lbuser, lbpassword, lbname, lbsdt;
    MaterialToolbar title;
    Button btnThem;
    ImageButton btnExit;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_admin);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThem();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountAdminActivity.this, AdminManegerAccountActivity2.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void xuLyThem() {
        String taikhoan = user.getText().toString();
        String pass = password.getText().toString();
        String ten = name.getText().toString();
        String phone = sdt.getText().toString();
        if (!taikhoan.isEmpty()) {
        }
        if (taikhoan.equals("")) {
            lbuser.setError("Chưa điền thông tin.");
        } else if (pass.equals("")) {
            lbpassword.setError("Chưa điền thông tin.");
            lbuser.setErrorEnabled(false);
        } else if (ten.equals("")) {
            lbname.setError("Chưa điền thông tin.");
            lbuser.setErrorEnabled(false);
            lbpassword.setErrorEnabled(false);
        } else if (phone.equals("")) {
            lbsdt.setError("Chưa điền thông tin.");
            lbuser.setErrorEnabled(false);
            lbpassword.setErrorEnabled(false);
            lbname.setErrorEnabled(false);
        } else {
            loadingBar.setTitle("Đang xử lý.");
            loadingBar.setMessage("Vui lòng đợi trong giây lát!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            themAccout(taikhoan, pass, ten, phone);
        }

    }

    private void themAccout(String taikhoan, String pass, String ten, String phone) {
        DatabaseReference adminref = FirebaseDatabase.getInstance().getReference();
        adminref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Admins").child(taikhoan).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("Users", taikhoan);
                    userdataMap.put("Password", pass);
                    userdataMap.put("Phonenumber", phone);
                    userdataMap.put("Name", ten);
                    adminref.child("Admins").child(taikhoan).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(CreateAccountAdminActivity.this, "Tài khoản của bạn đã được tạo.", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(CreateAccountAdminActivity.this, AdminManegerAccountActivity2.class);
                                startActivity(intent);
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(CreateAccountAdminActivity.this, "Đã xảy ra lỗi.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(CreateAccountAdminActivity.this, "Người dùng này đang tồn tại.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(CreateAccountAdminActivity.this, "Vui lòng sử dụng người dùng khác.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateAccountAdminActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void addControls() {
        user = findViewById(R.id.txtUsernameAdmin);
        password = findViewById(R.id.txtPasswordAdmin);
        name = findViewById(R.id.txtNameAdmin);
        sdt = findViewById(R.id.txtSDTAdmin);
        btnThem = findViewById(R.id.btnThemTkAdmin);
        loadingBar = new ProgressDialog(this);

        lbuser = findViewById(R.id.lbUsernameAdmin);
        lbpassword = findViewById(R.id.lbPasswordAdmin);
        lbname = findViewById(R.id.lbNameAdmin);
        lbsdt = findViewById(R.id.lbSDTAdmin);
        btnExit = findViewById(R.id.btnTaoAdminExit);
    }
}