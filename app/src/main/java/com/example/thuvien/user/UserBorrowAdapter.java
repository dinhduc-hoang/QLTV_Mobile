package com.example.thuvien.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;
import com.example.thuvien.muontra.MuonTra;

import java.util.List;

public class UserBorrowAdapter extends BaseAdapter {

    private Context context;
    private List<MuonTra> list;

    public UserBorrowAdapter(Context context, List<MuonTra> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user_borrow, parent, false);
        }

        MuonTra mt = list.get(position);

        TextView tvBookName = convertView.findViewById(R.id.tvBookName);
        TextView tvBorrowDate = convertView.findViewById(R.id.tvBorrowDate);
        TextView tvDueDate = convertView.findViewById(R.id.tvDueDate);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);

        tvBookName.setText(mt.getTenSach());
        tvBorrowDate.setText("Ngày mượn: " + mt.getNgayMuon());
        tvDueDate.setText("Hạn trả: " + mt.getNgayHenTra());

        if (mt.getNgayThucTra() != null && !mt.getNgayThucTra().isEmpty()) {
            tvStatus.setText("Đã trả (" + mt.getNgayThucTra() + ")");
            tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvStatus.setText("Đang mượn");
            tvStatus.setTextColor(context.getResources().getColor(R.color.orange_primary));
        }

        return convertView;
    }
}
