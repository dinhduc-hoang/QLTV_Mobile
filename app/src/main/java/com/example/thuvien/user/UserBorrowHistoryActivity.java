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

public class UserBorrowHistoryActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TextView btnFilterAll, btnFilterReturned, btnFilterOverdue;
    private ListView lvHistory;
    private MuonTraQuery muonTraQuery;
    private UserBorrowAdapter adapter;
    private List<MuonTra> listHistory = new ArrayList<>();
    private String currentFilter = "Tất cả";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_borrow_history);

        imgBack = findViewById(R.id.imgBack);
        btnFilterAll = findViewById(R.id.btnFilterAll);
        btnFilterReturned = findViewById(R.id.btnFilterReturned);
        btnFilterOverdue = findViewById(R.id.btnFilterOverdue);
        lvHistory = findViewById(R.id.lvHistory);

        muonTraQuery = new MuonTraQuery(this);
        imgBack.setOnClickListener(v -> finish());

        adapter = new UserBorrowAdapter(this, listHistory);
        lvHistory.setAdapter(adapter);

        setupFilters();
        loadData();
    }

    private void setupFilters() {
        btnFilterAll.setOnClickListener(v -> updateFilter("Tất cả"));
        btnFilterReturned.setOnClickListener(v -> updateFilter("Đã trả"));
        btnFilterOverdue.setOnClickListener(v -> updateFilter("Quá hạn"));
        updateFilterUI("Tất cả");
    }

    private void updateFilter(String filter) {
        if (currentFilter.equals(filter)) return;
        currentFilter = filter;
        updateFilterUI(filter);
        loadData();
    }

    private void updateFilterUI(String filter) {
        btnFilterAll.setBackgroundTintList(getColorStateList(R.color.line_soft));
        btnFilterAll.setTextColor(getResources().getColor(R.color.text_gray));
        btnFilterReturned.setBackgroundTintList(getColorStateList(R.color.line_soft));
        btnFilterReturned.setTextColor(getResources().getColor(R.color.text_gray));
        btnFilterOverdue.setBackgroundTintList(getColorStateList(R.color.line_soft));
        btnFilterOverdue.setTextColor(getResources().getColor(R.color.text_gray));

        TextView activeBtn = btnFilterAll;
        if (filter.equals("Đã trả")) activeBtn = btnFilterReturned;
        else if (filter.equals("Quá hạn")) activeBtn = btnFilterOverdue;

        activeBtn.setBackgroundTintList(getColorStateList(R.color.orange_primary));
        activeBtn.setTextColor(getResources().getColor(R.color.white));
    }

    private void loadData() {
        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String maDG = sp.getString("MaDG", "");

        listHistory.clear();
        listHistory.addAll(muonTraQuery.layDanhSachMuonTraTheoDocGia(maDG, currentFilter));
        adapter.notifyDataSetChanged();
    }
}
