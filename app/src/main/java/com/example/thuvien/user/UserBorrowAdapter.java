package com.example.thuvien.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuvien.R;
import com.example.thuvien.muontra.MuonTra;
import com.example.thuvien.muontra.MuonTraQuery;
import com.example.thuvien.sach.SachQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UserBorrowAdapter extends RecyclerView.Adapter<UserBorrowAdapter.ViewHolder> {

    private Context context;
    private List<MuonTra> list;
    private SachQuery sachQuery;
    private MuonTraQuery muonTraQuery;

    public UserBorrowAdapter(Context context, List<MuonTra> list) {
        this.context = context;
        this.list = list;
        this.sachQuery = new SachQuery(context);
        this.muonTraQuery = new MuonTraQuery(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_borrow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MuonTra mt = list.get(position);
        
        // Get book info for this borrow
        List<String> books = muonTraQuery.layChiTietSachTrongPhieu(mt.getMaMT());
        if (!books.isEmpty()) {
            // Simplified: just show first book name
            String firstBook = books.get(0).split(" - ")[0];
            holder.tvBookName.setText(firstBook);
        }

        holder.tvBorrowDate.setText("Ngày mượn: " + mt.getNgayMuon());
        holder.tvDueDate.setText("Hạn trả: " + mt.getHanTra());

        if (mt.getTrangThai().equals("Đã trả")) {
            holder.tvStatusLabel.setVisibility(View.VISIBLE);
            holder.tvStatusLabel.setText("Đã trả");
            holder.tvStatusLabel.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.tvStatusLabel.getBackground().setTint(0xFFE8F5E9);
            holder.tvDaysLeft.setVisibility(View.GONE);
        } else {
            holder.tvStatusLabel.setVisibility(View.GONE);
            holder.tvDaysLeft.setVisibility(View.VISIBLE);
            
            // Calculate days left
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date dueDate = sdf.parse(mt.getHanTra());
                Date today = new Date();
                
                long diff = dueDate.getTime() - today.getTime();
                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                
                if (days < 0) {
                    holder.tvDaysLeft.setText("Quá hạn " + Math.abs(days) + " ngày");
                    holder.tvDaysLeft.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                    holder.tvDaysLeft.getBackground().setTint(0xFFFFEBEE);
                } else {
                    holder.tvDaysLeft.setText("Còn " + days + " ngày");
                    holder.tvDaysLeft.setTextColor(0xFFFF8F00);
                    holder.tvDaysLeft.getBackground().setTint(0xFFFFF8E1);
                }
            } catch (Exception e) {
                holder.tvDaysLeft.setText("N/A");
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookName, tvBorrowDate, tvDueDate, tvStatusLabel, tvDaysLeft;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvBorrowDate = itemView.findViewById(R.id.tvBorrowDate);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvStatusLabel = itemView.findViewById(R.id.tvStatusLabel);
            tvDaysLeft = itemView.findViewById(R.id.tvDaysLeft);
        }
    }
}
