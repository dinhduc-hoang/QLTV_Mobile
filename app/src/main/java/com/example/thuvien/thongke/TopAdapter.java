package com.example.thuvien.thongke;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thuvien.R;

import java.io.File;
import java.util.List;

public class TopAdapter extends BaseAdapter {

    private Context context;
    private int layout_id;
    private List<String> list;

    public TopAdapter(Context context, int layout_id, List<String> list) {
        this.context = context;
        this.layout_id = layout_id;
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
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout_id, parent, false);
        }

        String raw = list.get(position);
        String[] parts = raw.split("\\|");

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvSubtitle = view.findViewById(R.id.tvSubtitle);
        TextView tvDetail = view.findViewById(R.id.tvDetail);
        TextView tvBadge = view.findViewById(R.id.tvBadge);
        ImageView imgSach = view.findViewById(R.id.imgSach);

        if (parts.length >= 1) tvTitle.setText(parts[0]);
        if (parts.length >= 2) tvSubtitle.setText(parts[1]);
        if (parts.length >= 3) tvDetail.setText(parts[2]);
        if (parts.length >= 4) tvBadge.setText(parts[3]);

        if (imgSach != null) {
            if (parts.length >= 5 && !parts[4].isEmpty()) {
                File file = new File(parts[4]);
                if (file.exists()) {
                    imgSach.setImageURI(Uri.fromFile(file));
                } else {
                    imgSach.setImageResource(R.drawable.ic_book);
                }
            } else {
                imgSach.setImageResource(R.drawable.ic_book);
            }
        }

        return view;
    }
}
