package vn.stu.edu.doancn.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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
import vn.stu.edu.doancn.RegisterActivity;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView settings_profile_image;
    private TextInputEditText settings_name, settings_phone, settings_address, settings_password;
    TextView profile_image_change_btn, close_settings_btn, update_settings_btn, btnChagePassword;
    private TextInputEditText txtUsername_st_edit, txtAddress_st_edit, txtPhone_st_edit, txtPassword_st_edit, txtConfirmPassword_st_edit;
    private Button btnConfirm_edit, btnEdit_st, btnEditPassword_st;

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
        addEvents();
        userInfoDisplay(settings_profile_image, settings_name, settings_phone, settings_address, settings_password);
    }

    private void userInfoDisplay(CircleImageView settings_profile_image, TextInputEditText settings_name, TextInputEditText settings_phone, TextInputEditText settings_address, TextInputEditText settings_password)            {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getUsers());
        UsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("image").exists()) {
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("Name").getValue().toString();
                        String phone = snapshot.child("Phonenumber").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();
                        String password = snapshot.child("Password").getValue().toString();
                        Picasso.get().load(image).into(settings_profile_image);
                        //Picasso.get().load(image).into(settings_profile_image);
                        settings_name.setText(name);
                        settings_phone.setText(phone);
                        settings_address.setText(address);
                        settings_password.setText(password);
                    }
                    else {
                        String name = snapshot.child("Name").getValue().toString();
                        String phone = snapshot.child("Phonenumber").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();
                        String password = snapshot.child("Password").getValue().toString();
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

        btnEdit_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xulyEditProfile();
            }
        });
        btnChagePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xulyEditPasword();
            }
        });
    }

    private void xulyEditPasword() {
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder = new AlertDialog.Builder(SettingsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SettingsActivity.this);
        }

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_settings_password, null);

        txtPassword_st_edit = view.findViewById(R.id.txtNewPassword);
        txtConfirmPassword_st_edit = view.findViewById(R.id.txtConfirmPassword_st);
        btnEditPassword_st = view.findViewById(R.id.btnEditPassword_st);
        builder.setView(view);


        AlertDialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        btnEditPassword_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = txtPassword_st_edit.getText().toString();
                String conpass =  txtConfirmPassword_st_edit.getText().toString();
                if (pass.equals(conpass)){
                    settings_password.setText(txtPassword_st_edit.getText().toString());
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("Password", txtPassword_st_edit.getText().toString());
                            userRef.child("Users").child(Prevalent.currentOnlineUser.getUsers()).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingsActivity.this, "Cập nhật thành công.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SettingsActivity.this, "Cập nhật lỗi.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(SettingsActivity.this, "Mật khẩu không trùng", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void xulyEditProfile() {
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder = new AlertDialog.Builder(SettingsActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SettingsActivity.this);
        }

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_settings_profile, null);

        txtUsername_st_edit = view.findViewById(R.id.txtUsername_st_edit);
        txtPhone_st_edit= view.findViewById(R.id.txtPhone_st_edit);
        txtAddress_st_edit = view.findViewById(R.id.txtAddress_st_edit);
        btnConfirm_edit = view.findViewById(R.id.btnConfirm_edit);
        txtUsername_st_edit.setText(Prevalent.currentOnlineUser.getName());
        txtPhone_st_edit.setText(Prevalent.currentOnlineUser.getPhonenumber());
        txtAddress_st_edit.setText(Prevalent.currentOnlineUser.getAddress());
        builder.setView(view);


        AlertDialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        btnConfirm_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent =getIntent();
//                //c1:
//                name = txtUsername_st_edit.getText().toString();
//                String phone = txtPhone_st_edit.getText().toString();
//                String address = txtAddress_st_edit.getText().toString();
                //c2:
                settings_name.setText(txtUsername_st_edit.getText().toString());
                settings_address.setText(txtAddress_st_edit.getText().toString());
                settings_phone.setText(txtPhone_st_edit.getText().toString());
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, Object> userdataMap = new HashMap<>();
                        userdataMap.put("Name", txtUsername_st_edit.getText().toString());
                        userdataMap.put("Phonenumber", txtPhone_st_edit.getText().toString());
                        userdataMap.put("address", txtAddress_st_edit.getText().toString());
                        userRef.child("Users").child(Prevalent.currentOnlineUser.getUsers()).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SettingsActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SettingsActivity.this, "Cập nhật lỗi", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dialog.dismiss();
                Toast.makeText(SettingsActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SettingsActivity.this, "Lỗi, vui lòng thử lại !", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("Name", settings_name.getText().toString());
        userMap.put("Phonenumber", settings_phone.getText().toString());
        userMap.put("address", settings_address.getText().toString());
        userMap.put("Password", settings_password.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getUsers()).updateChildren(userMap);
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        Toast.makeText(SettingsActivity.this, "Cập nhật thông tin hồ sơ thành công.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoUpdated() {
        if (TextUtils.isEmpty(settings_name.getText().toString())) {
            Toast.makeText(SettingsActivity.this, "Họ tên.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(settings_address.getText().toString())) {
            Toast.makeText(SettingsActivity.this, "Địa chỉ.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(settings_phone.getText().toString())) {
            Toast.makeText(SettingsActivity.this, "Số điện thoại.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(settings_password.getText().toString())) {
            Toast.makeText(SettingsActivity.this, "Mật khẩu.", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")) {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setTitle("Upload Profile.");
        progressDialog.setMessage("Vui lòng đợi, trong khi chúng tôi cập nhật thông tin tài khoản của bạn.");
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
                        userMap.put("Name", settings_name.getText().toString());
                        userMap.put("Phonenumber", settings_phone.getText().toString());
                        userMap.put("address", settings_address.getText().toString());
                        userMap.put("Password", settings_password.getText().toString());
                        userMap.put("image", myUrl);


                        ref.child(Prevalent.currentOnlineUser.getUsers()).updateChildren(userMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                        Toast.makeText(SettingsActivity.this, "Cập nhật thông tin hồ sơ thành công.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Lỗi cập nhật.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(SettingsActivity.this, "Hình ảnh không được chọn", Toast.LENGTH_SHORT).show();
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
        btnChagePassword = findViewById(R.id.txtChange);
        btnEdit_st = findViewById(R.id.btnEditSt);
    }
}