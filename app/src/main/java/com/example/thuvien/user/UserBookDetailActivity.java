package com.example.thuvien.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.muontra.MuonTraQuery;
import com.example.thuvien.sach.Sach;
import com.example.thuvien.sach.SachQuery;

public class UserBookDetailActivity extends AppCompatActivity {

    private ImageView imgBack, imgBook;
    private TextView tvBookName, tvAuthorName, tvCategory, tvStockStatus, tvPublishYear, tvPublisher, tvLanguage;
    private Button btnBorrow;
    private SachQuery sachQuery;
    private MuonTraQuery muonTraQuery;
    private String maSach;
    private Sach currentSach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_detail);

        imgBack = findViewById(R.id.imgBack);
        tvBookName = findViewById(R.id.tvBookName);
        tvAuthorName = findViewById(R.id.tvAuthorName);
        tvCategory = findViewById(R.id.tvCategory);
        tvStockStatus = findViewById(R.id.tvStockStatus);
        tvPublishYear = findViewById(R.id.tvPublishYear);
        tvPublisher = findViewById(R.id.tvPublisher);
        tvLanguage = findViewById(R.id.tvLanguage);
        imgBook = findViewById(R.id.imgBook);
        btnBorrow = findViewById(R.id.btnBorrow);

        sachQuery = new SachQuery(this);
        muonTraQuery = new MuonTraQuery(this);
        maSach = getIntent().getStringExtra("MaSach");

        imgBack.setOnClickListener(v -> finish());

        loadBookDetail();

        btnBorrow.setOnClickListener(v -> {
            android.content.SharedPreferences sp = getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE);
            String maDG = sp.getString("MaDG", "");

            if (!muonTraQuery.kiemTraDocGiaCoTheThuVien(maDG)) {
                Toast.makeText(this, "Bạn cần có thẻ thư viện còn hiệu lực để mượn sách!", Toast.LENGTH_LONG).show();
                return;
            }

            if (muonTraQuery.kiemTraDocGiaDangMuonSach(maDG)) {
                Toast.makeText(this, "Bạn đang mượn sách chưa trả, vui lòng trả sách trước khi mượn mới!", Toast.LENGTH_LONG).show();
                return;
            }

            if (currentSach != null && currentSach.getSoLuong() > 0) {
                Intent intent = new Intent(this, UserBorrowSuccessActivity.class);
                intent.putExtra("MaSach", maSach);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Sách đã hết, không thể mượn!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBookDetail() {
        currentSach = sachQuery.layThongTinSachTheoMa(maSach);

        if (currentSach != null) {
            tvBookName.setText(currentSach.getTenSach());
            tvAuthorName.setText(currentSach.getTenTG() != null ? currentSach.getTenTG() : "Chưa rõ tác giả");

            if (currentSach.getTenTL() != null && !currentSach.getTenTL().isEmpty()) {
                tvCategory.setVisibility(android.view.View.VISIBLE);
                tvCategory.setText(currentSach.getTenTL());
            } else {
                tvCategory.setVisibility(android.view.View.GONE);
            }

            tvStockStatus.setText("Số lượng còn: " + currentSach.getSoLuong());
            tvPublishYear.setText(String.valueOf(currentSach.getNamXB()));
            tvPublisher.setText(currentSach.getTenNXB() != null ? currentSach.getTenNXB() : "Đang cập nhật");
            tvLanguage.setText(currentSach.getTenNN() != null ? currentSach.getTenNN() : "Tiếng Việt");

            if (imgBook != null) {
                if (currentSach.getHinhAnh() != null && !currentSach.getHinhAnh().isEmpty()) {
                    java.io.File file = new java.io.File(currentSach.getHinhAnh());
                    if (file.exists()) {
                        imgBook.setImageURI(android.net.Uri.fromFile(file));
                    } else {
                        imgBook.setImageResource(R.drawable.ic_book);
                    }
                } else {
                    imgBook.setImageResource(R.drawable.ic_book);
                }
            }

            if (currentSach.getSoLuong() <= 0) {
                btnBorrow.setEnabled(false);
                btnBorrow.setText("Hết sách");
                btnBorrow.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
            }
        }
    }
}
