package com.example.thuvien.sach;

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

public class SachAdapter extends RecyclerView.Adapter<SachAdapter.SachViewHolder> implements Filterable {

    public interface OnSachListener {
        void onLongClick(String maSach, View view);
    }

    private List<Sach> list;
    private List<Sach> listGoc;
    private final OnSachListener listener;

    public SachAdapter(List<Sach> list, OnSachListener listener) {
        this.list = list;
        this.listGoc = new ArrayList<>(list);
        this.listener = listener;
    }

    public void capNhatDuLieu(List<Sach> newList) {
        this.list = new ArrayList<>(newList);
        this.listGoc = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sach, parent, false);
        return new SachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SachViewHolder holder, int position) {
        final Sach sach = list.get(position);

        holder.tvTenSach.setText(sach.getTenSach());
        holder.tvMaSach.setText("Mã sách: " + sach.getMaSach());
        holder.tvTacGia.setText("Tác giả: " + sach.getTenTG());
        holder.tvTheLoai.setText("Thể loại: " + sach.getTenTL());
        holder.tvNXB.setText("Nhà xuất bản: " + sach.getTenNXB());
        holder.tvNgonNgu.setText("Ngôn ngữ: " + sach.getTenNN());
        holder.tvViTri.setText("Kệ sách: " + sach.getTenViTri());
        holder.tvNamXB.setText("Năm XB: " + sach.getNamXB());
        holder.tvSoLuong.setText("SL: " + sach.getSoLuong());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null) {
                    listener.onLongClick(sach.getMaSach(), view);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class SachViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSach, tvMaSach, tvTacGia, tvTheLoai, tvNXB, tvNgonNgu, tvViTri, tvNamXB, tvSoLuong;

        public SachViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSach = itemView.findViewById(R.id.tvTenSach);
            tvMaSach = itemView.findViewById(R.id.tvMaSach);
            tvTacGia = itemView.findViewById(R.id.tvTacGia);
            tvTheLoai = itemView.findViewById(R.id.tvTheLoai);
            tvNXB = itemView.findViewById(R.id.tvNXB);
            tvNgonNgu = itemView.findViewById(R.id.tvNgonNgu);
            tvViTri = itemView.findViewById(R.id.tvViTri);
            tvNamXB = itemView.findViewById(R.id.tvNamXB);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
        }
    }

    @Override
    public Filter getFilter() {
        return sachFilter;
    }

    private final Filter sachFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Sach> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listGoc);
            } else {
                String keyword = constraint.toString().toLowerCase().trim();

                for (Sach item : listGoc) {
                    if ((item.getTenSach() != null && item.getTenSach().toLowerCase().contains(keyword))
                            || (item.getMaSach() != null && item.getMaSach().toLowerCase().contains(keyword))
                            || (item.getTenTG() != null && item.getTenTG().toLowerCase().contains(keyword))
                            || (item.getTenTL() != null && item.getTenTL().toLowerCase().contains(keyword))
                            || (item.getTenNXB() != null && item.getTenNXB().toLowerCase().contains(keyword))) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = new ArrayList<>((List<Sach>) results.values);
            notifyDataSetChanged();
        }
    };
}