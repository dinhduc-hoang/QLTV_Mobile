package com.BTCK.qltv.theloai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.BTCK.qltv.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class TheLoaiQuery {

    private final SQLiteHelper dbHelper;

    public TheLoaiQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<TheLoai> layDanhSachTheLoai() {
        List<TheLoai> listTheLoai = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaTL, TenTL FROM theloai ORDER BY MaTL",
                null
        );

        while (cursor.moveToNext()) {
            TheLoai theLoai = new TheLoai();
            theLoai.setMaTL(cursor.getString(0));
            theLoai.setTenTL(cursor.getString(1));
            listTheLoai.add(theLoai);
        }

        cursor.close();
        return listTheLoai;
    }

    public boolean themTheLoai(TheLoai theLoai) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaTL", theLoai.getMaTL());
        values.put("TenTL", theLoai.getTenTL());

        long row = db.insert("theloai", null, values);
        return row != -1;
    }

    public boolean suaTheLoai(String maCu, TheLoai theLoai) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaTL", theLoai.getMaTL());
        values.put("TenTL", theLoai.getTenTL());

        int row = db.update("theloai", values, "MaTL = ?", new String[]{maCu});
        return row > 0;
    }

    public boolean xoaTheLoai(String maTL) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete("theloai", "MaTL = ?", new String[]{maTL});
        return row > 0;
    }
}
