package com.example.thuvien.muontra;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class MuonTraAdapter extends BaseAdapter {

    private Context context;
    private List<MuonTra> list;

    public MuonTraAdapter(Context context, List<MuonTra> list) {
        this.context = context;
        this.list = list;
    }

    public void capNhatDuLieu(List<MuonTra> newList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_muontra, parent, false);
            holder = new ViewHolder();
            holder.tvMaMT = convertView.findViewById(R.id.tvMaMT);
            holder.tvDocGia = convertView.findViewById(R.id.tvDocGia);
            holder.tvNhanVien = convertView.findViewById(R.id.tvNhanVien);
            holder.tvNgayMuon = convertView.findViewById(R.id.tvNgayMuon);
            holder.tvHanTra = convertView.findViewById(R.id.tvHanTra);
            holder.tvTrangThai = convertView.findViewById(R.id.tvTrangThai);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MuonTra item = list.get(position);

        holder.tvMaMT.setText("Phiếu " + item.getMaMT());
        holder.tvDocGia.setText("Độc giả: " + item.getTenDG());
        holder.tvNhanVien.setText("Nhân viên: " + item.getTenNV());
        holder.tvNgayMuon.setText("Ngày mượn: " + item.getNgayMuon());
        holder.tvHanTra.setText("Hạn trả: " + item.getHanTra());
        holder.tvTrangThai.setText(item.getTrangThai());

        setTrangThaiStyle(holder.tvTrangThai, item.getTrangThai());

        return convertView;
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

    private static class ViewHolder {
        TextView tvMaMT, tvDocGia, tvNhanVien, tvNgayMuon, tvHanTra, tvTrangThai;
    }
}
