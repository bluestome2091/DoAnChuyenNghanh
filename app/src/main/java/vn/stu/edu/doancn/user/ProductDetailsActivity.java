package vn.stu.edu.doancn.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import vn.stu.edu.doancn.R;

public class ProductDetailsActivity extends AppCompatActivity {

    private FloatingActionButton add_product_to_cart;
    private ImageView product_image_details;
    private ElegantNumberButton number_btn;
    private TextView txtProductname_details, txtDescription_details, txtPrice_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        addControls();
        addEvents();
    }

    private void addEvents() {
    }

    private void addControls() {
        add_product_to_cart=findViewById(R.id.add_product_to_cart);
        product_image_details=findViewById(R.id.product_image_details);
        number_btn=findViewById(R.id.number_btn);
        txtProductname_details=findViewById(R.id.txtProductname_details);
        txtPrice_details=findViewById(R.id.txtPrice_details);
        txtDescription_details=findViewById(R.id.txtDescription_details);
    }
}