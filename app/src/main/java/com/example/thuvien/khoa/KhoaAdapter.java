package com.example.thuvien.khoa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.List;

public class KhoaAdapter extends BaseAdapter {

    private Context context;
    private int layout_id;
    private List<Khoa> listKhoa;

    public KhoaAdapter(Context context, int layout_id, List<Khoa> listKhoa) {
        this.context = context;
        this.layout_id = layout_id;
        this.listKhoa = listKhoa;
    }

    @Override
    public int getCount() {
        return listKhoa.size();
    }

    @Override
    public Object getItem(int position) {
        return listKhoa.get(position);
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

        Khoa khoa = listKhoa.get(position);

        TextView tvTenKhoa = view.findViewById(R.id.tvTenKhoa);
        TextView tvMaKhoa = view.findViewById(R.id.tvMaKhoa);

        tvTenKhoa.setText(khoa.getTenKhoa());
        tvMaKhoa.setText("Mã khoa: " + khoa.getMaKhoa());

        return view;
    }
}
