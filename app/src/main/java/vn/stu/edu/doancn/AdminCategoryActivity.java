package vn.stu.edu.doancn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AdminCategoryActivity extends AppCompatActivity {
    LinearLayout nike, puma, adidas, underamor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        Create();
        addEvents();
    }

    private void addEvents() {
        nike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminManagerProductActivity.class);
                intent.putExtra("Category", "nike");
                startActivity(intent);
            }
        });
        puma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminManagerProductActivity.class);
                intent.putExtra("Category", "puma");
                startActivity(intent);
            }
        });
        underamor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminManagerProductActivity.class);
                intent.putExtra("Category", "underamor");
                startActivity(intent);
            }
        });
        adidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminManagerProductActivity.class);
                intent.putExtra("Category", "adidas");
                startActivity(intent);
            }
        });
    }

    private void Create() {
        nike = (LinearLayout) findViewById(R.id.nike);
        puma = (LinearLayout) findViewById(R.id.puma);
        underamor = (LinearLayout) findViewById(R.id.underamor);
        adidas = (LinearLayout) findViewById(R.id.adidas);
    }
}