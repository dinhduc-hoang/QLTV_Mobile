package com.example.thuvien.user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;
import com.example.thuvien.sach.Sach;

import java.util.List;

public class UserBookAdapter extends BaseAdapter {

    private Context context;
    private List<Sach> list;

    public UserBookAdapter(Context context, List<Sach> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_book_list, parent, false);
        }

        Sach sach = list.get(position);
        TextView tvBookName = convertView.findViewById(R.id.tvBookName);
        TextView tvAuthorName = convertView.findViewById(R.id.tvAuthorName);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);

        tvBookName.setText(sach.getTenSach());
        tvAuthorName.setText(sach.getTenTG());

        if (sach.getSoLuong() <= 0) {
            tvStatus.setText("Hết sách");
            tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            tvStatus.setText("Còn " + sach.getSoLuong());
            tvStatus.setTextColor(context.getResources().getColor(R.color.orange_primary));
        }

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserBookDetailActivity.class);
            intent.putExtra("MaSach", sach.getMaSach());
            context.startActivity(intent);
        });

        return convertView;
    }
}
