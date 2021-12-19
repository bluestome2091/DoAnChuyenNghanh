package vn.stu.edu.doancn.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.stu.edu.doancn.MainActivity;
import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.R;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView settings_profile_image;
    private TextInputEditText settings_name, settings_phone, settings_address, settings_password;
    private TextView profile_image_change_btn, close_settings_btn, update_settings_btn;
    private Button btnEditSt;
    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfileReference;
    private String checker = "";
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfileReference = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        addControls();
//        addEvents();
        userInfoDisplay();
    }

    private void userInfoDisplay()            {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getUsers());
        UsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("image").exists()) {
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();
                        String password = snapshot.child("Password").getValue().toString();
                        Picasso.get().load(image).into(settings_profile_image);
                        settings_name.setText(name);
                        settings_phone.setText(phone);
                        settings_address.setText(address);
                        settings_password.setText(password);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvents() {
        close_settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update_settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")) {
                    userInfoUpdated();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });

        profile_image_change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
                CropImage.activity(imageUri).setAspectRatio(1, 1)
                        .start(SettingsActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            settings_profile_image.setImageURI(imageUri);
        } else {
            Toast.makeText(SettingsActivity.this, "Error, Try Again !!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", settings_name.getText().toString());
        userMap.put("phone", settings_phone.getText().toString());
        userMap.put("address", settings_address.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhonenumber()).updateChildren(userMap);
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Info update successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoUpdated() {
        if (TextUtils.isEmpty(settings_name.getText().toString())) {
            Toast.makeText(SettingsActivity.this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(settings_address.getText().toString())) {
            Toast.makeText(SettingsActivity.this, "Name is address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(settings_phone.getText().toString())) {
            Toast.makeText(SettingsActivity.this, "Name is phone", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")) {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setTitle("Upload Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference fileRef = storageProfileReference.child(Prevalent.currentOnlineUser.getPhonenumber() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", settings_name.getText().toString());
                        userMap.put("phone", settings_phone.getText().toString());
                        userMap.put("address", settings_address.getText().toString());
                        userMap.put("image", myUrl);


                        ref.child(Prevalent.currentOnlineUser.getUsers()).updateChildren(userMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile Info update successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(SettingsActivity.this, "image is not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void addControls() {
        settings_profile_image = findViewById(R.id.settings_profile_image);
        settings_name = findViewById(R.id.txtName_st);
        settings_phone = findViewById(R.id.txtPhone_st);
        settings_address = findViewById(R.id.txtAddress_st);
        settings_password = findViewById(R.id.txtPassword_st);
        profile_image_change_btn = findViewById(R.id.profile_image_change_btn);
        close_settings_btn = findViewById(R.id.close_settings_btn);
        update_settings_btn = findViewById(R.id.update_settings_btn);
        btnEditSt = findViewById(R.id.btnEditSt);
    }
}