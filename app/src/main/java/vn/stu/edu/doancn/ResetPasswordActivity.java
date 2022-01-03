package vn.stu.edu.doancn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import vn.stu.edu.doancn.model.Users;

public class ResetPasswordActivity extends AppCompatActivity {
    TextInputLayout lbUser, lbPass, lbConfirm, lbPhone;
    TextInputEditText User, Pass, Confirm, Phone;
    TextView ht;
    Button btnXacNhan;
    ImageButton btnExit;
    String xacnhan = "";
    DatabaseReference userref;
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = User.getText().toString();
                String phone = Phone.getText().toString();
                if(xacnhan.equals("")) {
                    loadingBar.setTitle("Đang xử lý");
                    loadingBar.setMessage("Please wait, while we are checking!!!");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    xulyquanmk(user, phone);
                    ht.setText("Điền mật khẩu mới");
                }else{

                    String mk =Pass.getText().toString();
                    String cmk= Confirm.getText().toString();
                    if (mk.equals(cmk)){
                        loadingBar.setTitle("Đang cập nhật mật khẩu");
                        loadingBar.setMessage("Please wait, while we are checking!!!");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        xulycapnhatmk(mk, user);
                    }
                    else {
                        lbConfirm.setError("Mật khẩu không trùng");
                        Toast.makeText(ResetPasswordActivity.this, "Sai số điện thoại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void xulycapnhatmk(String mk, String user) {
        userref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("Password", mk);
        userref.child(user).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(ResetPasswordActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void xulyquanmk(String user, String phone) {
        userref = FirebaseDatabase.getInstance().getReference().child("Users");
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(user).exists()){
                    Users usersData = snapshot.child(user).getValue(Users.class);
                    if (usersData.getPhonenumber().equals(phone)){
                        xacnhan = "true";
                        btnXacNhan.setText("Cập nhật mật khẩu");
                        lbUser.setVisibility(View.INVISIBLE);
                        lbPhone.setVisibility(View.INVISIBLE);
                        lbPass.setVisibility(View.VISIBLE);
                        lbConfirm.setVisibility(View.VISIBLE);
                        loadingBar.dismiss();
                    }
                    else {
                        loadingBar.dismiss();
                        Toast.makeText(ResetPasswordActivity.this, "Sai số điện thoại", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loadingBar.dismiss();
                    Toast.makeText(ResetPasswordActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addControls() {
        ht = findViewById(R.id.tvHienThi);
        loadingBar = new ProgressDialog(this);
        lbUser = findViewById(R.id.lbUsernameForgotPassword);
        lbPass = findViewById(R.id.lbeditTextPassword2Forgot);
        lbConfirm = findViewById(R.id.lbeditTextConfirmPasswordForgot);
        lbPhone = findViewById(R.id.lbPhoneForgotPassword);
        User = findViewById(R.id.UsernameForgotPassword);
        Pass = findViewById(R.id.editTextPassword2Forgot);
        Confirm = findViewById(R.id.editTextConfirmPasswordForgot);
        Phone = findViewById(R.id.PhoneForgotPassword);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        btnExit = findViewById(R.id.btnForgotPasswordExit);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lbConfirm.setVisibility(View.INVISIBLE);
        lbPass.setVisibility(View.INVISIBLE);
    }
}