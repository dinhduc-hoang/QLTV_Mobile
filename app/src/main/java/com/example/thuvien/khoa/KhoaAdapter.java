package com.example.thuvien.khoa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class KhoaAdapter extends BaseAdapter {

    private Context context;
    private List<Khoa> list;

    public KhoaAdapter(Context context, List<Khoa> list) {
        this.context = context;
        this.list = list;
    }

    public void capNhatDuLieu(List<Khoa> newList) {
        this.list = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_khoa, parent, false);
            holder = new ViewHolder();
            holder.tvTenKhoa = convertView.findViewById(R.id.tvTenKhoa);
            holder.tvMaKhoa = convertView.findViewById(R.id.tvMaKhoa);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Khoa item = list.get(position);

        holder.tvTenKhoa.setText(item.getTenKhoa());
        holder.tvMaKhoa.setText("Mã khoa: " + item.getMaKhoa());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTenKhoa, tvMaKhoa;
    }
}
