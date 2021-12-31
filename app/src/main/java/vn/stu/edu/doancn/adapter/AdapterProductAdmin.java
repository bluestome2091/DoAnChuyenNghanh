package vn.stu.edu.doancn.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.admin.AdminDetailsProduct;
import vn.stu.edu.doancn.model.Products;
import vn.stu.edu.doancn.model.Users;

public class AdapterProductAdmin extends ArrayAdapter<Products> {
    private Activity context;
    private int resource;
    private List<Products> objects;
    int tamp = -1;
    public AdapterProductAdmin(Activity context, int resource, List<Products> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item= inflater.inflate(this.resource, null);
        TextView ma = item.findViewById(R.id.txtMaSanPhamAdmin);
        TextView ten = item.findViewById(R.id.txtSanPhamAdmin);
        TextView ngaynhap = item.findViewById(R.id.txtNgayNhapSanPhamAdmin);
        TextView chitiet = item.findViewById(R.id.btnChiTietSpAdmin);
        ImageView hinhanh = item.findViewById(R.id.imgHinhSanPhamAdmin);
        final Products sp = objects.get(position);
        ma.setText(sp.getId());
        ten.setText(sp.getName());
        ngaynhap.setText(sp.getDate());
        Picasso.get().load(sp.getImage()).into(hinhanh);
        chitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminDetailsProduct.class);
                intent.putExtra("product", sp);
                context.startActivity(intent);
            }
        });
        return item;
    }

}
