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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import vn.stu.edu.doancn.R;

public class AdminManagerProductActivity extends AppCompatActivity {
    EditText InputProductName, InputProductPrice, InputProductCount, InputProductDescription;
    Button AddNewProduct;
    ImageView InputProductImage;
    String Description, Count, Price, Name, saveCurrentDate, saveCurrentTime;
    String productRamdomKey, downloadImageURL;
    static final int GalleryPick = 1;
    Uri ImageUri;
    StorageReference ProductImageRef;
    DatabaseReference ProductRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manager_product);
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
    }

    private void AddProduct() {
        Name = InputProductName.getText().toString();
        Price = InputProductPrice.getText().toString();
        Count = InputProductCount.getText().toString();
        Description = InputProductDescription.getText().toString();
        if (ImageUri == null) {
            Toast.makeText(AdminManagerProductActivity.this, "Please select image....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Name)) {
            Toast.makeText(AdminManagerProductActivity.this, "Chưa điền tên sản phẩm....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(AdminManagerProductActivity.this, "Chưa điền giá sản phẩm....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Count)) {
            Toast.makeText(AdminManagerProductActivity.this, "Chưa điền số lượng sản phẩm....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(AdminManagerProductActivity.this, "Chưa điền mô tả sản phẩm....", Toast.LENGTH_SHORT).show();
        } else {
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

        StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + productRamdomKey);

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminManagerProductActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminManagerProductActivity.this, "Tải ảnh thành công", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AdminManagerProductActivity.this, "Ảnh sản phẩm đã lưu vào database", Toast.LENGTH_SHORT).show();
                            saveProductInformation();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInformation() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRamdomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("name", Name);
        productMap.put("price", Price);
        productMap.put("count", Count);
        productMap.put("description", productRamdomKey);
        productMap.put("image", downloadImageURL);

        ProductRef.child(productRamdomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(AdminManagerProductActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);
                    Toast.makeText(AdminManagerProductActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    String message = task.getException().toString();
                    Toast.makeText(AdminManagerProductActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                }
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
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        InputProductCount = (EditText) findViewById(R.id.product_count);
        AddNewProduct = (Button) findViewById(R.id.add_new_product);
        loadingBar = new ProgressDialog(this);
    }
}