package com.example.thuvien.thongke;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.List;

public class TopAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;

    public TopAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_simple, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            holder.tvSubtitle = convertView.findViewById(R.id.tvSubtitle);
            holder.tvDetail = convertView.findViewById(R.id.tvDetail);
            holder.tvBadge = convertView.findViewById(R.id.tvBadge);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String raw = list.get(position);
        String[] parts = raw.split("\\|");

        if (parts.length >= 1) holder.tvTitle.setText(parts[0]);
        if (parts.length >= 2) holder.tvSubtitle.setText(parts[1]);
        if (parts.length >= 3) holder.tvDetail.setText(parts[2]);
        if (parts.length >= 4) holder.tvBadge.setText(parts[3]);

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTitle, tvSubtitle, tvDetail, tvBadge;
    }
}
