package vn.stu.edu.doancn.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.stu.edu.doancn.R;

public class OrderHistoryProductViewHolder extends RecyclerView.ViewHolder{

    public TextView order_history_username, order_history_nguoinhan, order_history_phonenumber, order_history_totalprice,
            order_history_address_city, order_history_datetime, order_history_trangthai;
    public Button btnshow_all_order_products_history;

    public OrderHistoryProductViewHolder(@NonNull View itemView) {
        super(itemView);

        order_history_username=itemView.findViewById(R.id.order_history_username);
        order_history_nguoinhan=itemView.findViewById(R.id.order_history_nguoinhan);
        order_history_phonenumber=itemView.findViewById(R.id.order_history_phonenumber);
        order_history_totalprice=itemView.findViewById(R.id.order_history_totalprice);
        order_history_address_city=itemView.findViewById(R.id.order_history_address_city);
        order_history_datetime=itemView.findViewById(R.id.order_history_datetime);
        order_history_trangthai=itemView.findViewById(R.id.order_history_trangthai);
        btnshow_all_order_products_history=itemView.findViewById(R.id.btnshow_all_order_products_history);
    }
}
