package com.example.thuvien.nhanvien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class NhanVienAdapter extends BaseAdapter {

    private Context context;
    private List<NhanVien> list;

    public NhanVienAdapter(Context context, List<NhanVien> list) {
        this.context = context;
        this.list = list;
    }

    public void capNhatDuLieu(List<NhanVien> newList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nhanvien, parent, false);
            holder = new ViewHolder();
            holder.tvTenNV = convertView.findViewById(R.id.tvTenNV);
            holder.tvMaNV = convertView.findViewById(R.id.tvMaNV);
            holder.tvVaiTro = convertView.findViewById(R.id.tvVaiTro);
            holder.tvGioiTinh = convertView.findViewById(R.id.tvGioiTinh);
            holder.tvNamSinh = convertView.findViewById(R.id.tvNamSinh);
            holder.tvQueQuan = convertView.findViewById(R.id.tvQueQuan);
            holder.tvEmail = convertView.findViewById(R.id.tvEmail);
            holder.tvSdt = convertView.findViewById(R.id.tvSdt);
            holder.tvUser = convertView.findViewById(R.id.tvUser);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final NhanVien item = list.get(position);

        holder.tvTenNV.setText(item.getTenNV());
        holder.tvMaNV.setText("Mã nhân viên: " + item.getMaNV());
        holder.tvVaiTro.setText("Vai trò: " + item.getVaiTro());
        holder.tvGioiTinh.setText("Giới tính: " + item.getGioiTinh());
        holder.tvNamSinh.setText("Năm sinh: " + item.getNamSinh());
        holder.tvQueQuan.setText("Quê quán: " + item.getQueQuan());
        holder.tvEmail.setText("Email: " + item.getEmail());
        holder.tvSdt.setText("SĐT: " + item.getSdt());
        holder.tvUser.setText("User: " + item.getUser());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTenNV, tvMaNV, tvVaiTro, tvGioiTinh, tvNamSinh, tvQueQuan, tvEmail, tvSdt, tvUser;
    }
}
