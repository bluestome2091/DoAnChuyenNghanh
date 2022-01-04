package vn.stu.edu.doancn.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.ViewHolder.ProductViewHolder;
import vn.stu.edu.doancn.model.Products;

public class SearchProductsActivity extends AppCompatActivity {
    ImageButton btnExit, btnSearch;
    RecyclerView search_list;
    TextInputLayout lbsearch;
    TextInputEditText txtsearch;
    String key = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchProductsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = txtsearch.getText().toString();
                onStart();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchProductsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(ref.orderByChild("name").startAt(key), Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options){
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                        productViewHolder.txtProductName.setText(products.getName());
                        productViewHolder.txtProductPrice.setText("Price: " + products.getPrice() + "VND");
                        productViewHolder.txtDecription.setText(products.getDescription());
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("id", products);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout, parent, false);
                        ProductViewHolder productViewHolder = new ProductViewHolder(view);
                        return productViewHolder;
                    }
                };
        search_list.setAdapter(adapter);
        adapter.startListening();
    }

    private void addControls() {
        btnExit = findViewById(R.id.btnSearchProductExit);
        btnSearch = findViewById(R.id.btnSreachProducts);
        search_list  = findViewById(R.id.search_list);
        lbsearch =  findViewById(R.id.lbsearch);
        txtsearch = findViewById(R.id.txtsearch);
        search_list.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));
    }
}