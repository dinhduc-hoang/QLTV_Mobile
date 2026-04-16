package com.example.thuvien.thethuvien;

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
import java.util.Calendar;
import java.util.List;

public class TheThuVienAdapter extends BaseAdapter {

    private Context context;
    private List<TheThuVien> list;

    public TheThuVienAdapter(Context context, List<TheThuVien> list) {
        this.context = context;
        this.list = list;
    }

    public void capNhatDuLieu(List<TheThuVien> newList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_thethuvien, parent, false);
            holder = new ViewHolder();
            holder.tvMaThe = convertView.findViewById(R.id.tvMaThe);
            holder.tvTenDG = convertView.findViewById(R.id.tvTenDG);
            holder.tvNgayCap = convertView.findViewById(R.id.tvNgayCap);
            holder.tvNgayHetHan = convertView.findViewById(R.id.tvNgayHetHan);
            holder.tvTrangThai = convertView.findViewById(R.id.tvTrangThai);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final TheThuVien item = list.get(position);

        String ngayCapStr = dinhDangNgay(item.getNgayCap());
        String ngayHHStr = dinhDangNgay(item.getNgayHetHan());

        // Tự động tính toán trạng thái thực tế dựa trên ngày hiện tại
        String trangThai = "Còn hiệu lực";
        try {
            String[] p = ngayHHStr.split("/");
            if (p.length == 3) {
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(p[2]), Integer.parseInt(p[1]) - 1, Integer.parseInt(p[0]), 23, 59);
                if (c.before(Calendar.getInstance())) {
                    trangThai = "Hết hiệu lực";
                }
            }
        } catch (Exception e) {
            trangThai = item.getTrangThai();
        }

        holder.tvMaThe.setText("Thẻ " + item.getMaThe());
        holder.tvTenDG.setText("Độc giả: " + item.getTenDG());
        holder.tvNgayCap.setText("Ngày cấp: " + ngayCapStr);
        holder.tvNgayHetHan.setText("Ngày hết hạn: " + ngayHHStr);
        holder.tvTrangThai.setText(trangThai);

        setTrangThaiStyle(holder.tvTrangThai, trangThai);

        return convertView;
    }

    private String dinhDangNgay(String ngay) {
        if (ngay != null && ngay.contains("-")) {
            String[] parts = ngay.split("-");
            if (parts.length == 3) {
                return parts[2] + "/" + parts[1] + "/" + parts[0];
            }
        }
        return ngay;
    }

    private void setTrangThaiStyle(TextView textView, String trangThai) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(100f);

        if ("Hết hiệu lực".equalsIgnoreCase(trangThai)) {
            drawable.setColor(Color.parseColor("#FEE2E2"));
            textView.setTextColor(Color.parseColor("#DC2626"));
        } else {
            drawable.setColor(Color.parseColor("#D1FAE5"));
            textView.setTextColor(Color.parseColor("#16A34A"));
        }

        textView.setBackground(drawable);
    }

    private static class ViewHolder {
        TextView tvMaThe, tvTenDG, tvNgayCap, tvNgayHetHan, tvTrangThai;
    }
}
