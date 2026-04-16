package com.example.thuvien.kesach;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KeSachQuery {

    private final SQLiteHelper dbHelper;

    public KeSachQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<KeSach> layDanhSachKeSach() {
        List<KeSach> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaViTri, TenKe, MoTa FROM kesach ORDER BY MaViTri ASC",
                null
        );

        while (cursor.moveToNext()) {
            KeSach item = new KeSach();
            item.setMaViTri(cursor.getString(0));
            item.setTenKe(cursor.getString(1));
            item.setMoTa(cursor.getString(2));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public List<KeSach> timKiemKeSach(String keyword) {
        List<KeSach> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String key = "%" + keyword + "%";

        Cursor cursor = db.rawQuery(
                "SELECT MaViTri, TenKe, MoTa FROM kesach WHERE MaViTri LIKE ? OR TenKe LIKE ? ORDER BY MaViTri ASC",
                new String[]{key, key}
        );

        while (cursor.moveToNext()) {
            KeSach item = new KeSach();
            item.setMaViTri(cursor.getString(0));
            item.setTenKe(cursor.getString(1));
            item.setMoTa(cursor.getString(2));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public KeSach layThongTinTheoMa(String maViTri) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaViTri, TenKe, MoTa FROM kesach WHERE MaViTri = ?",
                new String[]{maViTri}
        );

        KeSach item = null;
        if (cursor.moveToFirst()) {
            item = new KeSach();
            item.setMaViTri(cursor.getString(0));
            item.setTenKe(cursor.getString(1));
            item.setMoTa(cursor.getString(2));
        }

        cursor.close();
        db.close();
        return item;
    }

    public String taoMaMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaViTri FROM kesach ORDER BY MaViTri DESC LIMIT 1", null);

        String maMoi = "KS001";
        if (cursor.moveToFirst()) {
            try {
                int so = Integer.parseInt(cursor.getString(0).substring(2)) + 1;
                maMoi = String.format(Locale.getDefault(), "KS%03d", so);
            } catch (Exception e) {
                maMoi = "KS001";
            }
        }

        cursor.close();
        db.close();
        return maMoi;
    }

    public boolean themKeSach(KeSach item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "INSERT INTO kesach (MaViTri, TenKe, MoTa) VALUES (?, ?, ?)",
                    new Object[]{item.getMaViTri(), item.getTenKe(), item.getMoTa()}
            );
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean capNhatKeSach(KeSach item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "UPDATE kesach SET TenKe = ?, MoTa = ? WHERE MaViTri = ?",
                    new Object[]{item.getTenKe(), item.getMoTa(), item.getMaViTri()}
            );
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean xoaKeSach(String maViTri) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int row = db.delete("kesach", "MaViTri = ?", new String[]{maViTri});
            db.close();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean keSachDangDuocSuDung(String maViTri) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean result = false;
        Cursor cursor = db.rawQuery("SELECT 1 FROM sach WHERE MaViTri = ? LIMIT 1", new String[]{maViTri});
        if (cursor.moveToFirst()) {
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }
}
