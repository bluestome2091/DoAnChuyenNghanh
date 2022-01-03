package vn.stu.edu.doancn.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rey.material.widget.ImageButton;

import vn.stu.edu.doancn.R;

public class SearchProductsActivity extends AppCompatActivity {
    ImageButton btnSreachProducts;
    RecyclerView searchList;
    TextInputEditText txtSearch;
    TextInputLayout lbSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        addControls();
    }

    private void addControls() {
//        btnExit = (ImageButton) findViewById(R.id.btnSearchtProductsExit);
        btnSreachProducts = findViewById(R.id.btnSreachProducts);
//        searchList = findViewById(R.id.sreach_list);
//        txtSearch = findViewById(R.id.txtsearch);
//        lbSearch = findViewById(R.id.lbsearch);
//        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));
    }
}