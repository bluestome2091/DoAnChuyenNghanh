package vn.stu.edu.doancn.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.stu.edu.doancn.Interface.ItemClickListner;
import vn.stu.edu.doancn.R;

public class ProductViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

    public TextView txtProductName, txtDecription;
    public ImageView imageView;
    public ItemClickListner listner;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        Create();


    }

    public void setItemClickListener (ItemClickListner listner)
    {
        this.listner= listner;
    }
    private void Create() {
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtDecription = (TextView) itemView.findViewById(R.id.product_name);
        txtDecription = (TextView) itemView.findViewById(R.id.product_description);
    }

    @Override
    public void onClick(View v) {
    listner.onClick(v, getAdapterPosition(), false);
    }

}
