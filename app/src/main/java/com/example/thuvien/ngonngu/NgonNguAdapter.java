package com.example.thuvien.ngonngu;

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

public class NgonNguAdapter extends RecyclerView.Adapter<NgonNguAdapter.ViewHolder> implements Filterable {

    public interface OnNgonNguListener {
        void onLongClick(String maNN, View view);
    }

    private List<NgonNgu> list;
    private List<NgonNgu> listGoc;
    private final OnNgonNguListener listener;

    public NgonNguAdapter(List<NgonNgu> list, OnNgonNguListener listener) {
        this.list = list;
        this.listGoc = new ArrayList<>(list);
        this.listener = listener;
    }

    public void capNhatDuLieu(List<NgonNgu> newList) {
        this.list = new ArrayList<>(newList);
        this.listGoc = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ngonngu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NgonNgu item = list.get(position);

        holder.tvTenNN.setText(item.getTenNN());
        holder.tvMaNN.setText("Mã ngôn ngữ: " + item.getMaNN());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongClick(item.getMaNN(), view);
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
        TextView tvTenNN, tvMaNN;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenNN = itemView.findViewById(R.id.tvTenNN);
            tvMaNN = itemView.findViewById(R.id.tvMaNN);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<NgonNgu> filtered = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(listGoc);
            } else {
                String key = constraint.toString().toLowerCase().trim();
                for (NgonNgu item : listGoc) {
                    if ((item.getMaNN() != null && item.getMaNN().toLowerCase().contains(key))
                            || (item.getTenNN() != null && item.getTenNN().toLowerCase().contains(key))) {
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
            list = new ArrayList<>((List<NgonNgu>) results.values);
            notifyDataSetChanged();
        }
    };
}