package com.example.thuvien.docgia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.common.SpinnerItem;
import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class DocGiaQuery {

    private SQLiteHelper dbHelper;

    public DocGiaQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<DocGia> layDanhSachDocGia() {
        List<DocGia> listDocGia = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT d.MaDG, d.MaKhoa, d.MaLop, d.TenDG, d.NamSinh, d.GioiTinh, d.DiaChi, d.Email, d.Sdt, " +
                        "k.TenKhoa, l.TenLop " +
                        "FROM docgia d " +
                        "JOIN khoa k ON d.MaKhoa = k.MaKhoa " +
                        "JOIN lop l ON d.MaLop = l.MaLop " +
                        "ORDER BY d.MaDG",
                null
        );

        while (cursor.moveToNext() == true) {
            DocGia dg = new DocGia();
            dg.setMaDG(cursor.getString(0));
            dg.setMaKhoa(cursor.getString(1));
            dg.setMaLop(cursor.getString(2));
            dg.setTenDG(cursor.getString(3));
            dg.setNamSinh(cursor.getString(4));
            dg.setGioiTinh(cursor.getString(5));
            dg.setDiaChi(cursor.getString(6));
            dg.setEmail(cursor.getString(7));
            dg.setSdt(cursor.getString(8));
            dg.setTenKhoa(cursor.getString(9));
            dg.setTenLop(cursor.getString(10));

            listDocGia.add(dg);
        }

        cursor.close();
        return listDocGia;
    }

    public String taoMaMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaDG FROM docgia ORDER BY MaDG DESC LIMIT 1", null);

        String maMoi = "DG001";
        if (cursor.moveToFirst() == true) {
            String maCuoi = cursor.getString(0);

            if (maCuoi != null && maCuoi.startsWith("DG")) {
                String phanSo = maCuoi.substring(2);
                try {
                    int so = Integer.parseInt(phanSo);
                    so = so + 1;

                    if (so < 10) {
                        maMoi = "DG00" + so;
                    } else if (so < 100) {
                        maMoi = "DG0" + so;
                    } else {
                        maMoi = "DG" + so;
                    }
                } catch (Exception e) {
                    maMoi = "DG001";
                }
            }
        }

        cursor.close();
        return maMoi;
    }

    public boolean themDocGia(DocGia dg) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Object[] data = new Object[]{
                    dg.getMaDG(), dg.getMaKhoa(), dg.getMaLop(), dg.getTenDG(), dg.getNamSinh(),
                    dg.getGioiTinh(), dg.getDiaChi(), dg.getEmail(), dg.getSdt()
            };
            db.execSQL("INSERT INTO docgia (MaDG, MaKhoa, MaLop, TenDG, NamSinh, GioiTinh, DiaChi, Email, Sdt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean suaDocGia(DocGia dg) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Object[] data = new Object[]{
                    dg.getMaKhoa(), dg.getMaLop(), dg.getTenDG(), dg.getNamSinh(),
                    dg.getGioiTinh(), dg.getDiaChi(), dg.getEmail(), dg.getSdt(), dg.getMaDG()
            };
            db.execSQL("UPDATE docgia SET MaKhoa = ?, MaLop = ?, TenDG = ?, NamSinh = ?, GioiTinh = ?, DiaChi = ?, Email = ?, Sdt = ? WHERE MaDG = ?", data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean capNhatDocGia(DocGia dg) {
        return suaDocGia(dg);
    }

    public boolean xoaDocGia(String maDG) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Object[] data = new Object[]{maDG};
            db.execSQL("DELETE FROM docgia WHERE MaDG = ?", data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public List<DocGia> timKiemDocGia(String keyword) {
        List<DocGia> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String key = "%" + keyword + "%";

        Cursor cursor = db.rawQuery(
                "SELECT d.MaDG, d.MaKhoa, d.MaLop, d.TenDG, d.NamSinh, d.GioiTinh, d.DiaChi, d.Email, d.Sdt, d.MatKhau, k.TenKhoa, l.TenLop " +
                        "FROM docgia d " +
                        "JOIN khoa k ON d.MaKhoa = k.MaKhoa " +
                        "JOIN lop l ON d.MaLop = l.MaLop " +
                        "WHERE d.MaDG LIKE ? OR d.TenDG LIKE ? OR d.Email LIKE ? OR d.Sdt LIKE ?",
                new String[]{key, key, key, key}
        );

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
            item.setMatKhau(cursor.getString(9));
            item.setTenKhoa(cursor.getString(10));
            item.setTenLop(cursor.getString(11));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public DocGia layThongTinTheoMa(String maDG) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT d.MaDG, d.MaKhoa, d.MaLop, d.TenDG, d.NamSinh, d.GioiTinh, d.DiaChi, d.Email, d.Sdt, d.MatKhau, k.TenKhoa, l.TenLop " +
                        "FROM docgia d " +
                        "JOIN khoa k ON d.MaKhoa = k.MaKhoa " +
                        "JOIN lop l ON d.MaLop = l.MaLop " +
                        "WHERE d.MaDG = ?",
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
            item.setMatKhau(cursor.getString(9));
            item.setTenKhoa(cursor.getString(10));
            item.setTenLop(cursor.getString(11));
        }

        cursor.close();
        db.close();
        return item;
    }
    public boolean docGiaDangMuonSach(String maDG) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean dangMuon = false;

        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM muontra WHERE MaDG = ? AND TrangThai = 'Chưa trả' LIMIT 1",
                new String[]{maDG}
        );
        if (cursor.moveToFirst() == true) {
            dangMuon = true;
        }
        cursor.close();

        if (dangMuon == false) {
            Cursor cursorThe = db.rawQuery(
                    "SELECT 1 FROM thethuvien WHERE MaDG = ? LIMIT 1",
                    new String[]{maDG}
            );
            if (cursorThe.moveToFirst() == true) {
                dangMuon = true;
            }
            cursorThe.close();
        }

        return dangMuon;
    }

    public List<SpinnerItem> layDanhSachSpinner(String tableName, String idCol, String nameCol, String title) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + idCol + ", " + nameCol + " FROM " + tableName, null);

        List<SpinnerItem> listSpinner = new ArrayList<>();
        listSpinner.add(new SpinnerItem("", title));

        while (cursor.moveToNext() == true) {
            listSpinner.add(new SpinnerItem(cursor.getString(0), cursor.getString(1)));
        }

        cursor.close();
        return listSpinner;
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
        List<SpinnerItem> listLop = new ArrayList<>();
        listLop.add(new SpinnerItem("", "--- Chọn lớp ---"));

        if (maKhoa != null && !maKhoa.isEmpty()) {
            Cursor cursor = db.rawQuery(
                    "SELECT MaLop, TenLop FROM lop WHERE MaKhoa = ? ORDER BY TenLop ASC",
                    new String[]{maKhoa}
            );

            while (cursor.moveToNext() == true) {
                listLop.add(new SpinnerItem(cursor.getString(0), cursor.getString(1)));
            }
            cursor.close();
        }

        return listLop;
    }
}
