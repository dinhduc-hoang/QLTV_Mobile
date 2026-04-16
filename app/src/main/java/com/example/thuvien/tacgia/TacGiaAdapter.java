package com.example.thuvien.tacgia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.List;

public class TacGiaAdapter extends BaseAdapter {

    private Context context;
    private int layout_id;
    private List<TacGia> listTacGia;

    public TacGiaAdapter(Context context, int layout_id, List<TacGia> listTacGia) {
        this.context = context;
        this.layout_id = layout_id;
        this.listTacGia = listTacGia;
    }

    @Override
    public int getCount() {
        return listTacGia.size();
    }

    @Override
    public Object getItem(int position) {
        return listTacGia.get(position);
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

        TacGia tg = listTacGia.get(position);

        TextView tvTenTG = view.findViewById(R.id.tvTenTG);
        TextView tvMaTG = view.findViewById(R.id.tvMaTG);
        TextView tvNamSinh = view.findViewById(R.id.tvNamSinh);
        TextView tvGioiTinh = view.findViewById(R.id.tvGioiTinh);
        TextView tvQuocTich = view.findViewById(R.id.tvQuocTich);

        tvTenTG.setText(tg.getTenTG());
        tvMaTG.setText("Mã tác giả: " + tg.getMaTG());
        tvNamSinh.setText("Năm sinh: " + tg.getNamSinh());
        tvGioiTinh.setText("Giới tính: " + tg.getGioiTinh());
        tvQuocTich.setText("Quốc tịch: " + tg.getQuocTich());

        return view;
    }
}
