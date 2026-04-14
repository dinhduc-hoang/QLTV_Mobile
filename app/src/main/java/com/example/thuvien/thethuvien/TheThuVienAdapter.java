package com.example.thuvien.thethuvien;

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

public class TheThuVienAdapter extends RecyclerView.Adapter<TheThuVienAdapter.Holder> implements Filterable {

    public interface OnItemListener {
        void onLongClick(String id, View view);
    }

    private List<TheThuVien> list;
    private List<TheThuVien> goc;
    private final OnItemListener listener;

    public TheThuVienAdapter(List<TheThuVien> list, OnItemListener listener) {
        this.list = list;
        this.goc = new ArrayList<>(list);
        this.listener = listener;
    }

    public void capNhatDuLieu(List<TheThuVien> newList) {
        this.list = new ArrayList<>(newList);
        this.goc = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new Holder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_thethuvien, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final TheThuVien item = list.get(position);

        holder.tvMaThe.setText("Thẻ " + item.getMaThe());
        holder.tvTenDG.setText("Độc giả: " + item.getTenDG());
        holder.tvNgayCap.setText("Ngày cấp: " + item.getNgayCap());
        holder.tvNgayHetHan.setText("Ngày hết hạn: " + item.getNgayHetHan());
        holder.tvTrangThai.setText(item.getTrangThai());

        setTrangThaiStyle(holder.tvTrangThai, item.getTrangThai());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onLongClick(item.getMaThe(), v);
                }
                return true;
            }
        });
    }

    private void setTrangThaiStyle(TextView textView, String trangThai) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(100f);

        if ("Hết hạn".equalsIgnoreCase(trangThai)) {
            drawable.setColor(Color.parseColor("#FEE2E2"));
            textView.setTextColor(Color.parseColor("#B91C1C"));
        } else if ("Khóa".equalsIgnoreCase(trangThai)) {
            drawable.setColor(Color.parseColor("#E5E7EB"));
            textView.setTextColor(Color.parseColor("#374151"));
        } else {
            drawable.setColor(Color.parseColor("#D1FAE5"));
            textView.setTextColor(Color.parseColor("#065F46"));
        }

        textView.setBackground(drawable);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView tvMaThe, tvTenDG, tvNgayCap, tvNgayHetHan, tvTrangThai;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvMaThe = itemView.findViewById(R.id.tvMaThe);
            tvTenDG = itemView.findViewById(R.id.tvTenDG);
            tvNgayCap = itemView.findViewById(R.id.tvNgayCap);
            tvNgayHetHan = itemView.findViewById(R.id.tvNgayHetHan);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence c) {
            List<TheThuVien> f = new ArrayList<>();

            if (c == null || c.length() == 0) {
                f.addAll(goc);
            } else {
                String k = c.toString().toLowerCase().trim();
                for (TheThuVien item : goc) {
                    if ((item.getTenDG() != null && item.getTenDG().toLowerCase().contains(k))
                            || (item.getMaDG() != null && item.getMaDG().toLowerCase().contains(k))
                            || (item.getMaThe() != null && item.getMaThe().toLowerCase().contains(k))
                            || (item.getTrangThai() != null && item.getTrangThai().toLowerCase().contains(k))) {
                        f.add(item);
                    }
                }
            }

            FilterResults r = new FilterResults();
            r.values = f;
            return r;
        }

        @Override
        protected void publishResults(CharSequence c, FilterResults r) {
            list = new ArrayList<>((List<TheThuVien>) r.values);
            notifyDataSetChanged();
        }
    };
}