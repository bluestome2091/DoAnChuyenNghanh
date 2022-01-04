package vn.stu.edu.doancn.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.stu.edu.doancn.R;

public class AdminOrdersViewHolder extends RecyclerView.ViewHolder {

    public TextView orders_username, orders_phonenumber, orders_totalprice, orders_address_city, orders_datetime;
    public Button btnshow_all_products;

    public AdminOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        orders_username = itemView.findViewById(R.id.orders_username);
        orders_phonenumber = itemView.findViewById(R.id.orders_phonenumber);
        orders_address_city = itemView.findViewById(R.id.orders_address_city);
        orders_totalprice = itemView.findViewById(R.id.orders_totalprice);
        orders_datetime = itemView.findViewById(R.id.orders_datetime);
        btnshow_all_products = itemView.findViewById(R.id.btnshow_all_products);

    }
}
