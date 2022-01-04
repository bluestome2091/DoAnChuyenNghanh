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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.adapter.AdapterProductAdmin;
import vn.stu.edu.doancn.model.Products;

public class AdminDetailsProduct extends AppCompatActivity {
    TextInputEditText InputProductId, InputProductName, InputProductPrice, InputProductSize, InputProductCount, InputProductDescription, InputProductDateTime;
    TextInputLayout lbInputProductId, lbInputProductName, lbInputProductPrice, lbInputProductSize, lbInputProductCount, lbInputProductDescription, lbInputProductDateTime;
    TextView btnChonAnh;
    ImageView InputProductImage;
    Products tamp;
    ImageButton btnExit;
    Button btnUpdate;
    static final int GalleryPick = 1;
    Uri ImageUri;
    String Description, Count, Price, Name, id, size, saveCurrentDate, saveCurrentTime;
    String productRamdomKey, downloadImageURL;
    StorageReference ProductImageRef;
    DatabaseReference ProductRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_details_product);
        Intent intent = getIntent();
        tamp = (Products) intent.getSerializableExtra("product");
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        addControls();
        addEvents();
        setData();
    }

    private void setData() {
        Picasso.get().load(tamp.getImage()).into(InputProductImage);
        InputProductId.setText(tamp.getId());
        InputProductName.setText(tamp.getName());
        InputProductPrice.setText(tamp.getPrice());
        InputProductSize.setText(tamp.getSize());
        InputProductCount.setText(tamp.getCount());
        InputProductDescription.setText(tamp.getDescription());
        InputProductDateTime.setText(tamp.getTime()+" "+tamp.getDate());

    }

    private void addEvents() {
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyUpdate();
            }
        });
        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDetailsProduct.this, AdminManagerProduct.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void xuLyUpdate() {
        id = InputProductId.getText().toString();
        Name = InputProductName.getText().toString();
        Price = InputProductPrice.getText().toString();
        size = InputProductSize.getText().toString();
        Count = InputProductCount.getText().toString();
        Description = InputProductDescription.getText().toString();
        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(AdminDetailsProduct.this, "Chưa điền tên sản phẩm....", Toast.LENGTH_SHORT).show();
            lbInputProductName.setError("Chưa điền tên giày");
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(AdminDetailsProduct.this, "Chưa điền giá sản phẩm....", Toast.LENGTH_SHORT).show();
            lbInputProductPrice.setError("Chưa điền giá");
        } else if (TextUtils.isEmpty(Count)) {
            Toast.makeText(AdminDetailsProduct.this, "Chưa điền số lượng sản phẩm....", Toast.LENGTH_SHORT).show();
            lbInputProductCount.setError("Chưa điền số lượng");
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(AdminDetailsProduct.this, "Chưa điền mô tả sản phẩm....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(id)) {
            Toast.makeText(AdminDetailsProduct.this, "Chưa điền mã sản phẩm....", Toast.LENGTH_SHORT).show();
            lbInputProductId.setError("Chưa điền mã giày");
        } else if (TextUtils.isEmpty(size)) {
            Toast.makeText(AdminDetailsProduct.this, "Chưa điền kích thước của giày....", Toast.LENGTH_SHORT).show();
            lbInputProductSize.setError("Chưa điền kích thứcapp:boxStrokeColor=\"@color/black\"\n" +
                    "        app:hintTextColor=\"@color/black\"");
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
        if(ImageUri != null) {
            StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + productRamdomKey);

            final UploadTask uploadTask = filePath.putFile(ImageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(AdminDetailsProduct.this, "Error" + message, Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AdminDetailsProduct.this, "Tải ảnh thành công", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AdminDetailsProduct.this, "Ảnh sản phẩm đã lưu vào database", Toast.LENGTH_SHORT).show();
                                updateProductInformation();
                            }
                        }
                    });
                }
            });
        }else{
            updateProductInformation();
        }
    }

    private void updateProductInformation() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRamdomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("size", size);
        productMap.put("name", Name);
        productMap.put("price", Price);
        productMap.put("count", Count);
        productMap.put("description", Description);
        if(ImageUri != null){
            productMap.put("image", downloadImageURL);
        }else {
            productMap.put("image", tamp.getImage());
        }

        ProductRef.child(id).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(AdminDetailsProduct.this, AdminManagerProduct.class);
                    startActivity(intent);
                    Toast.makeText(AdminDetailsProduct.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    String message = task.getException().toString();
                    Toast.makeText(AdminDetailsProduct.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
    private void addControls() {
        InputProductImage = (ImageView) findViewById(R.id.imgDetailsProduct);
        InputProductId = findViewById(R.id.txtMaDetailsProduct);
        InputProductName = findViewById(R.id.txtTenDetailsProduct);
        InputProductPrice = findViewById(R.id.txtGiaDetailsProduct);
        InputProductSize = findViewById(R.id.txtSizeDetailsProduct);
        InputProductCount = findViewById(R.id.txtSoLuongDetailsProduct);
        InputProductDescription = findViewById(R.id.txtMoTaDetailsProduct);
        InputProductDateTime = findViewById(R.id.txtNgayGioNhapDetailsProduct);

        lbInputProductId = findViewById(R.id.lbMaDetailsProduct);
        lbInputProductName = findViewById(R.id.lbTenDetailsProduct);
        lbInputProductPrice = findViewById(R.id.lbGiaDetailsProduct);
        lbInputProductSize = findViewById(R.id.lbSizeDetailsProduct);
        lbInputProductCount = findViewById(R.id.lbSoLuongDetailsProduct);
        lbInputProductDescription = findViewById(R.id.lbMoTaDetailsProduct);
        lbInputProductDateTime = findViewById(R.id.lbNgayGioNhapDetailsProduct);

        btnExit = findViewById(R.id.btnDetailsProductExit);
        btnUpdate = findViewById(R.id.btnUpdtDetailsProduct);
        btnChonAnh = findViewById(R.id.btnChonAnh);

        loadingBar = new ProgressDialog(this);
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
}