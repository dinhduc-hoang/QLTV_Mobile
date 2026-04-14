package com.example.thuvien.docgia;

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

public class DocGiaAdapter extends RecyclerView.Adapter<DocGiaAdapter.ViewHolder> implements Filterable {

    public interface OnDocGiaListener {
        void onLongClick(String maDG, View view);
    }

    private List<DocGia> list;
    private List<DocGia> listGoc;
    private final OnDocGiaListener listener;

    public DocGiaAdapter(List<DocGia> list, OnDocGiaListener listener) {
        this.list = list;
        this.listGoc = new ArrayList<>(list);
        this.listener = listener;
    }

    public void capNhatDuLieu(List<DocGia> newList) {
        this.list = new ArrayList<>(newList);
        this.listGoc = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_docgia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DocGia item = list.get(position);

        holder.tvTenDG.setText(item.getTenDG());
        holder.tvMaDG.setText("Mã độc giả: " + item.getMaDG());
        holder.tvGioiTinh.setText("Giới tính: " + item.getGioiTinh());
        holder.tvNamSinh.setText("Năm sinh: " + item.getNamSinh());
        holder.tvLop.setText("Lớp: " + item.getTenLop());
        holder.tvKhoa.setText("Khoa: " + item.getTenKhoa());
        holder.tvDiaChi.setText("Địa chỉ: " + item.getDiaChi());
        holder.tvEmail.setText("Email: " + item.getEmail());
        holder.tvSdt.setText("SĐT: " + item.getSdt());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongClick(item.getMaDG(), view);
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
        TextView tvTenDG, tvMaDG, tvGioiTinh, tvNamSinh, tvLop, tvKhoa, tvDiaChi, tvEmail, tvSdt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDG = itemView.findViewById(R.id.tvTenDG);
            tvMaDG = itemView.findViewById(R.id.tvMaDG);
            tvGioiTinh = itemView.findViewById(R.id.tvGioiTinh);
            tvNamSinh = itemView.findViewById(R.id.tvNamSinh);
            tvLop = itemView.findViewById(R.id.tvLop);
            tvKhoa = itemView.findViewById(R.id.tvKhoa);
            tvDiaChi = itemView.findViewById(R.id.tvDiaChi);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvSdt = itemView.findViewById(R.id.tvSdt);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DocGia> filtered = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered.addAll(listGoc);
            } else {
                String key = constraint.toString().toLowerCase().trim();
                for (DocGia item : listGoc) {
                    if ((item.getTenDG() != null && item.getTenDG().toLowerCase().contains(key))
                            || (item.getMaDG() != null && item.getMaDG().toLowerCase().contains(key))
                            || (item.getEmail() != null && item.getEmail().toLowerCase().contains(key))
                            || (item.getSdt() != null && item.getSdt().toLowerCase().contains(key))
                            || (item.getGioiTinh() != null && item.getGioiTinh().toLowerCase().contains(key))
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
            list = new ArrayList<>((List<DocGia>) results.values);
            notifyDataSetChanged();
        }
    };
}