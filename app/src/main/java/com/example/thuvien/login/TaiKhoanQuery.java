package com.example.thuvien.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.database.SQLiteHelper;
import com.example.thuvien.docgia.DocGia;

public class TaiKhoanQuery {

    private SQLiteHelper dbHelper;

    public TaiKhoanQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public static class UserInfo {
        private String tenNhanVien;
        private String vaiTro;
        private String maNhanVien;

        public UserInfo(String maNhanVien, String tenNhanVien, String vaiTro) {
            this.maNhanVien = maNhanVien;
            this.tenNhanVien = tenNhanVien;
            this.vaiTro = vaiTro;
        }

        public String getMaNhanVien() {
            return maNhanVien;
        }

        public String getTenNhanVien() {
            return tenNhanVien;
        }

        public String getVaiTro() {
            return vaiTro;
        }
    }

    public UserInfo dangNhap(String user, String pass) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MaNV, TenNV, VaiTro FROM nhanvien WHERE User = ? AND Pass = ? LIMIT 1",
                new String[]{user, pass}
        );

        UserInfo userInfo = null;

        if (cursor.moveToFirst()) {
            String maNV = cursor.getString(0);
            String tenNV = cursor.getString(1);
            String vaiTro = cursor.getString(2);
            userInfo = new UserInfo(maNV, tenNV, vaiTro);
        }

        cursor.close();
        db.close();

        return userInfo;
    }

    public DocGia dangNhapDocGia(String email, String pass) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MaDG, TenDG FROM docgia WHERE Email = ? AND MatKhau = ? LIMIT 1",
                new String[]{email, pass}
        );

        DocGia docGia = null;
        if (cursor.moveToFirst()) {
            docGia = new DocGia();
            docGia.setMaDG(cursor.getString(0));
            docGia.setTenDG(cursor.getString(1));
        }

        cursor.close();
        db.close();
        return docGia;
    }

    public boolean doiMatKhau(String user, String email, String passMoi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("UPDATE nhanvien SET Pass = ? WHERE [User] = ? AND Email = ?", new Object[]{passMoi, user, email});
            db.execSQL("UPDATE docgia SET MatKhau = ? WHERE MaDG = ? AND Email = ?", new Object[]{passMoi, user, email});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}