package vn.stu.edu.doancn.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.model.Users;

public class AdapterAccountAdmin extends ArrayAdapter<Users> {
    private Activity context;
    private int resource;
    private List<Users> objects;
    int tamp = -1;
    public AdapterAccountAdmin(Activity context, int resource, List<Users> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item= inflater.inflate(this.resource, null);
        TextView username = item.findViewById(R.id.txtTaiKhoanAdmin);
        TextView password = item.findViewById(R.id.txtMatKhauAdmin);
        TextView ten = item.findViewById(R.id.txtTenAdmin);
        TextView sdt = item.findViewById(R.id.txtSDTAdmin);
        ImageButton btnSua = item.findViewById(R.id.btnSuaAccountAdmin);
        ImageButton btnXoa = item.findViewById(R.id.btnDeleteAccountAdmin);
        final Users nv = objects.get(position);
        username.setText(username.getText()+ "  " +nv.getUsers());
        password.setText(password.getText()+ "  " +nv.getPassword());
        ten.setText(nv.getName());
        sdt.setText(sdt.getText()+ "  " +nv.getPhonenumber());
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xyLyXoa(nv.getUsers(), position);
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLySua(nv);
                tamp = position;
            }
        });
        return item;
    }

    private void xuLySua(Users nv) {
        Hienthithongbaosua(nv);
    }

    private void Hienthithongbaosua(Users nv) {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        LayoutInflater inflater = context.getLayoutInflater();
        View view  = inflater.inflate(R.layout.dialogsuaaccountadmin, null);
        TextInputEditText name  = view.findViewById(R.id.txtNameedtadmin);
        TextInputEditText phone  = view.findViewById(R.id.txtPhoneedtadmin);
        TextInputEditText pass  = view.findViewById(R.id.txtPasswordedtAdmin);
        Button btnSave = view.findViewById(R.id.btnSaveProfileAdminAccount);
        Button btnCancle = view.findViewById(R.id.btnCancleProfileAdminAccount);
        name.setText(nv.getName().toString());
        phone.setText(nv.getPhonenumber().toString());
        pass.setText(nv.getPassword().toString());

        alert.setView(view);
        alert.setCancelable(true);

        AlertDialog dialog = alert.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = name.getText().toString();
                String dt = phone.getText().toString();
                String mk = pass.getText().toString();
                capNhat(nv.getUsers(),ten, dt, mk);
                dialog.dismiss();

            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void capNhat(String users, String ten, String dt, String mk) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Admins");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("Name", ten);
        userMap.put("Phonenumber", dt);
        userMap.put("Users",users);
        userMap.put("Password", mk);
        ref.child(users).updateChildren(userMap);
        objects.remove(tamp);
        objects.add(tamp, new Users(users, mk, dt, ten,null, null));
        notifyDataSetChanged();
        Toast.makeText(context, "Đã cập nhật thành công", Toast.LENGTH_LONG).show();
    }

    private void xyLyXoa(String nv, int position) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Admins");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc muốn xóa tài khoản nào không");
        builder.setPositiveButton(("Có"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ref.child(nv).removeValue();
                objects.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Xóa thành công tài khoảng", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(("Không"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
