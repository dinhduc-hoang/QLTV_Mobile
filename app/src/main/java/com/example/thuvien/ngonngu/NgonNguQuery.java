package com.example.thuvien.ngonngu;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NgonNguQuery {

    private final SQLiteHelper dbHelper;

    public NgonNguQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<NgonNgu> layDanhSachNgonNgu() {
        List<NgonNgu> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaNN, TenNN FROM ngonngu ORDER BY MaNN ASC",
                null
        );

        while (cursor.moveToNext()) {
            NgonNgu item = new NgonNgu();
            item.setMaNN(cursor.getString(0));
            item.setTenNN(cursor.getString(1));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public NgonNgu layThongTinTheoMa(String maNN) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaNN, TenNN FROM ngonngu WHERE MaNN = ?",
                new String[]{maNN}
        );

        NgonNgu item = null;
        if (cursor.moveToFirst()) {
            item = new NgonNgu();
            item.setMaNN(cursor.getString(0));
            item.setTenNN(cursor.getString(1));
        }

        cursor.close();
        db.close();
        return item;
    }

    public String taoMaMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaNN FROM ngonngu ORDER BY MaNN DESC LIMIT 1", null);

        String maMoi = "NN001";
        if (cursor.moveToFirst()) {
            try {
                int so = Integer.parseInt(cursor.getString(0).substring(2)) + 1;
                maMoi = String.format(Locale.getDefault(), "NN%03d", so);
            } catch (Exception e) {
                maMoi = "NN001";
            }
        }

        cursor.close();
        db.close();
        return maMoi;
    }

    public boolean themNgonNgu(NgonNgu item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "INSERT INTO ngonngu (MaNN, TenNN) VALUES (?, ?)",
                    new Object[]{item.getMaNN(), item.getTenNN()}
            );
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean capNhatNgonNgu(NgonNgu item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "UPDATE ngonngu SET TenNN = ? WHERE MaNN = ?",
                    new Object[]{item.getTenNN(), item.getMaNN()}
            );
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean xoaNgonNgu(String maNN) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int row = db.delete("ngonngu", "MaNN = ?", new String[]{maNN});
            db.close();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean ngonNguDangDuocSuDung(String maNN) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean result = false;
        Cursor cursor = db.rawQuery("SELECT 1 FROM sach WHERE MaNN = ? LIMIT 1", new String[]{maNN});
        if (cursor.moveToFirst()) {
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }
}
