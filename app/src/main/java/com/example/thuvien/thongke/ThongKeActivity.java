package com.example.thuvien.thongke;

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

public class ThongKeActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView tvTongDauSach, tvSachDangMuon, tvTongDocGia, tvTongSoLuongSach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);

        imgBack = findViewById(R.id.imgBack);
        tvTongDauSach = findViewById(R.id.tvTongDauSach);
        tvSachDangMuon = findViewById(R.id.tvSachDangMuon);
        tvTongDocGia = findViewById(R.id.tvTongDocGia);
        tvTongSoLuongSach = findViewById(R.id.tvTongSoLuongSach);


        Button btnTopDocGia = findViewById(R.id.btnTopDocGia);
        Button btnTopSach = findViewById(R.id.btnTopSach);
        Button btnQuaHan = findViewById(R.id.btnQuaHan);

        btnTopDocGia.setOnClickListener(v ->
                startActivity(new Intent(this, TopDocGiaActivity.class)));

        btnTopSach.setOnClickListener(v ->
                startActivity(new Intent(this, TopSachActivity.class)));

        btnQuaHan.setOnClickListener(v ->
                startActivity(new Intent(this, QuaHanActivity.class)));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { finish(); }
        });

        loadData();
    }

    private void loadData() {
        SQLiteDatabase db = new SQLiteHelper(this).getReadableDatabase();

        tvTongDauSach.setText(String.valueOf(getCount(db,"SELECT COUNT(*) FROM sach")));

        tvTongSoLuongSach.setText(String.valueOf(getCount(db,"SELECT IFNULL(SUM(SoLuong),0) FROM sach")));

        tvTongDocGia.setText(String.valueOf(getCount(db,"SELECT COUNT(*) FROM docgia")));

        tvSachDangMuon.setText(String.valueOf(getCount(db,
                "SELECT IFNULL(SUM(ct.SoLuong),0) " +
                        "FROM chitietmuontra ct " +
                        "JOIN muontra mt ON ct.MaMT = mt.MaMT " +
                        "WHERE mt.TrangThai = 'Chưa trả'"
        )));

        db.close();
    }

    private int getCount(SQLiteDatabase db, String sql) {
        Cursor c = db.rawQuery(sql, null);
        int value = 0;
        if (c.moveToFirst()) {
            value = c.getInt(0);
        }
        c.close();
        return value;
    }
}