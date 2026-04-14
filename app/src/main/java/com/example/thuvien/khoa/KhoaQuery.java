package com.example.thuvien.khoa;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KhoaQuery {

    private final SQLiteHelper dbHelper;

    public KhoaQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<Khoa> layDanhSachKhoa() {
        List<Khoa> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaKhoa, TenKhoa FROM khoa ORDER BY MaKhoa ASC",
                null
        );

        while (cursor.moveToNext()) {
            Khoa item = new Khoa();
            item.setMaKhoa(cursor.getString(0));
            item.setTenKhoa(cursor.getString(1));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public Khoa layThongTinTheoMa(String maKhoa) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaKhoa, TenKhoa FROM khoa WHERE MaKhoa = ?",
                new String[]{maKhoa}
        );

        Khoa item = null;
        if (cursor.moveToFirst()) {
            item = new Khoa();
            item.setMaKhoa(cursor.getString(0));
            item.setTenKhoa(cursor.getString(1));
        }

        cursor.close();
        db.close();
        return item;
    }

    public String taoMaMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaKhoa FROM khoa ORDER BY MaKhoa DESC LIMIT 1", null);

        String maMoi = "KH001";
        if (cursor.moveToFirst()) {
            try {
                int so = Integer.parseInt(cursor.getString(0).substring(2)) + 1;
                maMoi = String.format(Locale.getDefault(), "KH%03d", so);
            } catch (Exception e) {
                maMoi = "KH001";
            }
        }

        cursor.close();
        db.close();
        return maMoi;
    }

    public boolean themKhoa(Khoa item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "INSERT INTO khoa (MaKhoa, TenKhoa) VALUES (?, ?)",
                    new Object[]{item.getMaKhoa(), item.getTenKhoa()}
            );
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean capNhatKhoa(Khoa item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "UPDATE khoa SET TenKhoa = ? WHERE MaKhoa = ?",
                    new Object[]{item.getTenKhoa(), item.getMaKhoa()}
            );
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean xoaKhoa(String maKhoa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int row = db.delete("khoa", "MaKhoa = ?", new String[]{maKhoa});
            db.close();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }
}