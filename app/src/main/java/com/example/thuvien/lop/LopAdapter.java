package com.example.thuvien.lop;

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

public class LopAdapter extends RecyclerView.Adapter<LopAdapter.ViewHolder> implements Filterable {

    public interface OnLopListener {
        void onLongClick(String maLop, View view);
    }

    private List<Lop> list;
    private List<Lop> listGoc;
    private final OnLopListener listener;

    public LopAdapter(List<Lop> list, OnLopListener listener) {
        this.list = list;
        this.listGoc = new ArrayList<>(list);
        this.listener = listener;
    }

    public void capNhatDuLieu(List<Lop> newList) {
        this.list = new ArrayList<>(newList);
        this.listGoc = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Lop item = list.get(position);

        holder.tvTenLop.setText(item.getTenLop());
        holder.tvMaLop.setText("Mã lớp: " + item.getMaLop());
        holder.tvKhoa.setText("Khoa: " + item.getTenKhoa());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongClick(item.getMaLop(), view);
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
        TextView tvTenLop, tvMaLop, tvKhoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenLop = itemView.findViewById(R.id.tvTenLop);
            tvMaLop = itemView.findViewById(R.id.tvMaLop);
            tvKhoa = itemView.findViewById(R.id.tvKhoa);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Lop> filtered = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(listGoc);
            } else {
                String key = constraint.toString().toLowerCase().trim();
                for (Lop item : listGoc) {
                    if ((item.getMaLop() != null && item.getMaLop().toLowerCase().contains(key))
                            || (item.getTenLop() != null && item.getTenLop().toLowerCase().contains(key))
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
            list = new ArrayList<>((List<Lop>) results.values);
            notifyDataSetChanged();
        }
    };
}