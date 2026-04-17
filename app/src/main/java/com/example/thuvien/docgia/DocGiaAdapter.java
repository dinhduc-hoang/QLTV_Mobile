package com.example.thuvien.docgia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.List;

public class DocGiaAdapter extends BaseAdapter {

    private Context context;
    private int layout_id;
    private List<DocGia> listDocGia;

    public DocGiaAdapter(Context context, int layout_id, List<DocGia> listDocGia) {
        this.context = context;
        this.layout_id = layout_id;
        this.listDocGia = listDocGia;
    }

    @Override
    public int getCount() {
        return listDocGia.size();
    }

    @Override
    public Object getItem(int position) {
        return listDocGia.get(position);
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

        DocGia dg = listDocGia.get(position);

        TextView tvTenDG = view.findViewById(R.id.tvTenDG);
        TextView tvMaDG = view.findViewById(R.id.tvMaDG);
        TextView tvGioiTinh = view.findViewById(R.id.tvGioiTinh);
        TextView tvNamSinh = view.findViewById(R.id.tvNamSinh);
        TextView tvLop = view.findViewById(R.id.tvLop);
        TextView tvKhoa = view.findViewById(R.id.tvKhoa);
        TextView tvDiaChi = view.findViewById(R.id.tvDiaChi);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvSdt = view.findViewById(R.id.tvSdt);

        tvTenDG.setText(dg.getTenDG());
        tvMaDG.setText("Mã độc giả: " + dg.getMaDG());
        tvGioiTinh.setText("Giới tính: " + dg.getGioiTinh());
        tvNamSinh.setText("Năm sinh: " + dg.getNamSinh());
        tvLop.setText("Lớp: " + dg.getTenLop());
        tvKhoa.setText("Khoa: " + dg.getTenKhoa());
        tvDiaChi.setText("Địa chỉ: " + dg.getDiaChi());
        tvEmail.setText("Email: " + dg.getEmail());
        tvSdt.setText("SĐT: " + dg.getSdt());

        return view;
    }
}
