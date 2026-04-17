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
        TextView tvStatus = convertView.findViewById(R.id.tvStatusLabel);
        TextView tvDaysLeft = convertView.findViewById(R.id.tvDaysLeft);
        android.widget.ImageView imgBook = convertView.findViewById(R.id.imgBook);

        tvBookName.setText(mt.getTenSach() != null ? mt.getTenSach() : "Tên sách chưa cập nhật");
        tvBorrowDate.setText("Ngày mượn: " + mt.getNgayMuon());
        tvDueDate.setText("Hạn trả: " + mt.getHanTra());

        if (imgBook != null) {
            if (mt.getHinhAnh() != null && !mt.getHinhAnh().isEmpty()) {
                java.io.File file = new java.io.File(mt.getHinhAnh());
                if (file.exists()) {
                    imgBook.setImageURI(android.net.Uri.fromFile(file));
                } else {
                    imgBook.setImageResource(R.drawable.ic_book);
                }
            } else {
                imgBook.setImageResource(R.drawable.ic_book);
            }
        }

        if (tvStatus != null) {
            String status = mt.getTrangThai();
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(status);
            tvStatus.setTextColor(context.getResources().getColor(android.R.color.white));

            if ("Đã trả".equals(status)) {
                tvStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(context.getResources().getColor(android.R.color.holo_green_light)));
                if (tvDaysLeft != null) tvDaysLeft.setVisibility(View.GONE);
            } else {
                int colorRes = "Quá hạn".equals(status) ? android.R.color.holo_red_light : R.color.orange_primary;
                tvStatus.setBackgroundTintList(android.content.res.ColorStateList.valueOf(context.getResources().getColor(colorRes)));
                if (tvDaysLeft != null) {
                    tvDaysLeft.setVisibility(View.VISIBLE);
                    tinhSoNgayConLai(mt.getHanTra(), tvDaysLeft);
                }
            }
        }

        return convertView;
    }

    private void tinhSoNgayConLai(String hanTra, TextView tvDaysLeft) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
            java.util.Date dueDate = sdf.parse(hanTra);
            java.util.Date today = sdf.parse(sdf.format(new java.util.Date()));

            if (dueDate != null) {
                long diff = dueDate.getTime() - today.getTime();
                long days = diff / (24 * 60 * 60 * 1000);
                if (days < 0) {
                    tvDaysLeft.setText("Quá hạn " + Math.abs(days) + " ngày");
                    tvDaysLeft.setBackgroundTintList(android.content.res.ColorStateList.valueOf(context.getResources().getColor(android.R.color.holo_red_light)));
                    tvDaysLeft.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    tvDaysLeft.setText("Còn " + days + " ngày");
                }
            }
        } catch (Exception e) {
            tvDaysLeft.setVisibility(View.GONE);
        }
    }
}
