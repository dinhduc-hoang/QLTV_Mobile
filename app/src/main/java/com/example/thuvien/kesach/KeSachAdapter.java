package com.example.thuvien.kesach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class KeSachAdapter extends BaseAdapter {

    private Context context;
    private List<KeSach> list;

    public KeSachAdapter(Context context, List<KeSach> list) {
        this.context = context;
        this.list = list;
    }

    public void capNhatDuLieu(List<KeSach> newList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_kesach, parent, false);
            holder = new ViewHolder();
            holder.tvTenKe = convertView.findViewById(R.id.tvTenKe);
            holder.tvMaViTri = convertView.findViewById(R.id.tvMaViTri);
            holder.tvMoTa = convertView.findViewById(R.id.tvMoTa);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final KeSach item = list.get(position);

        holder.tvTenKe.setText(item.getTenKe());
        holder.tvMaViTri.setText("Mã vị trí: " + item.getMaViTri());
        holder.tvMoTa.setText("Mô tả: " + item.getMoTa());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTenKe, tvMaViTri, tvMoTa;
    }
}
