package vn.stu.edu.doancn.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import vn.stu.edu.doancn.LoginActivity;
import vn.stu.edu.doancn.Prevalent.PrevalentAdmin;
import vn.stu.edu.doancn.R;

public class AdminCategoryActivity extends AppCompatActivity {

    RelativeLayout relativeLogout;
    LinearLayout sanpham, taikhoan, giohang;
    String checkPermission = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        Create();
        addEvents();
    }

    private void addEvents() {
        sanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AdminCategoryActivity.this, AdminManagerProduct.class);
                startActivity(intent1);
            }
        });
        taikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrevalentAdmin.currentOnlineUser.getUsers().equals(checkPermission)) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminManegerAccountActivity2.class);
                    startActivity(intent);
                    Toast.makeText(AdminCategoryActivity.this, checkPermission, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminCategoryActivity.this, "Không có quyền truy cập", Toast.LENGTH_SHORT).show();
                }

            }
        });
        giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });
        relativeLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Create() {
        sanpham = (LinearLayout) findViewById(R.id.quanlysanpham);
        taikhoan = (LinearLayout) findViewById(R.id.quanlytaikhoan);
        giohang = (LinearLayout) findViewById(R.id.quanlygiohang);
        relativeLogout = findViewById(R.id.relativeLogout);
    }
}