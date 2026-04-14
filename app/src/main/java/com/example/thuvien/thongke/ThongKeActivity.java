package com.example.thuvien.thongke;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.database.SQLiteHelper;

public class ThongKeActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView tvTongDauSach, tvSachDangMuon, tvTongDocGia, tvTongSoLuongSach;
    TextView tvTopDocGia1, tvTopDocGia2, tvTopDocGia3, tvTopSach1, tvTopSach2, tvTopSach3, tvQuaHan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);

        imgBack = findViewById(R.id.imgBack);
        tvTongDauSach = findViewById(R.id.tvTongDauSach);
        tvSachDangMuon = findViewById(R.id.tvSachDangMuon);
        tvTongDocGia = findViewById(R.id.tvTongDocGia);
        tvTongSoLuongSach = findViewById(R.id.tvTongSoLuongSach);
        tvTopDocGia1 = findViewById(R.id.tvTopDocGia1);
        tvTopDocGia2 = findViewById(R.id.tvTopDocGia2);
        tvTopDocGia3 = findViewById(R.id.tvTopDocGia3);
        tvTopSach1 = findViewById(R.id.tvTopSach1);
        tvTopSach2 = findViewById(R.id.tvTopSach2);
        tvTopSach3 = findViewById(R.id.tvTopSach3);
        tvQuaHan = findViewById(R.id.tvQuaHan);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { finish(); }
        });

        loadData();
    }

    private void loadData() {
        SQLiteDatabase db = new SQLiteHelper(this).getReadableDatabase();

        tvTongDauSach.setText(String.valueOf(getCount(db, "SELECT COUNT(*) FROM sach")));
        tvSachDangMuon.setText(String.valueOf(getCount(db, "SELECT COUNT(*) FROM muontra WHERE TrangThai = 'Chưa trả' OR TrangThai = 'Đang mượn'")));
        tvTongDocGia.setText(String.valueOf(getCount(db, "SELECT COUNT(*) FROM docgia")));
        tvTongSoLuongSach.setText(String.valueOf(getCount(db, "SELECT IFNULL(SUM(SoLuong),0) FROM sach")));

        loadTopDocGia(db);
        loadTopSach(db);
        loadQuaHan(db);

        db.close();
    }

    private int getCount(SQLiteDatabase db, String sql) {
        Cursor c = db.rawQuery(sql, null);
        int value = 0;
        if (c.moveToFirst()) { value = c.getInt(0); }
        c.close();
        return value;
    }

    private void loadTopDocGia(SQLiteDatabase db) {
        Cursor c = db.rawQuery(
                "SELECT d.TenDG, COUNT(mt.MaMT) AS TongLuot " +
                        "FROM docgia d LEFT JOIN muontra mt ON d.MaDG = mt.MaDG " +
                        "GROUP BY d.MaDG, d.TenDG " +
                        "ORDER BY TongLuot DESC, d.TenDG ASC LIMIT 3", null);

        String[] rows = new String[]{"Chưa có dữ liệu", "Chưa có dữ liệu", "Chưa có dữ liệu"};
        int i = 0;
        while (c.moveToNext() && i < 3) {
            rows[i] = c.getString(0) + "  •  " + c.getInt(1) + " lượt";
            i++;
        }
        c.close();

        tvTopDocGia1.setText(rows[0]);
        tvTopDocGia2.setText(rows[1]);
        tvTopDocGia3.setText(rows[2]);
    }

    private void loadTopSach(SQLiteDatabase db) {
        Cursor c = db.rawQuery(
                "SELECT s.TenSach, IFNULL(SUM(ct.SoLuong),0) AS TongMuon " +
                        "FROM sach s LEFT JOIN chitietmuontra ct ON s.MaSach = ct.MaSach " +
                        "GROUP BY s.MaSach, s.TenSach " +
                        "ORDER BY TongMuon DESC, s.TenSach ASC LIMIT 3", null);

        String[] rows = new String[]{"Chưa có dữ liệu", "Chưa có dữ liệu", "Chưa có dữ liệu"};
        int i = 0;
        while (c.moveToNext() && i < 3) {
            rows[i] = c.getString(0) + "  •  " + c.getInt(1) + " lượt";
            i++;
        }
        c.close();

        tvTopSach1.setText(rows[0]);
        tvTopSach2.setText(rows[1]);
        tvTopSach3.setText(rows[2]);
    }

    private void loadQuaHan(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM muontra WHERE TrangThai = 'Chưa trả' AND HanTra < date('now')", null);
        int soQuaHan = 0;
        if (c.moveToFirst()) { soQuaHan = c.getInt(0); }
        c.close();

        if (soQuaHan == 0) {
            tvQuaHan.setText("Không có phiếu mượn quá hạn");
        } else {
            tvQuaHan.setText("Có " + soQuaHan + " phiếu mượn quá hạn");
        }
    }
}
