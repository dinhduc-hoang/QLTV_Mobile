package com.example.thuvien.theloai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.List;

public class TheLoaiAdapter extends BaseAdapter {

    private Context context;
    private int layout_id;
    private List<TheLoai> listTheLoai;

    public TheLoaiAdapter(Context context, int layout_id, List<TheLoai> listTheLoai) {
        this.context = context;
        this.layout_id = layout_id;
        this.listTheLoai = listTheLoai;
    }

    @Override
    public int getCount() {
        return listTheLoai.size();
    }

    @Override
    public Object getItem(int position) {
        return listTheLoai.get(position);
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

        TheLoai tl = listTheLoai.get(position);

        TextView tvTenTL = view.findViewById(R.id.tvTenTL);
        TextView tvMaTL = view.findViewById(R.id.tvMaTL);

        tvTenTL.setText(tl.getTenTL());
        tvMaTL.setText("Mã thể loại: " + tl.getMaTL());

        return view;
    }
}
