package com.example.thuvien.muontra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.List;

public class MuonTraAdapter extends BaseAdapter {

    private Context context;
    private int layout_id;
    private List<MuonTra> listMuonTra;

    public MuonTraAdapter(Context context, int layout_id, List<MuonTra> listMuonTra) {
        this.context = context;
        this.layout_id = layout_id;
        this.listMuonTra = listMuonTra;
    }

    @Override
    public int getCount() {
        return listMuonTra.size();
    }

    @Override
    public Object getItem(int position) {
        return listMuonTra.get(position);
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

        MuonTra mt = listMuonTra.get(position);

        TextView tvMaMT = view.findViewById(R.id.tvMaMT);
        TextView tvDocGia = view.findViewById(R.id.tvDocGia);
        TextView tvNhanVien = view.findViewById(R.id.tvNhanVien);
        TextView tvNgayMuon = view.findViewById(R.id.tvNgayMuon);
        TextView tvHanTra = view.findViewById(R.id.tvHanTra);
        TextView tvTrangThai = view.findViewById(R.id.tvTrangThai);

        tvMaMT.setText("Phiếu " + mt.getMaMT());
        tvDocGia.setText("Độc giả: " + mt.getTenDG());
        tvNhanVien.setText("Nhân viên: " + mt.getTenNV());
        tvNgayMuon.setText("Ngày mượn: " + mt.getNgayMuon());
        tvHanTra.setText("Hạn trả: " + mt.getHanTra());
        tvTrangThai.setText(mt.getTrangThai());

        return view;
    }
}
