package vn.stu.edu.doancn.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import vn.stu.edu.doancn.R;

public class CreateAccountAdminActivity extends AppCompatActivity {
    EditText user, password, diachi, email, sdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_admin);

    }
}