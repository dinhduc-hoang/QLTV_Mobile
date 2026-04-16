package com.example.thuvien.ngonngu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class NgonNguAdapter extends BaseAdapter {

    private Context context;
    private List<NgonNgu> list;

    public NgonNguAdapter(Context context, List<NgonNgu> list) {
        this.context = context;
        this.list = list;
    }

    public void capNhatDuLieu(List<NgonNgu> newList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ngonngu, parent, false);
            holder = new ViewHolder();
            holder.tvTenNN = convertView.findViewById(R.id.tvTenNN);
            holder.tvMaNN = convertView.findViewById(R.id.tvMaNN);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final NgonNgu item = list.get(position);
        holder.tvTenNN.setText(item.getTenNN());
        holder.tvMaNN.setText("Mã ngôn ngữ: " + item.getMaNN());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTenNN, tvMaNN;
    }
}
