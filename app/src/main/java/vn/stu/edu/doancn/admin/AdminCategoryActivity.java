package vn.stu.edu.doancn.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import vn.stu.edu.doancn.Prevalent.PrevalentAdmin;
import vn.stu.edu.doancn.R;

public class AdminCategoryActivity extends AppCompatActivity {
    LinearLayout sanpham, taikhoan, hoadon;
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
                Intent intent = new Intent(AdminCategoryActivity.this, AdminManagerProductActivity.class);
                startActivity(intent);
            }
        });
        taikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrevalentAdmin.currentOnlineUser.getUsers().equals(checkPermission)){
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminManegerAccountActivity.class);
                    startActivity(intent);
                    Toast.makeText(AdminCategoryActivity.this, checkPermission, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AdminCategoryActivity.this, "Không có quyền truy cập", Toast.LENGTH_SHORT).show();
                }

            }
        });
        hoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void Create() {
        sanpham = (LinearLayout) findViewById(R.id.quanlysanpham);
        taikhoan = (LinearLayout) findViewById(R.id.quanlytaikhoan);
        hoadon = (LinearLayout) findViewById(R.id.quanlyhoadon);
    }
}