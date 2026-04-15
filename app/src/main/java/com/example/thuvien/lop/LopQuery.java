package com.example.thuvien.lop;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.common.SpinnerItem;
import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LopQuery {

    private final SQLiteHelper dbHelper;

    public LopQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<Lop> layDanhSachLop() {
        List<Lop> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT l.MaLop, l.TenLop, l.MaKhoa, k.TenKhoa " +
                "FROM lop l JOIN khoa k ON l.MaKhoa = k.MaKhoa " +
                "ORDER BY l.MaLop ASC";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            Lop item = new Lop();
            item.setMaLop(cursor.getString(0));
            item.setTenLop(cursor.getString(1));
            item.setMaKhoa(cursor.getString(2));
            item.setTenKhoa(cursor.getString(3));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public Lop layThongTinTheoMa(String maLop) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaLop, TenLop, MaKhoa FROM lop WHERE MaLop = ?",
                new String[]{maLop}
        );

        Lop item = null;
        if (cursor.moveToFirst()) {
            item = new Lop();
            item.setMaLop(cursor.getString(0));
            item.setTenLop(cursor.getString(1));
            item.setMaKhoa(cursor.getString(2));
        }

        cursor.close();
        db.close();
        return item;
    }

    public String taoMaMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaLop FROM lop ORDER BY MaLop DESC LIMIT 1", null);

        String maMoi = "L001";
        if (cursor.moveToFirst()) {
            try {
                int so = Integer.parseInt(cursor.getString(0).substring(1)) + 1;
                maMoi = String.format(Locale.getDefault(), "L%03d", so);
            } catch (Exception e) {
                maMoi = "L001";
            }
        }

        cursor.close();
        db.close();
        return maMoi;
    }

    public boolean themLop(Lop item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "INSERT INTO lop (MaLop, TenLop, MaKhoa) VALUES (?, ?, ?)",
                    new Object[]{item.getMaLop(), item.getTenLop(), item.getMaKhoa()}
            );
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean capNhatLop(Lop item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "UPDATE lop SET TenLop = ?, MaKhoa = ? WHERE MaLop = ?",
                    new Object[]{item.getTenLop(), item.getMaKhoa(), item.getMaLop()}
            );
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean xoaLop(String maLop) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int row = db.delete("lop", "MaLop = ?", new String[]{maLop});
            db.close();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public List<SpinnerItem> layDanhSachKhoaSpinner() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaKhoa, TenKhoa FROM khoa ORDER BY TenKhoa ASC", null);

        List<SpinnerItem> list = new ArrayList<>();
        list.add(new SpinnerItem("", "--- Chọn khoa ---"));

        while (cursor.moveToNext()) {
            list.add(new SpinnerItem(cursor.getString(0), cursor.getString(1)));
        }

        cursor.close();
        db.close();
        return list;
    }
}
