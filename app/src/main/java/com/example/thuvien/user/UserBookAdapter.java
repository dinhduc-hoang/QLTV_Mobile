package com.example.thuvien.user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuvien.R;
import com.example.thuvien.sach.Sach;

import java.util.List;

public class UserBookAdapter extends RecyclerView.Adapter<UserBookAdapter.ViewHolder> {

    private Context context;
    private List<Sach> list;
    private boolean isFeatured;

    public UserBookAdapter(Context context, List<Sach> list, boolean isFeatured) {
        this.context = context;
        this.list = list;
        this.isFeatured = isFeatured;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isFeatured ? R.layout.item_user_book_featured : R.layout.item_user_book_list;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sach sach = list.get(position);
        holder.tvBookName.setText(sach.getTenSach());
        holder.tvAuthorName.setText(sach.getTenTG());

        if (!isFeatured) {
            holder.tvStatus.setText("Còn " + sach.getSoLuong());
            if (sach.getSoLuong() <= 0) {
                holder.tvStatus.setText("Hết sách");
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.orange_primary));
            }
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserBookDetailActivity.class);
            intent.putExtra("MaSach", sach.getMaSach());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookName, tvAuthorName, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
