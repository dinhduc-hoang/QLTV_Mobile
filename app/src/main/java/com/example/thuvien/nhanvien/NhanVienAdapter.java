package com.example.thuvien.nhanvien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.List;

public class NhanVienAdapter extends BaseAdapter {
    private Context context;
    private int layout_id;
    private List<NhanVien> listNhanVien;

    public NhanVienAdapter(Context context, int layout_id, List<NhanVien> listNhanVien) {
        this.context = context;
        this.layout_id = layout_id;
        this.listNhanVien = listNhanVien;
    }

    @Override
    public int getCount() {
        return listNhanVien.size();
    }

    @Override
    public Object getItem(int position) {
        return listNhanVien.get(position);
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

        NhanVien nv = listNhanVien.get(position);

        TextView tvTenNV = view.findViewById(R.id.tvTenNV);
        TextView tvMaNV = view.findViewById(R.id.tvMaNV);
        TextView tvVaiTro = view.findViewById(R.id.tvVaiTro);
        TextView tvGioiTinh = view.findViewById(R.id.tvGioiTinh);
        TextView tvNamSinh = view.findViewById(R.id.tvNamSinh);
        TextView tvQueQuan = view.findViewById(R.id.tvQueQuan);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvSdt = view.findViewById(R.id.tvSdt);
        TextView tvUser = view.findViewById(R.id.tvUser);

        tvTenNV.setText(nv.getTenNV());
        tvMaNV.setText("Mã nhân viên: " + nv.getMaNV());
        tvVaiTro.setText("Vai trò: " + nv.getVaiTro());
        tvGioiTinh.setText("Giới tính: " + nv.getGioiTinh());
        tvNamSinh.setText("Năm sinh: " + nv.getNamSinh());
        tvQueQuan.setText("Quê quán: " + nv.getQueQuan());
        tvEmail.setText("Email: " + nv.getEmail());
        tvSdt.setText("SĐT: " + nv.getSdt());
        tvUser.setText("Tài khoản: " + nv.getUser());

        return view;
    }
}
