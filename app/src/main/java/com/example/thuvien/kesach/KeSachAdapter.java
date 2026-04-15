package com.example.thuvien.kesach;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class KeSachAdapter extends RecyclerView.Adapter<KeSachAdapter.ViewHolder> implements Filterable {

    public interface OnKeSachListener {
        void onLongClick(String maViTri, View view);
    }

    private List<KeSach> list;
    private List<KeSach> listGoc;
    private final OnKeSachListener listener;

    public KeSachAdapter(List<KeSach> list, OnKeSachListener listener) {
        this.list = list;
        this.listGoc = new ArrayList<>(list);
        this.listener = listener;
    }

    public void capNhatDuLieu(List<KeSach> newList) {
        this.list = new ArrayList<>(newList);
        this.listGoc = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kesach, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final KeSach item = list.get(position);

        holder.tvTenKe.setText(item.getTenKe());
        holder.tvMaViTri.setText("Mã vị trí: " + item.getMaViTri());
        holder.tvMoTa.setText("Mô tả: " + item.getMoTa());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongClick(item.getMaViTri(), view);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenKe, tvMaViTri, tvMoTa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKe = itemView.findViewById(R.id.tvTenKe);
            tvMaViTri = itemView.findViewById(R.id.tvMaViTri);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<KeSach> filtered = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(listGoc);
            } else {
                String key = constraint.toString().toLowerCase().trim();
                for (KeSach item : listGoc) {
                    if ((item.getMaViTri() != null && item.getMaViTri().toLowerCase().contains(key))
                            || (item.getTenKe() != null && item.getTenKe().toLowerCase().contains(key))
                            || (item.getMoTa() != null && item.getMoTa().toLowerCase().contains(key))) {
                        filtered.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = new ArrayList<>((List<KeSach>) results.values);
            notifyDataSetChanged();
        }
    };
}
