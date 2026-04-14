package com.example.thuvien.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.database.SQLiteHelper;

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

    public boolean doiMatKhau(String user, String email, String passMoi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Pass", passMoi);

        int row = db.update(
                "nhanvien",
                values,
                "User = ? AND Email = ?",
                new String[]{user, email}
        );

        db.close();
        return row > 0;
    }
}