package com.example.thuvien.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.muontra.MuonTra;
import com.example.thuvien.muontra.MuonTraQuery;

import java.util.ArrayList;
import java.util.List;

public class UserCurrentBorrowingActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TextView tvCount;
    private ListView lvCurrentBorrowing;
    private MuonTraQuery muonTraQuery;
    private UserBorrowAdapter adapter;
    private List<MuonTra> listMuonTra = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_current_borrowing);

        imgBack = findViewById(R.id.imgBack);
        tvCount = findViewById(R.id.tvCount);
        lvCurrentBorrowing = findViewById(R.id.lvCurrentBorrowing);

        muonTraQuery = new MuonTraQuery(this);
        imgBack.setOnClickListener(v -> finish());

        adapter = new UserBorrowAdapter(this, listMuonTra);
        lvCurrentBorrowing.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        muonTraQuery.tuDongCapNhatTrangThaiQuaHan();
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String maDG = sp.getString("MaDG", "");

        listMuonTra.clear();
        // Lấy cả 'Chưa trả' và 'Quá hạn' vì cả hai đều là đang mượn chưa trả
        List<MuonTra> chuTra = muonTraQuery.layDanhSachMuonTraTheoDocGia(maDG, "Chưa trả");
        List<MuonTra> quaHan = muonTraQuery.layDanhSachMuonTraTheoDocGia(maDG, "Quá hạn");
        
        listMuonTra.addAll(chuTra);
        listMuonTra.addAll(quaHan);
        
        tvCount.setText("Đang mượn (" + listMuonTra.size() + ")");
        adapter.notifyDataSetChanged();
    }
}
