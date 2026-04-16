package com.example.thuvien.thethuvien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.List;

public class TheThuVienAdapter extends BaseAdapter {

    private Context context;
    private int layout_id;
    private List<TheThuVien> listThe;

    public TheThuVienAdapter(Context context, int layout_id, List<TheThuVien> listThe) {
        this.context = context;
        this.layout_id = layout_id;
        this.listThe = listThe;
    }

    @Override
    public int getCount() {
        return listThe.size();
    }

    @Override
    public Object getItem(int position) {
        return listThe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout_id, null);
        }

        TheThuVien the = listThe.get(position);

        TextView tvMaThe = view.findViewById(R.id.tvMaThe);
        TextView tvTenDG = view.findViewById(R.id.tvTenDG);
        TextView tvNgayCap = view.findViewById(R.id.tvNgayCap);
        TextView tvNgayHetHan = view.findViewById(R.id.tvNgayHetHan);
        TextView tvTrangThai = view.findViewById(R.id.tvTrangThai);

        tvMaThe.setText("Thẻ " + the.getMaThe());
        tvTenDG.setText("Độc giả: " + the.getTenDG());
        tvNgayCap.setText("Ngày cấp: " + dinhDangNgay(the.getNgayCap()));
        tvNgayHetHan.setText("Ngày hết hạn: " + dinhDangNgay(the.getNgayHetHan()));
        tvTrangThai.setText(the.getTrangThai());

        return view;
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
}
