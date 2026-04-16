package com.example.thuvien.lop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class LopAdapter extends BaseAdapter {

    private Context context;
    private List<Lop> list;

    public LopAdapter(Context context, List<Lop> list) {
        this.context = context;
        this.list = list;
    }

    public void capNhatDuLieu(List<Lop> newList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lop, parent, false);
            holder = new ViewHolder();
            holder.tvTenLop = convertView.findViewById(R.id.tvTenLop);
            holder.tvMaLop = convertView.findViewById(R.id.tvMaLop);
            holder.tvKhoa = convertView.findViewById(R.id.tvKhoa);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Lop item = list.get(position);

        holder.tvTenLop.setText(item.getTenLop());
        holder.tvMaLop.setText("Mã lớp: " + item.getMaLop());
        holder.tvKhoa.setText("Khoa: " + item.getTenKhoa());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTenLop, tvMaLop, tvKhoa;
    }
}
