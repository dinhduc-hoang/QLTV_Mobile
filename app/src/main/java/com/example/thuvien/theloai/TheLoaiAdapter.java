package com.example.thuvien.theloai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class TheLoaiAdapter extends BaseAdapter {

    private Context context;
    private List<TheLoai> list;

    public TheLoaiAdapter(Context context, List<TheLoai> list) {
        this.context = context;
        this.list = list;
    }

    public void capNhatDuLieu(List<TheLoai> newList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_theloai, parent, false);
            holder = new ViewHolder();
            holder.tvTenTL = convertView.findViewById(R.id.tvTenTL);
            holder.tvMaTL = convertView.findViewById(R.id.tvMaTL);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final TheLoai item = list.get(position);
        holder.tvTenTL.setText(item.getTenTL());
        holder.tvMaTL.setText("Mã thể loại: " + item.getMaTL());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTenTL, tvMaTL;
    }
}
