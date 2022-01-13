package vn.stu.edu.doancn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import vn.stu.edu.doancn.Prevalent.Prevalent;
import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.admin.AdminDetailsProduct;
import vn.stu.edu.doancn.model.Products;
import vn.stu.edu.doancn.model.UsersOrders;

public class AdapterOrderUser extends ArrayAdapter<UsersOrders> {
    private Activity context;
    private int resource;
    private List<UsersOrders> objects;
    public AdapterOrderUser(Activity context, int resource, List<UsersOrders> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item= inflater.inflate(this.resource, null);
        TextView txtND = item.findViewById(R.id.username_order);
        TextView txtNN = item.findViewById(R.id.nguoinhan_order);
        TextView txtSDT = item.findViewById(R.id.phonenumber_order);
        TextView txtTT = item.findViewById(R.id.totalprice_order);
        TextView txtDC = item.findViewById(R.id.address_city_order);
        TextView txtTGD = item.findViewById(R.id.datetime_order);
        TextView txtTrangThai = item.findViewById(R.id.trangthai_order);
        Button btnSetTT = item.findViewById(R.id.btnDeleteOrder);
        final UsersOrders sp = objects.get(position);
        txtND.setText(sp.getName());
        txtNN.setText(sp.getName());
        txtSDT.setText(sp.getPhone());
        txtTT.setText(sp.getTotalPrice());
        txtDC.setText(sp.getAddress());
        txtTGD.setText(sp.getDate()+ " "+ sp.getTime());
        txtTrangThai.setText(sp.getState());
        if (!sp.getState().equals("Đang xử lý")){
            btnSetTT.setText("Đã nhận hàng");
        }
        btnSetTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Orders");
                if (sp.getState().equals("Đang xử lý")){
                    userRef.child(sp.getDate()+ " "+ sp.getTime()).removeValue();
                    DatabaseReference refProduct = FirebaseDatabase.getInstance().getReference().child("CartList").child("AdminsView");
                    refProduct.child(sp.getId()).removeValue();
                    objects.remove(position);
                    notifyDataSetChanged();
                }
                else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("HistoryOder");
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("address", sp.getAddress());
                    userMap.put("name", sp.getName());
                    userMap.put("phone", sp.getPhone());
                    userMap.put("date", sp.getDate());
                    userMap.put("city", sp.getCity());
                    userMap.put("state", "Đã nhận hàng");
                    userMap.put("id", sp.getId());
                    userMap.put("time", sp.getId());
                    userMap.put("totalPrice", sp.getTotalPrice());
                    ref.child(sp.getDate()+ " "+ sp.getTime()).updateChildren(userMap);
                    DatabaseReference refProduct = FirebaseDatabase.getInstance().getReference().child("CartList").child("AdminsView").child(sp.getId()).child("Products");
                    refProduct.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DatabaseReference refHistoryProduct = FirebaseDatabase.getInstance().getReference().child("HistoryProduct");
                            for (DataSnapshot n : snapshot.getChildren()){
                                HashMap<String, Object> userMap1 = new HashMap<>();
                                userMap1.put("discount", n.child("discount").getValue());
                                userMap1.put("name", n.child("name").getValue());
                                userMap1.put("quatity", n.child("quatity").getValue());
                                userMap1.put("pid", n.child("pid").getValue());
                                userMap1.put("user", sp.getId());
                                userMap1.put("time", n.child("time").getValue());
                                userMap1.put("price", n.child("price").getValue());
                                refHistoryProduct.child(sp.getDate()+ " "+ sp.getTime()).child(n.child("date").getValue ()+ " " + n.child("time").getValue()).updateChildren(userMap1);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        return item;
    }
}
