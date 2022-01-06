package vn.stu.edu.doancn.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.stu.edu.doancn.Interface.ItemClickListner;
import vn.stu.edu.doancn.R;

public class AdminHistoryProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txthistory_productname, txthistory_product_quatity, txthistory_product_price;
    public ItemClickListner itemClickListner;

    public AdminHistoryProductViewHolder(@NonNull View itemView) {
        super(itemView);

        addControls();
    }

    private void addControls() {
        txthistory_productname=itemView.findViewById(R.id.txthistory_productname);
        txthistory_product_quatity=itemView.findViewById(R.id.txthistory_product_quatity);
        txthistory_product_price=itemView.findViewById(R.id.txthistory_product_price);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
