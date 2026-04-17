package com.example.thuvien.thongke;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class TopDocGiaActivity extends AppCompatActivity {

    ListView lvData;
    TopAdapter adapter;
    List<String> list;
    TextView tvTitle;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke_list);

        tvTitle = findViewById(R.id.tvTitle);
        imgBack = findViewById(R.id.imgBack);
        tvTitle.setText("Top Độc Giả Mượn Sách");

        lvData = findViewById(R.id.lvData);

        list = new ArrayList<>();
        adapter = new TopAdapter(this, R.layout.item_simple, list);
        lvData.setAdapter(adapter);

        imgBack.setOnClickListener(v -> finish());

        loadData();
    }

    private void loadData() {
        SQLiteDatabase db = new SQLiteHelper(this).getReadableDatabase();

        String sql = "SELECT d.MaDG, d.TenDG, k.TenKhoa, l.TenLop, COUNT(m.MaMT) as SoLuot " +
                "FROM docgia d " +
                "JOIN khoa k ON d.MaKhoa = k.MaKhoa " +
                "JOIN lop l ON d.MaLop = l.MaLop " +
                "JOIN muontra m ON d.MaDG = m.MaDG " +
                "GROUP BY d.MaDG " +
                "ORDER BY SoLuot DESC";

        Cursor c = db.rawQuery(sql, null);

        list.clear();
        while (c.moveToNext()) {
            String info = c.getString(1) + "|" +
                         "Mã ĐG: " + c.getString(0) + "|" +
                         "Khoa: " + c.getString(2) + " - Lớp: " + c.getString(3) + "|" +
                         c.getInt(4);
            list.add(info);
        }
        c.close();
        adapter.notifyDataSetChanged();
        db.close();
    }
}
