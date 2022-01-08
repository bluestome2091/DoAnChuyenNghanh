package vn.stu.edu.doancn.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.stu.edu.doancn.Interface.ItemClickListner;
import vn.stu.edu.doancn.R;

public class UserShowProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtuser_history_productname, txtuser_history_product_quatity, txtuser_history_product_price;
    public ItemClickListner itemClickListner;

    public UserShowProductViewHolder(@NonNull View itemView) {
        super(itemView);

        txtuser_history_productname=itemView.findViewById(R.id.txtuser_history_productname);
        txtuser_history_product_quatity=itemView.findViewById(R.id.txtuser_history_product_quatity);
        txtuser_history_product_price=itemView.findViewById(R.id.txtuser_history_product_price);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
