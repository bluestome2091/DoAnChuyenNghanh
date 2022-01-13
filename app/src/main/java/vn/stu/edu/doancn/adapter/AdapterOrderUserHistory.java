package vn.stu.edu.doancn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import vn.stu.edu.doancn.R;
import vn.stu.edu.doancn.model.UserOrderHistory;
import vn.stu.edu.doancn.model.UsersOrders;
import vn.stu.edu.doancn.user.UserOrderHistoryActivity;
import vn.stu.edu.doancn.user.UserOrderShowProductActivity;

public class AdapterOrderUserHistory extends ArrayAdapter<UserOrderHistory> {
    private Activity context;
    private int resource;
    private List<UserOrderHistory> objects;
    public AdapterOrderUserHistory(Activity context, int resource, List<UserOrderHistory> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item= inflater.inflate(this.resource, null);
        TextView txtND = item.findViewById(R.id.order_history_username);
        TextView txtNN = item.findViewById(R.id.order_history_nguoinhan);
        TextView txtSDT = item.findViewById(R.id.order_history_phonenumber);
        TextView txtTT = item.findViewById(R.id.order_history_totalprice);
        TextView txtDC = item.findViewById(R.id.order_history_address_city);
        TextView txtTGD = item.findViewById(R.id.order_history_datetime);
        TextView txtTrangThai = item.findViewById(R.id.order_history_trangthai);
        Button btnSetTT = item.findViewById(R.id.btnshow_all_order_products_history);
        final UserOrderHistory sp = objects.get(position);
        txtND.setText(sp.getName());
        txtNN.setText(sp.getName());
        txtSDT.setText(sp.getPhone());
        txtTT.setText(sp.getTotalPrice());
        txtDC.setText(sp.getAddress());
        txtTGD.setText(sp.getDate()+ " "+ sp.getTime());
        txtTrangThai.setText(sp.getState());

        btnSetTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserOrderShowProductActivity.class);
                intent.putExtra("oID", sp.getDate() + " " + sp.getTime());
                context.startActivity(intent);
            }
        });
        return item;
    }
}
