package com.example.thuvien.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.thuvien.R;
import com.example.thuvien.sach.Sach;
import com.example.thuvien.sach.SachQuery;

import java.util.List;

public class UserHomeActivity extends AppCompatActivity {

    private TextView tvWelcome, btnLogoutHome;
    private LinearLayout lnFeaturedBooks;
    private CardView cardBookList, cardCurrentBorrowing, cardHistory, cardProfile;
    private SachQuery sachQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogoutHome = findViewById(R.id.btnLogoutHome);
        lnFeaturedBooks = findViewById(R.id.lnFeaturedBooks);
        cardBookList = findViewById(R.id.cardBookList);
        cardCurrentBorrowing = findViewById(R.id.cardCurrentBorrowing);
        cardHistory = findViewById(R.id.cardHistory);
        cardProfile = findViewById(R.id.cardProfile);

        sachQuery = new SachQuery(this);

        SharedPreferences sp = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String tenDG = sp.getString("TenDG", "Người dùng");
        tvWelcome.setText("Xin chào, " + tenDG + "!");

        setupFeaturedBooks();

        btnLogoutHome.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, com.example.thuvien.login.LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        cardBookList.setOnClickListener(v -> startActivity(new Intent(this, UserBookListActivity.class)));
        cardCurrentBorrowing.setOnClickListener(v -> startActivity(new Intent(this, UserCurrentBorrowingActivity.class)));
        cardHistory.setOnClickListener(v -> startActivity(new Intent(this, UserBorrowHistoryActivity.class)));
        cardProfile.setOnClickListener(v -> startActivity(new Intent(this, UserProfileActivity.class)));
    }

    private void setupFeaturedBooks() {
        List<Sach> featuredBooks = sachQuery.layDanhSachSachMuonNhieu();
        if (featuredBooks.size() > 5) {
            featuredBooks = featuredBooks.subList(0, 5);
        }

        lnFeaturedBooks.removeAllViews();
        for (Sach sach : featuredBooks) {
            View view = getLayoutInflater().inflate(R.layout.item_user_book_featured, lnFeaturedBooks, false);
            TextView tvBookName = view.findViewById(R.id.tvBookName);
            TextView tvAuthorName = view.findViewById(R.id.tvAuthorName);

            tvBookName.setText(sach.getTenSach());
            tvAuthorName.setText(sach.getTenTG());

            view.setOnClickListener(v -> {
                Intent intent = new Intent(this, UserBookDetailActivity.class);
                intent.putExtra("MaSach", sach.getMaSach());
                startActivity(intent);
            });

            lnFeaturedBooks.addView(view);
        }
    }
}
