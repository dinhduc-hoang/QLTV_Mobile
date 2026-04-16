package com.example.thuvien.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.muontra.MuonTraQuery;
import com.example.thuvien.sach.Sach;
import com.example.thuvien.sach.SachQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserBorrowSuccessActivity extends AppCompatActivity {

    private TextView tvBookName, tvAuthorName, tvBorrowDate, tvDueDate;
    private Button btnOk;
    private SachQuery sachQuery;
    private MuonTraQuery muonTraQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_borrow_success);

        tvBookName = findViewById(R.id.tvBookName);
        tvAuthorName = findViewById(R.id.tvAuthorName);
        tvBorrowDate = findViewById(R.id.tvBorrowDate);
        tvDueDate = findViewById(R.id.tvDueDate);
        btnOk = findViewById(R.id.btnOk);

        sachQuery = new SachQuery(this);
        muonTraQuery = new MuonTraQuery(this);
        String maSach = getIntent().getStringExtra("MaSach");

        android.content.SharedPreferences sp = getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE);
        String maDG = sp.getString("MaDG", "");

        Sach sach = sachQuery.layThongTinSachTheoMa(maSach);
        if (sach != null) {
            tvBookName.setText(sach.getTenSach());
            tvAuthorName.setText(sach.getTenTG());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date now = new Date();
        String ngayMuon = sdf.format(now);
        tvBorrowDate.setText(ngayMuon);

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, 14); // 14 days borrow period
        String hanTra = sdf.format(cal.getTime());
        tvDueDate.setText(hanTra);

        if (maDG != null && !maDG.isEmpty() && maSach != null) {
            String maMT = muonTraQuery.taoMaMuonTraMoi();
            
            List<String> listMaSach = new ArrayList<>();
            listMaSach.add(maSach);
            List<Integer> listSoLuong = new ArrayList<>();
            listSoLuong.add(1);

            muonTraQuery.themPhieuMuonVaChiTiet(maMT, maDG, "NV001", ngayMuon, hanTra, listMaSach, listSoLuong);
        }

        btnOk.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
