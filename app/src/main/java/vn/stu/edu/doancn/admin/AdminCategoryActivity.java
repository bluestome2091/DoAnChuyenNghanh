package vn.stu.edu.doancn.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import vn.stu.edu.doancn.LoginActivity;
import vn.stu.edu.doancn.Prevalent.PrevalentAdmin;
import vn.stu.edu.doancn.R;

public class AdminCategoryActivity extends AppCompatActivity {

    RelativeLayout relativeLogout;
    LinearLayout sanpham, taikhoan, giohang, lichsu;
    String checkPermission = "Admin";

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
                xulymogiaodien();

            }
        });
        giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });
        lichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminHistoryOrdersActivity.class);
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

    private void xulymogiaodien() {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(AdminCategoryActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        LayoutInflater inflater = AdminCategoryActivity.this.getLayoutInflater();
        View view  = inflater.inflate(R.layout.chonchucnangtaikhoan, null);
        Button btnqladmin = view.findViewById(R.id.buttonQLAdmin);
        Button btnqluser = view.findViewById(R.id.buttonQLUser);
        alert.setView(view);
        alert.setCancelable(true);

        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        btnqladmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrevalentAdmin.currentOnlineUser.getUsers().equals(checkPermission)) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminManegerAccountActivity2.class);
                    startActivity(intent);
                    Toast.makeText(AdminCategoryActivity.this, checkPermission, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(AdminCategoryActivity.this, "Không có quyền truy cập", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
        btnqluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminManegerAccountUsersActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    private void Create() {
        sanpham = (LinearLayout) findViewById(R.id.quanlysanpham);
        taikhoan = (LinearLayout) findViewById(R.id.quanlytaikhoan);
        giohang = (LinearLayout) findViewById(R.id.quanlygiohang);
        lichsu = findViewById(R.id.quanlylichsu);
        relativeLogout = findViewById(R.id.relativeLogout);
    }
}