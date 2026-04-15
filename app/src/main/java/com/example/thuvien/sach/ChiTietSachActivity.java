package com.example.thuvien.sach;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.database.SQLiteHelper;

public class ChiTietSachActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView tvMaSach, tvTenSach, tvTacGia, tvTheLoai, tvNXB, tvNgonNgu, tvKe, tvNamXB, tvSoLuong;
    Button btnSua;

    String maSach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_sach);

        imgBack = findViewById(R.id.imgBack);
        tvMaSach = findViewById(R.id.tvMaSach);
        tvTenSach = findViewById(R.id.tvTenSach);
        tvTacGia = findViewById(R.id.tvTacGia);
        tvTheLoai = findViewById(R.id.tvTheLoai);
        tvNXB = findViewById(R.id.tvNXB);
        tvNgonNgu = findViewById(R.id.tvNgonNgu);
        tvKe = findViewById(R.id.tvKe);
        tvNamXB = findViewById(R.id.tvNamXB);
        tvSoLuong = findViewById(R.id.tvSoLuong);
        btnSua = findViewById(R.id.btnSuaSach);

        maSach = getIntent().getStringExtra("MaSach");

        loadData();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietSachActivity.this, UpdateSachActivity.class);
                intent.putExtra("MaSach", maSach);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT s.MaSach, s.TenSach, s.NamXB, s.SoLuong, " +
                "tg.TenTG, tl.TenTL, nxb.TenNXB, nn.TenNN, ks.TenKe " +
                "FROM sach s " +
                "JOIN tacgia tg ON s.MaTG = tg.MaTG " +
                "JOIN theloai tl ON s.MaTL = tl.MaTL " +
                "JOIN nhaxuatban nxb ON s.MaNXB = nxb.MaNXB " +
                "JOIN ngonngu nn ON s.MaNN = nn.MaNN " +
                "JOIN kesach ks ON s.MaViTri = ks.MaViTri " +
                "WHERE s.MaSach = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{maSach});

        if (cursor.moveToFirst()) {
            tvMaSach.setText(cursor.getString(0));
            tvTenSach.setText(cursor.getString(1));
            tvNamXB.setText("Năm xuất bản: " + cursor.getInt(2));
            tvSoLuong.setText("Số lượng: " + cursor.getInt(3));
            tvTacGia.setText("Tác giả: " + cursor.getString(4));
            tvTheLoai.setText("Thể loại: " + cursor.getString(5));
            tvNXB.setText("Nhà xuất bản: " + cursor.getString(6));
            tvNgonNgu.setText("Ngôn ngữ: " + cursor.getString(7));
            tvKe.setText("Vị trí kệ: " + cursor.getString(8));
        }

        cursor.close();
        db.close();
    }
}
