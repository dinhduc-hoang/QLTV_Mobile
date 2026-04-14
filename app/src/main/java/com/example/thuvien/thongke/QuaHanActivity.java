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

public class QuaHanActivity extends AppCompatActivity {

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
                "SELECT MaMT, HanTra FROM muontra " +
                        "WHERE TrangThai = 'Chưa trả' AND " +
                        "date(substr(HanTra,7,4)||'-'||substr(HanTra,4,2)||'-'||substr(HanTra,1,2)) < date('now','localtime')",
                null);

        list.clear();

        if (c.getCount() == 0) {
            list.add("Không có phiếu mượn quá hạn");
        } else {
            while (c.moveToNext()) {
                list.add("Phiếu " + c.getString(0) + " - Hạn: " + c.getString(1));
            }
        }

        c.close();
        adapter.notifyDataSetChanged();
        db.close();
    }
}