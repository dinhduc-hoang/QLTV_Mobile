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

public class QuaHanActivity extends AppCompatActivity {

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
        tvTitle.setText("Phiếu Mượn Quá Hạn");

        lvData = findViewById(R.id.lvData);

        list = new ArrayList<>();
        adapter = new TopAdapter(this, list);
        lvData.setAdapter(adapter);

        imgBack.setOnClickListener(v -> finish());

        loadData();
    }

    private void loadData() {
        SQLiteDatabase db = new SQLiteHelper(this).getReadableDatabase();

        String sql = "SELECT * FROM (" +
                "  SELECT m.MaMT, d.TenDG, m.HanTra, " +
                "  CAST(julianday('now', 'start of day') - julianday(substr(m.HanTra, 7, 4) || '-' || substr(m.HanTra, 4, 2) || '-' || substr(m.HanTra, 1, 2)) AS INTEGER) as SoNgay " +
                "  FROM muontra m " +
                "  JOIN docgia d ON m.MaDG = d.MaDG " +
                "  WHERE m.TrangThai = 'Quá hạn'" +
                ") WHERE SoNgay > 0 ORDER BY SoNgay DESC";

        Cursor c = db.rawQuery(sql, null);

        list.clear();
        while (c.moveToNext()) {
            String info = c.getString(1) + "|" +
                         "Mã phiếu: " + c.getString(0) + "|" +
                         "Hạn trả: " + c.getString(2) + "|" +
                         c.getInt(3);
            list.add(info);
        }
        c.close();
        adapter.notifyDataSetChanged();
        db.close();
    }
}
