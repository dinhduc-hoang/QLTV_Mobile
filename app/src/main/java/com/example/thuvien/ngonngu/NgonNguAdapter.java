package com.example.thuvien.ngonngu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.List;

public class NgonNguAdapter extends BaseAdapter {

    private Context context;
    private int layout_id;
    private List<NgonNgu> listNgonNgu;

    public NgonNguAdapter(Context context, int layout_id, List<NgonNgu> listNgonNgu) {
        this.context = context;
        this.layout_id = layout_id;
        this.listNgonNgu = listNgonNgu;
    }

    @Override
    public int getCount() {
        return listNgonNgu.size();
    }

    @Override
    public Object getItem(int position) {
        return listNgonNgu.get(position);
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

        NgonNgu ngonNgu = listNgonNgu.get(position);

        TextView tvTenNN = view.findViewById(R.id.tvTenNN);
        TextView tvMaNN = view.findViewById(R.id.tvMaNN);

        tvTenNN.setText(ngonNgu.getTenNN());
        tvMaNN.setText("Mã ngôn ngữ: " + ngonNgu.getMaNN());

        return view;
    }
}
