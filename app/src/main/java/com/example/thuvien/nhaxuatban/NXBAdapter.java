package com.example.thuvien.nhaxuatban;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.List;

public class NXBAdapter extends BaseAdapter {

    private Context context;
    private int layout_id;
    private List<NXB> listNXB;

    public NXBAdapter(Context context, int layout_id, List<NXB> listNXB) {
        this.context = context;
        this.layout_id = layout_id;
        this.listNXB = listNXB;
    }

    @Override
    public int getCount() {
        return listNXB.size();
    }

    @Override
    public Object getItem(int position) {
        return listNXB.get(position);
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

        NXB nxb = listNXB.get(position);

        TextView tvTen = view.findViewById(R.id.tvTen);
        TextView tvSub1 = view.findViewById(R.id.tvSub1);
        TextView tvSub2 = view.findViewById(R.id.tvSub2);
        TextView tvSub3 = view.findViewById(R.id.tvSub3);
        TextView tvSub4 = view.findViewById(R.id.tvSub4);

        tvTen.setText(nxb.getTenNXB());
        tvSub1.setText("Mã: " + nxb.getMaNXB());
        tvSub2.setText("Email: " + nxb.getEmail());
        tvSub3.setText("SĐT: " + nxb.getSdt());
        tvSub4.setText("Địa chỉ: " + nxb.getDiaChi());

        return view;
    }
}
