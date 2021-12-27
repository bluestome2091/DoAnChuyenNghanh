package vn.stu.edu.doancn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.model.Users;

public class AdapterAccountAdmin extends ArrayAdapter<Users> {
    Activity context;
    int resource;
    List<Users> objects;
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
        final Users nv = objects.get(position);
        username.setText(username.getText()+ "  " +nv.getUsers());
        password.setText(password.getText()+ "  " +nv.getPassword());
        ten.setText(ten.getText()+ "  " +nv.getName());
        sdt.setText(sdt.getText()+ "  " +nv.getPhonenumber());
        return item;
    }
}
