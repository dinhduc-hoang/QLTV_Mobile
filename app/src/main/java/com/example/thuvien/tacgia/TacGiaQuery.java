package com.example.thuvien.tacgia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TacGiaQuery {

    private final SQLiteHelper dbHelper;

    public TacGiaQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<TacGia> layDanhSachTacGia() {
        List<TacGia> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaTG, TenTG, NamSinh, GioiTinh, QuocTich FROM tacgia ORDER BY MaTG ASC",
                null
        );

        while (cursor.moveToNext()) {
            TacGia item = new TacGia();
            item.setMaTG(cursor.getString(0));
            item.setTenTG(cursor.getString(1));
            item.setNamSinh(cursor.getString(2));
            item.setGioiTinh(cursor.getString(3));
            item.setQuocTich(cursor.getString(4));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public TacGia layThongTinTheoMa(String maTG) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaTG, TenTG, NamSinh, GioiTinh, QuocTich FROM tacgia WHERE MaTG = ?",
                new String[]{maTG}
        );

        TacGia item = null;
        if (cursor.moveToFirst()) {
            item = new TacGia();
            item.setMaTG(cursor.getString(0));
            item.setTenTG(cursor.getString(1));
            item.setNamSinh(cursor.getString(2));
            item.setGioiTinh(cursor.getString(3));
            item.setQuocTich(cursor.getString(4));
        }

        cursor.close();
        db.close();
        return item;
    }

    public String taoMaMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaTG FROM tacgia ORDER BY MaTG DESC LIMIT 1", null);

        String maMoi = "TG001";
        if (cursor.moveToFirst()) {
            try {
                int so = Integer.parseInt(cursor.getString(0).substring(2)) + 1;
                maMoi = String.format(Locale.getDefault(), "TG%03d", so);
            } catch (Exception e) {
                maMoi = "TG001";
            }
        }

        cursor.close();
        db.close();
        return maMoi;
    }

    public boolean themTacGia(TacGia item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "INSERT INTO tacgia (MaTG, TenTG, NamSinh, GioiTinh, QuocTich) VALUES (?, ?, ?, ?, ?)",
                    new Object[]{
                            item.getMaTG(),
                            item.getTenTG(),
                            item.getNamSinh(),
                            item.getGioiTinh(),
                            item.getQuocTich()
                    }
            );
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean capNhatTacGia(TacGia item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "UPDATE tacgia SET TenTG = ?, NamSinh = ?, GioiTinh = ?, QuocTich = ? WHERE MaTG = ?",
                    new Object[]{
                            item.getTenTG(),
                            item.getNamSinh(),
                            item.getGioiTinh(),
                            item.getQuocTich(),
                            item.getMaTG()
                    }
            );
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean xoaTacGia(String maTG) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int row = db.delete("tacgia", "MaTG = ?", new String[]{maTG});
            db.close();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public List<String> layDanhSachGioiTinh() {
        List<String> list = new ArrayList<>();
        list.add("Nam");
        list.add("Nữ");
        return list;
    }
}