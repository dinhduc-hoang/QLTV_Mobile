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
    private List<NXB> list;

    public NXBAdapter(Context context, List<NXB> list) {
        this.context = context;
        this.list = list;
    }

    public void capNhatDuLieu(List<NXB> newList) {
        this.list = newList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nxb, parent, false);
            holder = new ViewHolder();
            holder.tvTen = convertView.findViewById(R.id.tvTen);
            holder.tvSub1 = convertView.findViewById(R.id.tvSub1);
            holder.tvSub2 = convertView.findViewById(R.id.tvSub2);
            holder.tvSub3 = convertView.findViewById(R.id.tvSub3);
            holder.tvSub4 = convertView.findViewById(R.id.tvSub4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final NXB item = list.get(position);
        holder.tvTen.setText(item.getTenNXB());
        holder.tvSub1.setText("Mã: " + item.getMaNXB());
        holder.tvSub2.setText("Email: " + item.getEmail());
        holder.tvSub3.setText("SĐT: " + item.getSdt());
        holder.tvSub4.setText("Địa chỉ: " + item.getDiaChi());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTen, tvSub1, tvSub2, tvSub3, tvSub4;
    }
}
