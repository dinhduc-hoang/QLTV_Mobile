package com.example.thuvien.theloai;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TheLoaiQuery {

    private final SQLiteHelper dbHelper;

    public TheLoaiQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<TheLoai> layDanhSachTheLoai() {
        List<TheLoai> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaTL, TenTL FROM theloai ORDER BY MaTL ASC",
                null
        );

        while (cursor.moveToNext()) {
            TheLoai item = new TheLoai();
            item.setMaTL(cursor.getString(0));
            item.setTenTL(cursor.getString(1));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public TheLoai layThongTinTheoMa(String maTL) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaTL, TenTL FROM theloai WHERE MaTL = ?",
                new String[]{maTL}
        );

        TheLoai item = null;
        if (cursor.moveToFirst()) {
            item = new TheLoai();
            item.setMaTL(cursor.getString(0));
            item.setTenTL(cursor.getString(1));
        }

        cursor.close();
        db.close();
        return item;
    }

    public String taoMaMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaTL FROM theloai ORDER BY MaTL DESC LIMIT 1", null);

        String maMoi = "TL001";
        if (cursor.moveToFirst()) {
            try {
                int so = Integer.parseInt(cursor.getString(0).substring(2)) + 1;
                maMoi = String.format(Locale.getDefault(), "TL%03d", so);
            } catch (Exception e) {
                maMoi = "TL001";
            }
        }

        cursor.close();
        db.close();
        return maMoi;
    }

    public boolean themTheLoai(TheLoai item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "INSERT INTO theloai (MaTL, TenTL) VALUES (?, ?)",
                    new Object[]{item.getMaTL(), item.getTenTL()}
            );
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean capNhatTheLoai(TheLoai item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "UPDATE theloai SET TenTL = ? WHERE MaTL = ?",
                    new Object[]{item.getTenTL(), item.getMaTL()}
            );
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean xoaTheLoai(String maTL) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int row = db.delete("theloai", "MaTL = ?", new String[]{maTL});
            db.close();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }
}
