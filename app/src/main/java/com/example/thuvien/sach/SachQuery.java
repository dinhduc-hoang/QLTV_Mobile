package com.example.thuvien.sach;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.common.SpinnerItem;
import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SachQuery {

    private final SQLiteHelper dbHelper;

    public SachQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<Sach> layDanhSachSach() {
        List<Sach> list = new ArrayList<Sach>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT s.MaSach, s.MaTG, s.MaNXB, s.MaTL, s.TenSach, s.MaNN, s.MaViTri, s.NamXB, s.SoLuong, " +
                "tg.TenTG, tl.TenTL, nxb.TenNXB, nn.TenNN, ks.TenKe " +
                "FROM sach s " +
                "JOIN tacgia tg ON s.MaTG = tg.MaTG " +
                "JOIN theloai tl ON s.MaTL = tl.MaTL " +
                "JOIN nhaxuatban nxb ON s.MaNXB = nxb.MaNXB " +
                "JOIN ngonngu nn ON s.MaNN = nn.MaNN " +
                "JOIN kesach ks ON s.MaViTri = ks.MaViTri " +
                "ORDER BY s.MaSach ASC";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            Sach sach = new Sach();
            sach.setMaSach(cursor.getString(0));
            sach.setMaTG(cursor.getString(1));
            sach.setMaNXB(cursor.getString(2));
            sach.setMaTL(cursor.getString(3));
            sach.setTenSach(cursor.getString(4));
            sach.setMaNN(cursor.getString(5));
            sach.setMaViTri(cursor.getString(6));
            sach.setNamXB(cursor.getInt(7));
            sach.setSoLuong(cursor.getInt(8));
            sach.setTenTG(cursor.getString(9));
            sach.setTenTL(cursor.getString(10));
            sach.setTenNXB(cursor.getString(11));
            sach.setTenNN(cursor.getString(12));
            sach.setTenViTri(cursor.getString(13));
            list.add(sach);
        }

        cursor.close();
        db.close();
        return list;
    }

    public List<Sach> timKiemSach(String keyword) {
        List<Sach> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT s.MaSach, s.MaTG, s.MaNXB, s.MaTL, s.TenSach, s.MaNN, s.MaViTri, s.NamXB, s.SoLuong, " +
                "tg.TenTG, tl.TenTL, nxb.TenNXB, nn.TenNN, ks.TenKe " +
                "FROM sach s " +
                "JOIN tacgia tg ON s.MaTG = tg.MaTG " +
                "JOIN theloai tl ON s.MaTL = tl.MaTL " +
                "JOIN nhaxuatban nxb ON s.MaNXB = nxb.MaNXB " +
                "JOIN ngonngu nn ON s.MaNN = nn.MaNN " +
                "JOIN kesach ks ON s.MaViTri = ks.MaViTri " +
                "WHERE s.TenSach LIKE ? OR s.MaSach LIKE ? OR tg.TenTG LIKE ? " +
                "ORDER BY s.MaSach ASC";

        String key = "%" + keyword + "%";
        Cursor cursor = db.rawQuery(sql, new String[]{key, key, key});

        while (cursor.moveToNext()) {
            Sach sach = new Sach();
            sach.setMaSach(cursor.getString(0));
            sach.setMaTG(cursor.getString(1));
            sach.setMaNXB(cursor.getString(2));
            sach.setMaTL(cursor.getString(3));
            sach.setTenSach(cursor.getString(4));
            sach.setMaNN(cursor.getString(5));
            sach.setMaViTri(cursor.getString(6));
            sach.setNamXB(cursor.getInt(7));
            sach.setSoLuong(cursor.getInt(8));
            sach.setTenTG(cursor.getString(9));
            sach.setTenTL(cursor.getString(10));
            sach.setTenNXB(cursor.getString(11));
            sach.setTenNN(cursor.getString(12));
            sach.setTenViTri(cursor.getString(13));
            list.add(sach);
        }

        cursor.close();
        db.close();
        return list;
    }

    public Sach layThongTinSachTheoMa(String maSach) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT s.MaSach, s.MaTG, s.MaNXB, s.MaTL, s.TenSach, s.MaNN, s.MaViTri, s.NamXB, s.SoLuong, " +
                "tg.TenTG, tl.TenTL, nxb.TenNXB, nn.TenNN, ks.TenKe " +
                "FROM sach s " +
                "JOIN tacgia tg ON s.MaTG = tg.MaTG " +
                "JOIN theloai tl ON s.MaTL = tl.MaTL " +
                "JOIN nhaxuatban nxb ON s.MaNXB = nxb.MaNXB " +
                "JOIN ngonngu nn ON s.MaNN = nn.MaNN " +
                "JOIN kesach ks ON s.MaViTri = ks.MaViTri " +
                "WHERE s.MaSach = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{maSach});

        Sach sach = null;

        if (cursor.moveToFirst()) {
            sach = new Sach();
            sach.setMaSach(cursor.getString(0));
            sach.setMaTG(cursor.getString(1));
            sach.setMaNXB(cursor.getString(2));
            sach.setMaTL(cursor.getString(3));
            sach.setTenSach(cursor.getString(4));
            sach.setMaNN(cursor.getString(5));
            sach.setMaViTri(cursor.getString(6));
            sach.setNamXB(cursor.getInt(7));
            sach.setSoLuong(cursor.getInt(8));
            sach.setTenTG(cursor.getString(9));
            sach.setTenTL(cursor.getString(10));
            sach.setTenNXB(cursor.getString(11));
            sach.setTenNN(cursor.getString(12));
            sach.setTenViTri(cursor.getString(13));
        }

        cursor.close();
        db.close();
        return sach;
    }

    public String taoMaSachMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaSach FROM sach ORDER BY MaSach DESC LIMIT 1", null);

        String maMoi = "S001";

        if (cursor.moveToFirst()) {
            String maCu = cursor.getString(0);
            try {
                int so = Integer.parseInt(maCu.substring(1)) + 1;
                maMoi = String.format(Locale.getDefault(), "S%03d", so);
            } catch (Exception e) {
                maMoi = "S001";
            }
        }

        cursor.close();
        db.close();
        return maMoi;
    }

    public boolean themSach(Sach sach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("INSERT INTO sach (MaSach, MaTG, MaNXB, MaTL, TenSach, MaNN, MaViTri, NamXB, SoLuong) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{
                            sach.getMaSach(),
                            sach.getMaTG(),
                            sach.getMaNXB(),
                            sach.getMaTL(),
                            sach.getTenSach(),
                            sach.getMaNN(),
                            sach.getMaViTri(),
                            sach.getNamXB(),
                            sach.getSoLuong()
                    });
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean suaSach(Sach sach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("UPDATE sach SET MaTG = ?, MaNXB = ?, MaTL = ?, TenSach = ?, MaNN = ?, MaViTri = ?, NamXB = ?, SoLuong = ? WHERE MaSach = ?",
                    new Object[]{
                            sach.getMaTG(),
                            sach.getMaNXB(),
                            sach.getMaTL(),
                            sach.getTenSach(),
                            sach.getMaNN(),
                            sach.getMaViTri(),
                            sach.getNamXB(),
                            sach.getSoLuong(),
                            sach.getMaSach()
                    });
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public boolean sachDangDuocMuon(String maSach) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) " +
                        "FROM chitietmuontra ct " +
                        "JOIN muontra mt ON ct.MaMT = mt.MaMT " +
                        "WHERE ct.MaSach = ? AND (mt.TrangThai = 'Chưa trả' OR mt.TrangThai = 'Đang mượn')",
                new String[]{maSach}
        );

        int soLuongDangMuon = 0;
        if (cursor.moveToFirst()) {
            soLuongDangMuon = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return soLuongDangMuon > 0;
    }

    public boolean xoaSach(String maSach) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete("sach", "MaSach = ?", new String[]{maSach});
        db.close();
        return row > 0;
    }
    public List<Sach> layDanhSachSachMuonNhieu() {
        List<Sach> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT s.MaSach, s.MaTG, s.MaNXB, s.MaTL, s.TenSach, s.MaNN, s.MaViTri, s.NamXB, s.SoLuong, " +
                "tg.TenTG, tl.TenTL, nxb.TenNXB, nn.TenNN, ks.TenKe, SUM(ct.SoLuong) as TongMuon " +
                "FROM sach s " +
                "JOIN tacgia tg ON s.MaTG = tg.MaTG " +
                "JOIN theloai tl ON s.MaTL = tl.MaTL " +
                "JOIN nhaxuatban nxb ON s.MaNXB = nxb.MaNXB " +
                "JOIN ngonngu nn ON s.MaNN = nn.MaNN " +
                "JOIN kesach ks ON s.MaViTri = ks.MaViTri " +
                "JOIN chitietmuontra ct ON s.MaSach = ct.MaSach " +
                "GROUP BY s.MaSach " +
                "ORDER BY TongMuon DESC " +
                "LIMIT 10";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            Sach sach = new Sach();
            sach.setMaSach(cursor.getString(0));
            sach.setMaTG(cursor.getString(1));
            sach.setMaNXB(cursor.getString(2));
            sach.setMaTL(cursor.getString(3));
            sach.setTenSach(cursor.getString(4));
            sach.setMaNN(cursor.getString(5));
            sach.setMaViTri(cursor.getString(6));
            sach.setNamXB(cursor.getInt(7));
            sach.setSoLuong(cursor.getInt(8));
            sach.setTenTG(cursor.getString(9));
            sach.setTenTL(cursor.getString(10));
            sach.setTenNXB(cursor.getString(11));
            sach.setTenNN(cursor.getString(12));
            sach.setTenViTri(cursor.getString(13));
            list.add(sach);
        }

        cursor.close();
        db.close();

        // Nếu danh sách vẫn trống (rất hiếm khi xảy ra), lấy danh sách sách mặc định
        if (list.isEmpty()) {
            return layDanhSachSach();
        }

        return list;
    }
    public List<SpinnerItem> layDanhSachSpinner(String tableName, String idCol, String nameCol, String title) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + idCol + ", " + nameCol + " FROM " + tableName, null);

        List<SpinnerItem> list = new ArrayList<SpinnerItem>();
        list.add(new SpinnerItem("", title));

        while (cursor.moveToNext()) {
            list.add(new SpinnerItem(cursor.getString(0), cursor.getString(1)));
        }

        cursor.close();
        db.close();
        return list;
    }
}
