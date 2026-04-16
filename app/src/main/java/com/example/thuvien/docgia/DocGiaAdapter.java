package com.example.thuvien.docgia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class DocGiaAdapter extends BaseAdapter {

    private Context context;
    private List<DocGia> list;

    public DocGiaAdapter(Context context, List<DocGia> list) {
        this.context = context;
        this.list = list;
    }

    public void capNhatDuLieu(List<DocGia> newList) {
        this.list = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_docgia, parent, false);
            holder = new ViewHolder();
            holder.tvTenDG = convertView.findViewById(R.id.tvTenDG);
            holder.tvMaDG = convertView.findViewById(R.id.tvMaDG);
            holder.tvGioiTinh = convertView.findViewById(R.id.tvGioiTinh);
            holder.tvNamSinh = convertView.findViewById(R.id.tvNamSinh);
            holder.tvLop = convertView.findViewById(R.id.tvLop);
            holder.tvKhoa = convertView.findViewById(R.id.tvKhoa);
            holder.tvDiaChi = convertView.findViewById(R.id.tvDiaChi);
            holder.tvEmail = convertView.findViewById(R.id.tvEmail);
            holder.tvSdt = convertView.findViewById(R.id.tvSdt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

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

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTenDG, tvMaDG, tvGioiTinh, tvNamSinh, tvLop, tvKhoa, tvDiaChi, tvEmail, tvSdt;
    }
}
