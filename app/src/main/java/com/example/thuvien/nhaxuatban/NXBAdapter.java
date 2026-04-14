package com.example.thuvien.nhaxuatban;

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

public class NXBAdapter extends RecyclerView.Adapter<NXBAdapter.ViewHolder> implements Filterable {

    public interface OnNXBListener {
        void onLongClick(String maNXB, View view);
    }

    private List<NXB> list;
    private List<NXB> listGoc;
    private final OnNXBListener listener;

    public NXBAdapter(List<NXB> list, OnNXBListener listener) {
        this.list = list;
        this.listGoc = new ArrayList<>(list);
        this.listener = listener;
    }

    public void capNhatDuLieu(List<NXB> newList) {
        this.list = new ArrayList<>(newList);
        this.listGoc = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nxb, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NXB item = list.get(position);

        holder.tvTen.setText(item.getTenNXB());
        holder.tvSub1.setText("Mã: " + item.getMaNXB());
        holder.tvSub2.setText("Email: " + item.getEmail());
        holder.tvSub3.setText("SĐT: " + item.getSdt());
        holder.tvSub4.setText("Địa chỉ: " + item.getDiaChi());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongClick(item.getMaNXB(), view);
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
        TextView tvTen, tvSub1, tvSub2, tvSub3, tvSub4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvSub1 = itemView.findViewById(R.id.tvSub1);
            tvSub2 = itemView.findViewById(R.id.tvSub2);
            tvSub3 = itemView.findViewById(R.id.tvSub3);
            tvSub4 = itemView.findViewById(R.id.tvSub4);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<NXB> filtered = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(listGoc);
            } else {
                String key = constraint.toString().toLowerCase().trim();
                for (NXB item : listGoc) {
                    if ((item.getMaNXB() != null && item.getMaNXB().toLowerCase().contains(key))
                            || (item.getTenNXB() != null && item.getTenNXB().toLowerCase().contains(key))
                            || (item.getEmail() != null && item.getEmail().toLowerCase().contains(key))
                            || (item.getSdt() != null && item.getSdt().toLowerCase().contains(key))
                            || (item.getDiaChi() != null && item.getDiaChi().toLowerCase().contains(key))) {
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
            list = new ArrayList<>((List<NXB>) results.values);
            notifyDataSetChanged();
        }
    };
}