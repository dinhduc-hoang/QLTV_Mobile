package com.example.thuvien.tacgia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class TacGiaAdapter extends BaseAdapter {

    private Context context;
    private List<TacGia> list;

    public TacGiaAdapter(Context context, List<TacGia> list) {
        this.context = context;
        this.list = list;
    }

    public void capNhatDuLieu(List<TacGia> newList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tacgia, parent, false);
            holder = new ViewHolder();
            holder.tvTenTG = convertView.findViewById(R.id.tvTenTG);
            holder.tvMaTG = convertView.findViewById(R.id.tvMaTG);
            holder.tvNamSinh = convertView.findViewById(R.id.tvNamSinh);
            holder.tvGioiTinh = convertView.findViewById(R.id.tvGioiTinh);
            holder.tvQuocTich = convertView.findViewById(R.id.tvQuocTich);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final TacGia item = list.get(position);
        holder.tvTenTG.setText(item.getTenTG());
        holder.tvMaTG.setText("Mã tác giả: " + item.getMaTG());
        holder.tvNamSinh.setText("Năm sinh: " + item.getNamSinh());
        holder.tvGioiTinh.setText("Giới tính: " + item.getGioiTinh());
        holder.tvQuocTich.setText("Quốc tịch: " + item.getQuocTich());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTenTG, tvMaTG, tvNamSinh, tvGioiTinh, tvQuocTich;
    }
}
