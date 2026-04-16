package com.example.thuvien.sach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class SachAdapter extends BaseAdapter {

    private Context context;
    private List<Sach> list;

    public SachAdapter(Context context, List<Sach> list) {
        this.context = context;
        this.list = list;
    }

    public void capNhatDuLieu(List<Sach> newList) {
        this.list = newList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sach, parent, false);
            holder = new ViewHolder();
            holder.tvTenSach = convertView.findViewById(R.id.tvTenSach);
            holder.tvMaSach = convertView.findViewById(R.id.tvMaSach);
            holder.tvTacGia = convertView.findViewById(R.id.tvTacGia);
            holder.tvTheLoai = convertView.findViewById(R.id.tvTheLoai);
            holder.tvNXB = convertView.findViewById(R.id.tvNXB);
            holder.tvNgonNgu = convertView.findViewById(R.id.tvNgonNgu);
            holder.tvViTri = convertView.findViewById(R.id.tvViTri);
            holder.tvNamXB = convertView.findViewById(R.id.tvNamXB);
            holder.tvSoLuong = convertView.findViewById(R.id.tvSoLuong);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

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

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTenSach, tvMaSach, tvTacGia, tvTheLoai, tvNXB, tvNgonNgu, tvViTri, tvNamXB, tvSoLuong;
    }
}
