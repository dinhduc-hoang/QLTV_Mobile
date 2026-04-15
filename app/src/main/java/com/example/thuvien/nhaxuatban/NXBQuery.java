package com.example.thuvien.nhaxuatban;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NXBQuery {

    SQLiteHelper dbHelper;

    public NXBQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<NXB> layDanhSachNXB() {
        List<NXB> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaNXB, TenNXB, DiaChi, Email, Sdt FROM nhaxuatban ORDER BY MaNXB ASC",
                null
        );

        while (cursor.moveToNext()) {
            NXB item = new NXB();
            item.setMaNXB(cursor.getString(0));
            item.setTenNXB(cursor.getString(1));
            item.setDiaChi(cursor.getString(2));
            item.setEmail(cursor.getString(3));
            item.setSdt(cursor.getString(4));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public NXB layTheoMa(String maNXB) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaNXB, TenNXB, DiaChi, Email, Sdt FROM nhaxuatban WHERE MaNXB = ?",
                new String[]{maNXB}
        );

        NXB item = null;

        if (cursor.moveToFirst()) {
            item = new NXB();
            item.setMaNXB(cursor.getString(0));
            item.setTenNXB(cursor.getString(1));
            item.setDiaChi(cursor.getString(2));
            item.setEmail(cursor.getString(3));
            item.setSdt(cursor.getString(4));
        }

        cursor.close();
        db.close();
        return item;
    }

    public String taoMaMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaNXB FROM nhaxuatban ORDER BY MaNXB DESC LIMIT 1", null);

        String ma = "NXB001";

        if (cursor.moveToFirst()) {
            try {
                int so = Integer.parseInt(cursor.getString(0).substring(3)) + 1;
                ma = String.format(Locale.getDefault(), "NXB%03d", so);
            } catch (Exception e) {
                ma = "NXB001";
            }
        }

        cursor.close();
        db.close();
        return ma;
    }

    public boolean them(NXB item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "INSERT INTO nhaxuatban VALUES (?, ?, ?, ?, ?)",
                    new Object[]{
                            item.getMaNXB(),
                            item.getTenNXB(),
                            item.getDiaChi(),
                            item.getEmail(),
                            item.getSdt()
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

    public boolean capNhat(NXB item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "UPDATE nhaxuatban SET TenNXB=?, DiaChi=?, Email=?, Sdt=? WHERE MaNXB=?",
                    new Object[]{
                            item.getTenNXB(),
                            item.getDiaChi(),
                            item.getEmail(),
                            item.getSdt(),
                            item.getMaNXB()
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

    public boolean xoa(String maNXB) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete("nhaxuatban", "MaNXB=?", new String[]{maNXB});
        db.close();
        return row > 0;
    }
}
