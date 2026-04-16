package com.example.thuvien.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuvien.R;
import com.example.thuvien.muontra.MuonTra;
import com.example.thuvien.muontra.MuonTraQuery;

import java.util.List;

public class UserCurrentBorrowingActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TextView tvCount;
    private RecyclerView rvCurrentBorrowing;
    private MuonTraQuery muonTraQuery;
    private UserBorrowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_current_borrowing);

        imgBack = findViewById(R.id.imgBack);
        tvCount = findViewById(R.id.tvCount);
        rvCurrentBorrowing = findViewById(R.id.rvCurrentBorrowing);

        muonTraQuery = new MuonTraQuery(this);
        imgBack.setOnClickListener(v -> finish());

        loadData();
    }

    private void loadData() {
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String maDG = sp.getString("MaDG", "");

        List<MuonTra> list = muonTraQuery.layDanhSachMuonTraTheoDocGia(maDG, "Chưa trả");
        tvCount.setText("Đang mượn (" + list.size() + ")");

        rvCurrentBorrowing.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserBorrowAdapter(this, list);
        rvCurrentBorrowing.setAdapter(adapter);
    }
}
