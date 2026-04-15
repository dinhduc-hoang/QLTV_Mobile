package com.example.thuvien.theloai;

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

public class TheLoaiAdapter extends RecyclerView.Adapter<TheLoaiAdapter.ViewHolder> implements Filterable {

    public interface OnTheLoaiListener {
        void onLongClick(String maTL, View view);
    }

    private List<TheLoai> list;
    private List<TheLoai> listGoc;
    private final OnTheLoaiListener listener;

    public TheLoaiAdapter(List<TheLoai> list, OnTheLoaiListener listener) {
        this.list = list;
        this.listGoc = new ArrayList<>(list);
        this.listener = listener;
    }

    public void capNhatDuLieu(List<TheLoai> newList) {
        this.list = new ArrayList<>(newList);
        this.listGoc = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theloai, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TheLoai item = list.get(position);

        holder.tvTenTL.setText(item.getTenTL());
        holder.tvMaTL.setText("Mã thể loại: " + item.getMaTL());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongClick(item.getMaTL(), view);
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
        TextView tvTenTL, tvMaTL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenTL = itemView.findViewById(R.id.tvTenTL);
            tvMaTL = itemView.findViewById(R.id.tvMaTL);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TheLoai> filtered = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(listGoc);
            } else {
                String key = constraint.toString().toLowerCase().trim();
                for (TheLoai item : listGoc) {
                    if ((item.getMaTL() != null && item.getMaTL().toLowerCase().contains(key))
                            || (item.getTenTL() != null && item.getTenTL().toLowerCase().contains(key))) {
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
            list = new ArrayList<>((List<TheLoai>) results.values);
            notifyDataSetChanged();
        }
    };
}
