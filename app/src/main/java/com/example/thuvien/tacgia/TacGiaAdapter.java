package com.example.thuvien.tacgia;

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

public class TacGiaAdapter extends RecyclerView.Adapter<TacGiaAdapter.ViewHolder> implements Filterable {

    public interface OnTacGiaListener {
        void onLongClick(String maTG, View view);
    }

    private List<TacGia> list;
    private List<TacGia> listGoc;
    private final OnTacGiaListener listener;

    public TacGiaAdapter(List<TacGia> list, OnTacGiaListener listener) {
        this.list = list;
        this.listGoc = new ArrayList<>(list);
        this.listener = listener;
    }

    public void capNhatDuLieu(List<TacGia> newList) {
        this.list = new ArrayList<>(newList);
        this.listGoc = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tacgia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TacGia item = list.get(position);

        holder.tvTenTG.setText(item.getTenTG());
        holder.tvMaTG.setText("Mã tác giả: " + item.getMaTG());
        holder.tvNamSinh.setText("Năm sinh: " + item.getNamSinh());
        holder.tvGioiTinh.setText("Giới tính: " + item.getGioiTinh());
        holder.tvQuocTich.setText("Quốc tịch: " + item.getQuocTich());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongClick(item.getMaTG(), view);
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
        TextView tvTenTG, tvMaTG, tvNamSinh, tvGioiTinh, tvQuocTich;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenTG = itemView.findViewById(R.id.tvTenTG);
            tvMaTG = itemView.findViewById(R.id.tvMaTG);
            tvNamSinh = itemView.findViewById(R.id.tvNamSinh);
            tvGioiTinh = itemView.findViewById(R.id.tvGioiTinh);
            tvQuocTich = itemView.findViewById(R.id.tvQuocTich);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TacGia> filtered = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(listGoc);
            } else {
                String key = constraint.toString().toLowerCase().trim();
                for (TacGia item : listGoc) {
                    if ((item.getMaTG() != null && item.getMaTG().toLowerCase().contains(key))
                            || (item.getTenTG() != null && item.getTenTG().toLowerCase().contains(key))
                            || (item.getGioiTinh() != null && item.getGioiTinh().toLowerCase().contains(key))
                            || (item.getQuocTich() != null && item.getQuocTich().toLowerCase().contains(key))) {
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
            list = new ArrayList<>((List<TacGia>) results.values);
            notifyDataSetChanged();
        }
    };
}
