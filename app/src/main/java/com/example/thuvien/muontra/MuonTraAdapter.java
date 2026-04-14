package com.example.thuvien.muontra;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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

public class MuonTraAdapter extends RecyclerView.Adapter<MuonTraAdapter.ViewHolder> implements Filterable {

    public interface OnMuonTraListener {
        void onItemClick(String maMT);
        void onItemLongClick(String maMT);
    }

    private List<MuonTra> list;
    private List<MuonTra> listGoc;
    private final OnMuonTraListener listener;

    public MuonTraAdapter(List<MuonTra> list, OnMuonTraListener listener) {
        this.list = list;
        this.listGoc = new ArrayList<>(list);
        this.listener = listener;
    }

    public void capNhatDuLieu(List<MuonTra> newList) {
        this.list = new ArrayList<>(newList);
        this.listGoc = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_muontra, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MuonTra item = list.get(position);

        holder.tvMaMT.setText("Phiếu " + item.getMaMT());
        holder.tvDocGia.setText("Độc giả: " + item.getTenDG());
        holder.tvNhanVien.setText("Nhân viên: " + item.getTenNV());
        holder.tvNgayMuon.setText("Ngày mượn: " + item.getNgayMuon());
        holder.tvHanTra.setText("Hạn trả: " + item.getHanTra());
        holder.tvTrangThai.setText(item.getTrangThai());

        setTrangThaiStyle(holder.tvTrangThai, item.getTrangThai());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(item.getMaMT());
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onItemLongClick(item.getMaMT());
                }
                return true;
            }
        });
    }

    private void setTrangThaiStyle(TextView textView, String trangThai) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(100f);

        if ("Đã trả".equalsIgnoreCase(trangThai)) {
            drawable.setColor(Color.parseColor("#D1FAE5"));
            textView.setTextColor(Color.parseColor("#065F46"));
        } else {
            drawable.setColor(Color.parseColor("#FEF3C7"));
            textView.setTextColor(Color.parseColor("#D97706"));
        }

        textView.setBackground(drawable);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaMT, tvDocGia, tvNhanVien, tvNgayMuon, tvHanTra, tvTrangThai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaMT = itemView.findViewById(R.id.tvMaMT);
            tvDocGia = itemView.findViewById(R.id.tvDocGia);
            tvNhanVien = itemView.findViewById(R.id.tvNhanVien);
            tvNgayMuon = itemView.findViewById(R.id.tvNgayMuon);
            tvHanTra = itemView.findViewById(R.id.tvHanTra);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MuonTra> filtered = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(listGoc);
            } else {
                String key = constraint.toString().toLowerCase().trim();
                for (MuonTra item : listGoc) {
                    if ((item.getMaMT() != null && item.getMaMT().toLowerCase().contains(key))
                            || (item.getTenDG() != null && item.getTenDG().toLowerCase().contains(key))
                            || (item.getTenNV() != null && item.getTenNV().toLowerCase().contains(key))
                            || (item.getTrangThai() != null && item.getTrangThai().toLowerCase().contains(key))) {
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
            list = new ArrayList<>((List<MuonTra>) results.values);
            notifyDataSetChanged();
        }
    };
}