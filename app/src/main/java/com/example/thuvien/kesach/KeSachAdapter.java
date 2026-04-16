package com.example.thuvien.kesach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.List;

public class KeSachAdapter extends BaseAdapter {

    private Context context;
    private int layout_id;
    private List<KeSach> listKeSach;

    public KeSachAdapter(Context context, int layout_id, List<KeSach> listKeSach) {
        this.context = context;
        this.layout_id = layout_id;
        this.listKeSach = listKeSach;
    }

    @Override
    public int getCount() {
        return listKeSach.size();
    }

    @Override
    public Object getItem(int position) {
        return listKeSach.get(position);
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

        KeSach keSach = listKeSach.get(position);

        TextView tvTenKe = view.findViewById(R.id.tvTenKe);
        TextView tvMaViTri = view.findViewById(R.id.tvMaViTri);
        TextView tvMoTa = view.findViewById(R.id.tvMoTa);

        tvTenKe.setText(keSach.getTenKe());
        tvMaViTri.setText("Mã vị trí: " + keSach.getMaViTri());
        tvMoTa.setText("Mô tả: " + keSach.getMoTa());

        return view;
    }
}
