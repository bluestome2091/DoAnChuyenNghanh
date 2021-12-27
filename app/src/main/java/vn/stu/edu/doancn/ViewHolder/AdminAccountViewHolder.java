package vn.stu.edu.doancn.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.stu.edu.doancn.Interface.ItemClickListner;
import vn.stu.edu.doancn.R;

public class AdminAccountViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
    public TextView username, password, hoten, sdt;
    public ItemClickListner listner;
    public AdminAccountViewHolder(@NonNull View itemView) {
        super(itemView);
        Create();

    }

    public void setItemClickListener (ItemClickListner listner)
    {
        this.listner= listner;
    }
    private void Create() {
        username = (TextView) itemView.findViewById(R.id.txtTaiKhoanAdmin);
        password = (TextView) itemView.findViewById(R.id.txtMatKhauAdmin);
        hoten = (TextView) itemView.findViewById(R.id.txtTenAdmin);
        sdt = (TextView) itemView.findViewById(R.id.txtSDTAdmin);
    }

    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);
    }
}
