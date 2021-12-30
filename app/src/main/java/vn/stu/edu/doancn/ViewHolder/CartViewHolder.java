package vn.stu.edu.doancn.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import vn.stu.edu.doancn.Interface.ItemClickListner;
import vn.stu.edu.doancn.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtcart_product_name, txtcart_product_quatity, txtcart_product_price;
    private ItemClickListner ClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        addControls();
    }

    private void addControls() {
        txtcart_product_name=itemView.findViewById(R.id.txtcart_product_name);
        txtcart_product_price=itemView.findViewById(R.id.txtcart_product_price);
        txtcart_product_quatity=itemView.findViewById(R.id.txtcart_product_quatity);
    }

    @Override
    public void onClick(View v) {
        ClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListner itemClickListener) {
        this.ClickListener = itemClickListener;

    }
}
