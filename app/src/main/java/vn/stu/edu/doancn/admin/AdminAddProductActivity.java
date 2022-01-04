package vn.stu.edu.doancn.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.adapter.AdapterProductAdmin;

public class AdminAddProductActivity extends AppCompatActivity {
    TextInputEditText InputProductId, InputProductName, InputProductPrice, InputProductSize, InputProductCount, InputProductDescription;
    TextInputLayout lbInputProductId, lbInputProductName, lbInputProductPrice, lbInputProductSize, lbInputProductCount, lbInputProductDescription;
    Button AddNewProduct;
    ImageButton btnExit;
    ImageView InputProductImage;
    String Description, Count, Price, Name, id, size, saveCurrentDate, saveCurrentTime;
    String productRamdomKey, downloadImageURL;
    static final int GalleryPick = 1;
    Uri ImageUri;
    StorageReference ProductImageRef;
    DatabaseReference ProductRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Create();
        addEvents();
    }

    private void addEvents() {
        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        AddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProduct();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAddProductActivity.this, AdminManagerProduct.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void AddProduct() {
        id = InputProductId.getText().toString();
        Name = InputProductName.getText().toString();
        Price = InputProductPrice.getText().toString();
        size = InputProductSize.getText().toString();
        Count = InputProductCount.getText().toString();
        Description = InputProductDescription.getText().toString();
        if (ImageUri == null) {
            Toast.makeText(AdminAddProductActivity.this, "Please select image....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Name)) {
            Toast.makeText(AdminAddProductActivity.this, "Chưa điền tên sản phẩm....", Toast.LENGTH_SHORT).show();
            lbInputProductName.setError("Chưa điền tên giày");
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(AdminAddProductActivity.this, "Chưa điền giá sản phẩm....", Toast.LENGTH_SHORT).show();
            lbInputProductPrice.setError("Chưa điền giá");
            lbInputProductName.setErrorEnabled(false);
        } else if (TextUtils.isEmpty(Count)) {
            Toast.makeText(AdminAddProductActivity.this, "Chưa điền số lượng sản phẩm....", Toast.LENGTH_SHORT).show();
            lbInputProductCount.setError("Chưa điền số lượng");
            lbInputProductName.setErrorEnabled(false);
            lbInputProductPrice.setErrorEnabled(false);
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(AdminAddProductActivity.this, "Chưa điền mô tả sản phẩm....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(id)) {
            Toast.makeText(AdminAddProductActivity.this, "Chưa điền mã sản phẩm....", Toast.LENGTH_SHORT).show();
            lbInputProductId.setError("Chưa điền mã giày");
            lbInputProductName.setErrorEnabled(false);
            lbInputProductPrice.setErrorEnabled(false);
            lbInputProductCount.setErrorEnabled(false);
        } else if (TextUtils.isEmpty(size)) {
            Toast.makeText(AdminAddProductActivity.this, "Chưa điền kích thước của giày....", Toast.LENGTH_SHORT).show();
            lbInputProductSize.setError("Chưa điền kích thứ");
            lbInputProductName.setErrorEnabled(false);
            lbInputProductPrice.setErrorEnabled(false);
            lbInputProductCount.setErrorEnabled(false);
            lbInputProductId.setErrorEnabled(false);
        } else {
            lbInputProductName.setErrorEnabled(false);
            lbInputProductPrice.setErrorEnabled(false);
            lbInputProductCount.setErrorEnabled(false);
            lbInputProductId.setErrorEnabled(false);
            lbInputProductSize.setErrorEnabled(false);
            StorageProductInformation();
        }

    }

    private void StorageProductInformation() {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Please wait, while we are checking!!!");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        Calendar calendar = Calendar.getInstance();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat currentDate = new SimpleDateFormat(pattern);
        saveCurrentDate = currentDate.format(calendar.getTime());
        pattern = "HH:mm:ss a";
        SimpleDateFormat currentTime = new SimpleDateFormat(pattern);
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRamdomKey = saveCurrentDate+ " " + saveCurrentTime;
        ProductRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child(id).exists()){
                    StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment()+ " " + productRamdomKey);

                    final UploadTask uploadTask = filePath.putFile(ImageUri);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String message = e.toString();
                            Toast.makeText(AdminAddProductActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AdminAddProductActivity.this, "Tải ảnh thành công", Toast.LENGTH_SHORT).show();
                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    downloadImageURL = filePath.getDownloadUrl().toString();
                                    return filePath.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        downloadImageURL = task.getResult().toString();
                                        Toast.makeText(AdminAddProductActivity.this, "Ảnh sản phẩm đã lưu vào database", Toast.LENGTH_SHORT).show();
                                        saveProductInformation();
                                    }
                                }
                            });
                        }
                    });
                }else {
                    lbInputProductId.setError("Mã đã tồn tại");
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void saveProductInformation() {
        ProductRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> productMap = new HashMap<>();
                productMap.put("pid", productRamdomKey);
                productMap.put("date", saveCurrentDate);
                productMap.put("time", saveCurrentTime);
                productMap.put("size", size);
                productMap.put("name", Name);
                productMap.put("price", Price);
                productMap.put("count", Count);
                productMap.put("description", Description);
                productMap.put("image", downloadImageURL);
                productMap.put("id", id);
                if(!snapshot.child(id).exists()){
                    ProductRef.child(id).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(AdminAddProductActivity.this, AdminManagerProduct.class);
                                startActivity(intent);
                                Toast.makeText(AdminAddProductActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                finish();
                                loadingBar.dismiss();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(AdminAddProductActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }else {
                    lbInputProductId.setError("Mã đã tồn tại");
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
            InputProductImage.setBackground(null);

        }
    }

    private void Create() {
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductId = findViewById(R.id.product_id);
        InputProductName = findViewById(R.id.product_name);
        InputProductPrice = findViewById(R.id.product_price);
        InputProductSize = findViewById(R.id.product_size);
        InputProductCount = findViewById(R.id.product_count);
        InputProductDescription = findViewById(R.id.product_description);

        lbInputProductId = findViewById(R.id.lbproduct_id);
        lbInputProductName = findViewById(R.id.lbproduct_name);
        lbInputProductPrice = findViewById(R.id.lbproduct_price);
        lbInputProductSize = findViewById(R.id.lbproduct_size);
        lbInputProductCount = findViewById(R.id.lbproduct_count);
        lbInputProductDescription = findViewById(R.id.lbproduct_description);

        btnExit = findViewById(R.id.btnAddProductExit);
        AddNewProduct = (Button) findViewById(R.id.add_new_product);
        loadingBar = new ProgressDialog(this);
    }
}