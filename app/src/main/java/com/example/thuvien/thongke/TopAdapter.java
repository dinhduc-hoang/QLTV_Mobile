package com.example.thuvien.thongke;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuvien.R;

import java.util.List;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.Holder> {

    List<String> list;

    public TopAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_simple, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String raw = list.get(position);
        String[] parts = raw.split("\\|");

        if (parts.length >= 1) holder.tvTitle.setText(parts[0]);
        if (parts.length >= 2) holder.tvSubtitle.setText(parts[1]);
        if (parts.length >= 3) holder.tvDetail.setText(parts[2]);
        if (parts.length >= 4) holder.tvBadge.setText(parts[3]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubtitle, tvDetail, tvBadge;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvBadge = itemView.findViewById(R.id.tvBadge);
        }
    }
}
