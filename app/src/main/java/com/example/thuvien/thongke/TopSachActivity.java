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

public class TopSachActivity extends AppCompatActivity {

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
        tvTitle.setText("Top Sách Mượn Nhiều");

        lvData = findViewById(R.id.lvData);

        list = new ArrayList<>();
        adapter = new TopAdapter(this, R.layout.item_top_sach, list);
        lvData.setAdapter(adapter);

        imgBack.setOnClickListener(v -> finish());

        loadData();
    }

    private void loadData() {
        SQLiteDatabase db = new SQLiteHelper(this).getReadableDatabase();

        String sql = "SELECT s.MaSach, s.TenSach, t.TenTG, SUM(ct.SoLuong) as Tong, s.HinhAnh " +
                "FROM sach s " +
                "JOIN tacgia t ON s.MaTG = t.MaTG " +
                "JOIN chitietmuontra ct ON s.MaSach = ct.MaSach " +
                "GROUP BY s.MaSach " +
                "ORDER BY Tong DESC";

        Cursor c = db.rawQuery(sql, null);

        list.clear();
        while (c.moveToNext()) {
            String info = c.getString(1) + "|" +
                         "Mã: " + c.getString(0) + "|" +
                         "Tác giả: " + c.getString(2) + "|" +
                         c.getInt(3) + "|" +
                         (c.getString(4) != null ? c.getString(4) : "");
            list.add(info);
        }
        c.close();
        adapter.notifyDataSetChanged();
        db.close();
    }
}
