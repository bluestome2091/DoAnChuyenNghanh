package vn.stu.edu.doancn.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.stu.edu.doancn.R;

public class UserOrderViewHolder extends RecyclerView.ViewHolder{

    public TextView username_order, nguoinhan_order, phonenumber_order, totalprice_order, 
            address_city_order, datetime_order, trangthai_order;
    public Button btnDeleteOrder;

    public UserOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        
        addControls();
    }

    private void addControls() {
        username_order=itemView.findViewById(R.id.username_order);
        nguoinhan_order=itemView.findViewById(R.id.nguoinhan_order);
        phonenumber_order=itemView.findViewById(R.id.phonenumber_order);
        totalprice_order=itemView.findViewById(R.id.totalprice_order);
        address_city_order=itemView.findViewById(R.id.address_city_order);
        datetime_order=itemView.findViewById(R.id.datetime_order);
        trangthai_order=itemView.findViewById(R.id.trangthai_order);
        btnDeleteOrder=itemView.findViewById(R.id.btnDeleteOrder);

    }
}
