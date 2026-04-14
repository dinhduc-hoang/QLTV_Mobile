package com.example.thuvien.khoa;

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

public class KhoaAdapter extends RecyclerView.Adapter<KhoaAdapter.ViewHolder> implements Filterable {

    public interface OnKhoaListener {
        void onLongClick(String maKhoa, View view);
    }

    private List<Khoa> list;
    private List<Khoa> listGoc;
    private final OnKhoaListener listener;

    public KhoaAdapter(List<Khoa> list, OnKhoaListener listener) {
        this.list = list;
        this.listGoc = new ArrayList<>(list);
        this.listener = listener;
    }

    public void capNhatDuLieu(List<Khoa> newList) {
        this.list = new ArrayList<>(newList);
        this.listGoc = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khoa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Khoa item = list.get(position);

        holder.tvTenKhoa.setText(item.getTenKhoa());
        holder.tvMaKhoa.setText("Mã khoa: " + item.getMaKhoa());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongClick(item.getMaKhoa(), view);
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
        TextView tvTenKhoa, tvMaKhoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKhoa = itemView.findViewById(R.id.tvTenKhoa);
            tvMaKhoa = itemView.findViewById(R.id.tvMaKhoa);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Khoa> filtered = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(listGoc);
            } else {
                String key = constraint.toString().toLowerCase().trim();
                for (Khoa item : listGoc) {
                    if ((item.getMaKhoa() != null && item.getMaKhoa().toLowerCase().contains(key))
                            || (item.getTenKhoa() != null && item.getTenKhoa().toLowerCase().contains(key))) {
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
            list = new ArrayList<>((List<Khoa>) results.values);
            notifyDataSetChanged();
        }
    };
}