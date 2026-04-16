package com.example.thuvien.lop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.List;

public class LopAdapter extends BaseAdapter {

    private Context context;
    private int layout_id;
    private List<Lop> listLop;

    public LopAdapter(Context context, int layout_id, List<Lop> listLop) {
        this.context = context;
        this.layout_id = layout_id;
        this.listLop = listLop;
    }

    @Override
    public int getCount() {
        return listLop.size();
    }

    @Override
    public Object getItem(int position) {
        return listLop.get(position);
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

        Lop lop = listLop.get(position);

        TextView tvTenLop = view.findViewById(R.id.tvTenLop);
        TextView tvMaLop = view.findViewById(R.id.tvMaLop);
        TextView tvKhoa = view.findViewById(R.id.tvKhoa);

        tvTenLop.setText(lop.getTenLop());
        tvMaLop.setText("Mã lớp: " + lop.getMaLop());
        tvKhoa.setText("Khoa: " + lop.getTenKhoa());

        return view;
    }
}
