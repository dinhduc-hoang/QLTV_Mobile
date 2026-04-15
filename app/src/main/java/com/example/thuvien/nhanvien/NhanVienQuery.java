package com.example.thuvien.nhanvien;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NhanVienQuery {

    private final SQLiteHelper dbHelper;

    public NhanVienQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<NhanVien> layDanhSachNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaNV, TenNV, QueQuan, GioiTinh, NamSinh, VaiTro, Email, Sdt, User, Pass FROM nhanvien ORDER BY MaNV ASC",
                null
        );

        while (cursor.moveToNext()) {
            NhanVien item = new NhanVien();
            item.setMaNV(cursor.getString(0));
            item.setTenNV(cursor.getString(1));
            item.setQueQuan(cursor.getString(2));
            item.setGioiTinh(cursor.getString(3));
            item.setNamSinh(cursor.getString(4));
            item.setVaiTro(cursor.getString(5));
            item.setEmail(cursor.getString(6));
            item.setSdt(cursor.getString(7));
            item.setUser(cursor.getString(8));
            item.setPass(cursor.getString(9));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public NhanVien layThongTinTheoMa(String maNV) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT MaNV, TenNV, QueQuan, GioiTinh, NamSinh, VaiTro, Email, Sdt, User, Pass FROM nhanvien WHERE MaNV = ?",
                new String[]{maNV}
        );

        NhanVien item = null;
        if (cursor.moveToFirst()) {
            item = new NhanVien();
            item.setMaNV(cursor.getString(0));
            item.setTenNV(cursor.getString(1));
            item.setQueQuan(cursor.getString(2));
            item.setGioiTinh(cursor.getString(3));
            item.setNamSinh(cursor.getString(4));
            item.setVaiTro(cursor.getString(5));
            item.setEmail(cursor.getString(6));
            item.setSdt(cursor.getString(7));
            item.setUser(cursor.getString(8));
            item.setPass(cursor.getString(9));
        }

        cursor.close();
        db.close();
        return item;
    }

    public String taoMaMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaNV FROM nhanvien ORDER BY MaNV DESC LIMIT 1", null);

        String maMoi = "NV001";
        if (cursor.moveToFirst()) {
            try {
                int so = Integer.parseInt(cursor.getString(0).substring(2)) + 1;
                maMoi = String.format(Locale.getDefault(), "NV%03d", so);
            } catch (Exception e) {
                maMoi = "NV001";
            }
        }

        cursor.close();
        db.close();
        return maMoi;
    }

    public boolean themNhanVien(NhanVien item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "INSERT INTO nhanvien (MaNV, TenNV, QueQuan, GioiTinh, NamSinh, VaiTro, Email, Sdt, User, Pass) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{
                            item.getMaNV(),
                            item.getTenNV(),
                            item.getQueQuan(),
                            item.getGioiTinh(),
                            item.getNamSinh(),
                            item.getVaiTro(),
                            item.getEmail(),
                            item.getSdt(),
                            item.getUser(),
                            item.getPass()
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

    public boolean capNhatNhanVien(NhanVien item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "UPDATE nhanvien SET TenNV = ?, QueQuan = ?, GioiTinh = ?, NamSinh = ?, VaiTro = ?, Email = ?, Sdt = ?, User = ?, Pass = ? WHERE MaNV = ?",
                    new Object[]{
                            item.getTenNV(),
                            item.getQueQuan(),
                            item.getGioiTinh(),
                            item.getNamSinh(),
                            item.getVaiTro(),
                            item.getEmail(),
                            item.getSdt(),
                            item.getUser(),
                            item.getPass(),
                            item.getMaNV()
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

    public boolean xoaNhanVien(String maNV) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int row = db.delete("nhanvien", "MaNV = ?", new String[]{maNV});
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

    public List<String> layDanhSachVaiTro() {
        List<String> list = new ArrayList<>();
        list.add("Quản lý");
        list.add("Thủ thư");
        return list;
    }
}
