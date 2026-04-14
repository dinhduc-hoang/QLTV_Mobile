package com.example.thuvien.docgia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.common.SpinnerItem;
import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DocGiaQuery {

    private final SQLiteHelper dbHelper;

    public DocGiaQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<DocGia> layDanhSachDocGia() {
        List<DocGia> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT d.MaDG, d.MaKhoa, d.MaLop, d.TenDG, d.NamSinh, d.GioiTinh, d.DiaChi, d.Email, d.Sdt, " +
                "k.TenKhoa, l.TenLop " +
                "FROM docgia d " +
                "JOIN khoa k ON d.MaKhoa = k.MaKhoa " +
                "JOIN lop l ON d.MaLop = l.MaLop " +
                "ORDER BY d.MaDG ASC";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            DocGia item = new DocGia();
            item.setMaDG(cursor.getString(0));
            item.setMaKhoa(cursor.getString(1));
            item.setMaLop(cursor.getString(2));
            item.setTenDG(cursor.getString(3));
            item.setNamSinh(cursor.getString(4));
            item.setGioiTinh(cursor.getString(5));
            item.setDiaChi(cursor.getString(6));
            item.setEmail(cursor.getString(7));
            item.setSdt(cursor.getString(8));
            item.setTenKhoa(cursor.getString(9));
            item.setTenLop(cursor.getString(10));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public DocGia layThongTinTheoMa(String maDG) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MaDG, MaKhoa, MaLop, TenDG, NamSinh, GioiTinh, DiaChi, Email, Sdt FROM docgia WHERE MaDG = ?",
                new String[]{maDG}
        );

        DocGia item = null;
        if (cursor.moveToFirst()) {
            item = new DocGia();
            item.setMaDG(cursor.getString(0));
            item.setMaKhoa(cursor.getString(1));
            item.setMaLop(cursor.getString(2));
            item.setTenDG(cursor.getString(3));
            item.setNamSinh(cursor.getString(4));
            item.setGioiTinh(cursor.getString(5));
            item.setDiaChi(cursor.getString(6));
            item.setEmail(cursor.getString(7));
            item.setSdt(cursor.getString(8));
        }

        cursor.close();
        db.close();
        return item;
    }

    public String taoMaMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaDG FROM docgia ORDER BY MaDG DESC LIMIT 1", null);

        String maMoi = "DG001";
        if (cursor.moveToFirst()) {
            try {
                int so = Integer.parseInt(cursor.getString(0).substring(2)) + 1;
                maMoi = String.format(Locale.getDefault(), "DG%03d", so);
            } catch (Exception e) {
                maMoi = "DG001";
            }
        }

        cursor.close();
        db.close();
        return maMoi;
    }

    public boolean themDocGia(DocGia item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("INSERT INTO docgia (MaDG, MaKhoa, MaLop, TenDG, NamSinh, GioiTinh, DiaChi, Email, Sdt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{
                            item.getMaDG(),
                            item.getMaKhoa(),
                            item.getMaLop(),
                            item.getTenDG(),
                            item.getNamSinh(),
                            item.getGioiTinh(),
                            item.getDiaChi(),
                            item.getEmail(),
                            item.getSdt()
                    });
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean capNhatDocGia(DocGia item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("UPDATE docgia SET MaKhoa = ?, MaLop = ?, TenDG = ?, NamSinh = ?, GioiTinh = ?, DiaChi = ?, Email = ?, Sdt = ? WHERE MaDG = ?",
                    new Object[]{
                            item.getMaKhoa(),
                            item.getMaLop(),
                            item.getTenDG(),
                            item.getNamSinh(),
                            item.getGioiTinh(),
                            item.getDiaChi(),
                            item.getEmail(),
                            item.getSdt(),
                            item.getMaDG()
                    });
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean xoaDocGia(String maDG) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int row = db.delete("docgia", "MaDG = ?", new String[]{maDG});
            db.close();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public List<SpinnerItem> layDanhSachSpinner(String tableName, String idCol, String nameCol, String title) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + idCol + ", " + nameCol + " FROM " + tableName, null);

        List<SpinnerItem> list = new ArrayList<>();
        list.add(new SpinnerItem("", title));

        while (cursor.moveToNext()) {
            list.add(new SpinnerItem(cursor.getString(0), cursor.getString(1)));
        }

        cursor.close();
        db.close();
        return list;
    }

    public List<String> layDanhSachGioiTinh() {
        List<String> list = new ArrayList<>();
        list.add("Nam");
        list.add("Nữ");
        return list;
    }
    public List<SpinnerItem> layDanhSachKhoaSpinner() {
        return layDanhSachSpinner("khoa", "MaKhoa", "TenKhoa", "--- Chọn khoa ---");
    }

    public List<SpinnerItem> layDanhSachLopTheoKhoa(String maKhoa) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<SpinnerItem> list = new ArrayList<>();
        list.add(new SpinnerItem("", "--- Chọn lớp ---"));

        if (maKhoa != null && !maKhoa.isEmpty()) {
            Cursor cursor = db.rawQuery(
                    "SELECT MaLop, TenLop FROM lop WHERE MaKhoa = ? ORDER BY TenLop ASC",
                    new String[]{maKhoa}
            );

            while (cursor.moveToNext()) {
                list.add(new SpinnerItem(cursor.getString(0), cursor.getString(1)));
            }
            cursor.close();
        }

        db.close();
        return list;
    }
}