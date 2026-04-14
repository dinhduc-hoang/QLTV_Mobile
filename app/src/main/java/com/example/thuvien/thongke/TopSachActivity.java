package com.example.thuvien.thongke;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuvien.R;
import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class TopSachActivity extends AppCompatActivity {

    RecyclerView rvData;
    TopAdapter adapter;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        rvData = findViewById(R.id.rvData);
        rvData.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new TopAdapter(list);
        rvData.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        SQLiteDatabase db = new SQLiteHelper(this).getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT s.TenSach, SUM(ct.SoLuong) " +
                        "FROM sach s " +
                        "JOIN chitietmuontra ct ON s.MaSach = ct.MaSach " +
                        "GROUP BY s.MaSach " +
                        "ORDER BY SUM(ct.SoLuong) DESC", null);

        list.clear();
        while (c.moveToNext()) {
            list.add(c.getString(0) + " - " + c.getInt(1) + " lượt");
        }
        c.close();

        adapter.notifyDataSetChanged();
        db.close();
    }
}