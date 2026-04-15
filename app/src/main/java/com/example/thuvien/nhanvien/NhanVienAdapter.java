package com.example.thuvien.nhanvien;

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

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder> implements Filterable {

    public interface OnNhanVienListener {
        void onLongClick(String maNV, View view);
    }

    private List<NhanVien> list;
    private List<NhanVien> listGoc;
    private final OnNhanVienListener listener;

    public NhanVienAdapter(List<NhanVien> list, OnNhanVienListener listener) {
        this.list = list;
        this.listGoc = new ArrayList<>(list);
        this.listener = listener;
    }

    public void capNhatDuLieu(List<NhanVien> newList) {
        this.list = new ArrayList<>(newList);
        this.listGoc = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nhanvien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NhanVien item = list.get(position);

        holder.tvTenNV.setText(item.getTenNV());
        holder.tvMaNV.setText("Mã nhân viên: " + item.getMaNV());
        holder.tvVaiTro.setText("Vai trò: " + item.getVaiTro());
        holder.tvGioiTinh.setText("Giới tính: " + item.getGioiTinh());
        holder.tvNamSinh.setText("Năm sinh: " + item.getNamSinh());
        holder.tvQueQuan.setText("Quê quán: " + item.getQueQuan());
        holder.tvEmail.setText("Email: " + item.getEmail());
        holder.tvSdt.setText("SĐT: " + item.getSdt());
        holder.tvUser.setText("User: " + item.getUser());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongClick(item.getMaNV(), view);
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
        TextView tvTenNV, tvMaNV, tvVaiTro, tvGioiTinh, tvNamSinh, tvQueQuan, tvEmail, tvSdt, tvUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenNV = itemView.findViewById(R.id.tvTenNV);
            tvMaNV = itemView.findViewById(R.id.tvMaNV);
            tvVaiTro = itemView.findViewById(R.id.tvVaiTro);
            tvGioiTinh = itemView.findViewById(R.id.tvGioiTinh);
            tvNamSinh = itemView.findViewById(R.id.tvNamSinh);
            tvQueQuan = itemView.findViewById(R.id.tvQueQuan);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvSdt = itemView.findViewById(R.id.tvSdt);
            tvUser = itemView.findViewById(R.id.tvUser);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<NhanVien> filtered = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(listGoc);
            } else {
                String key = constraint.toString().toLowerCase().trim();
                for (NhanVien item : listGoc) {
                    if ((item.getMaNV() != null && item.getMaNV().toLowerCase().contains(key))
                            || (item.getTenNV() != null && item.getTenNV().toLowerCase().contains(key))
                            || (item.getVaiTro() != null && item.getVaiTro().toLowerCase().contains(key))
                            || (item.getEmail() != null && item.getEmail().toLowerCase().contains(key))
                            || (item.getSdt() != null && item.getSdt().toLowerCase().contains(key))
                            || (item.getUser() != null && item.getUser().toLowerCase().contains(key))) {
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
            list = new ArrayList<>((List<NhanVien>) results.values);
            notifyDataSetChanged();
        }
    };
}
