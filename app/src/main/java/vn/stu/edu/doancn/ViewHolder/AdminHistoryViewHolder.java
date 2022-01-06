package vn.stu.edu.doancn.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.stu.edu.doancn.R;

public class AdminHistoryViewHolder extends RecyclerView.ViewHolder{
    public TextView history_username, history_nguoinhan, history_phonenumber, history_totalprice, history_address_city,
            history_datetime, history_trangthai;
    public Button btnshow_all_products_history;

    public AdminHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        history_username=itemView.findViewById(R.id.history_username);
        history_nguoinhan=itemView.findViewById(R.id.history_nguoinhan);
        history_phonenumber=itemView.findViewById(R.id.history_phonenumber);
        history_totalprice=itemView.findViewById(R.id.history_totalprice);
        history_address_city=itemView.findViewById(R.id.history_address_city);
        history_datetime=itemView.findViewById(R.id.history_datetime);
        history_trangthai=itemView.findViewById(R.id.history_trangthai);
        btnshow_all_products_history=itemView.findViewById(R.id.btnshow_all_products_history);
    }
}
