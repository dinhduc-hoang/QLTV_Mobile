package com.example.thuvien.sach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;
import com.example.thuvien.R;

import java.util.List;

public class SachAdapter extends BaseAdapter {

    private Context context;
    private int layout_id;
    private List<Sach> listSach;

    public SachAdapter(Context context, int layout_id, List<Sach> listSach) {
        this.context = context;
        this.layout_id = layout_id;
        this.listSach = listSach;
    }

    @Override
    public int getCount() {
        return listSach.size();
    }

    @Override
    public Object getItem(int position) {
        return listSach.get(position);
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
            view = inflater.inflate(layout_id, parent, false);
        }

        Sach sach = listSach.get(position);

        TextView tvTenSach = view.findViewById(R.id.tvTenSach);
        TextView tvMaSach = view.findViewById(R.id.tvMaSach);
        TextView tvTacGia = view.findViewById(R.id.tvTacGia);
        TextView tvTheLoai = view.findViewById(R.id.tvTheLoai);
        TextView tvNXB = view.findViewById(R.id.tvNXB);
        TextView tvNgonNgu = view.findViewById(R.id.tvNgonNgu);
        TextView tvViTri = view.findViewById(R.id.tvViTri);
        TextView tvNamXB = view.findViewById(R.id.tvNamXB);
        TextView tvSoLuong = view.findViewById(R.id.tvSoLuong);
        ImageView imgSach = view.findViewById(R.id.imgSach);

        if (tvTenSach != null) tvTenSach.setText(sach.getTenSach());
        if (tvMaSach != null) tvMaSach.setText("Mã sách: " + sach.getMaSach());
        if (tvTacGia != null) tvTacGia.setText("Tác giả: " + sach.getTenTG());
        if (tvTheLoai != null) tvTheLoai.setText("Thể loại: " + sach.getTenTL());
        if (tvNXB != null) tvNXB.setText("Nhà xuất bản: " + sach.getTenNXB());
        if (tvNgonNgu != null) tvNgonNgu.setText("Ngôn ngữ: " + sach.getTenNN());
        if (tvViTri != null) tvViTri.setText("Kệ sách: " + sach.getTenViTri());
        if (tvNamXB != null) tvNamXB.setText("Năm XB: " + sach.getNamXB());
        if (tvSoLuong != null) tvSoLuong.setText("SL: " + sach.getSoLuong());

        if (imgSach != null) {
            if (sach.getHinhAnh() != null && !sach.getHinhAnh().isEmpty()) {
                File file = new File(sach.getHinhAnh());
                if (file.exists()) {
                    imgSach.setImageURI(Uri.fromFile(file));
                } else {
                    imgSach.setImageResource(R.drawable.ic_book);
                }
            } else {
                imgSach.setImageResource(R.drawable.ic_book);
            }
        }

        return view;
    }
}
