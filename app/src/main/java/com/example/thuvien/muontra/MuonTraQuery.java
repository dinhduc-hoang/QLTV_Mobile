package com.example.thuvien.muontra;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.common.SpinnerItem;
import com.example.thuvien.database.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MuonTraQuery {

    private final SQLiteHelper dbHelper;

    public MuonTraQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<MuonTra> layDanhSachMuonTra() {
        List<MuonTra> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT mt.MaMT, mt.MaDG, mt.MaNV, mt.NgayMuon, mt.HanTra, mt.TrangThai, " +
                "d.TenDG, nv.TenNV " +
                "FROM muontra mt " +
                "JOIN docgia d ON mt.MaDG = d.MaDG " +
                "JOIN nhanvien nv ON mt.MaNV = nv.MaNV " +
                "ORDER BY mt.MaMT ASC";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            MuonTra item = new MuonTra();
            item.setMaMT(cursor.getString(0));
            item.setMaDG(cursor.getString(1));
            item.setMaNV(cursor.getString(2));
            item.setNgayMuon(cursor.getString(3));
            item.setHanTra(cursor.getString(4));
            item.setTrangThai(cursor.getString(5));
            item.setTenDG(cursor.getString(6));
            item.setTenNV(cursor.getString(7));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public MuonTra layThongTinMuonTraTheoMa(String maMT) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT mt.MaMT, mt.MaDG, mt.MaNV, mt.NgayMuon, mt.HanTra, mt.TrangThai, " +
                "d.TenDG, nv.TenNV " +
                "FROM muontra mt " +
                "JOIN docgia d ON mt.MaDG = d.MaDG " +
                "JOIN nhanvien nv ON mt.MaNV = nv.MaNV " +
                "WHERE mt.MaMT = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{maMT});
        MuonTra item = null;

        if (cursor.moveToFirst()) {
            item = new MuonTra();
            item.setMaMT(cursor.getString(0));
            item.setMaDG(cursor.getString(1));
            item.setMaNV(cursor.getString(2));
            item.setNgayMuon(cursor.getString(3));
            item.setHanTra(cursor.getString(4));
            item.setTrangThai(cursor.getString(5));
            item.setTenDG(cursor.getString(6));
            item.setTenNV(cursor.getString(7));
        }

        cursor.close();
        db.close();
        return item;
    }

    public List<String> layChiTietSachTrongPhieu(String maMT) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT s.TenSach, ct.SoLuong " +
                        "FROM chitietmuontra ct " +
                        "JOIN sach s ON ct.MaSach = s.MaSach " +
                        "WHERE ct.MaMT = ?",
                new String[]{maMT}
        );

        while (cursor.moveToNext()) {
            list.add(cursor.getString(0) + " - SL: " + cursor.getInt(1));
        }

        cursor.close();
        db.close();
        return list;
    }

    public boolean capNhatTrangThaiDaTra(String maMT) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            Cursor cursor = db.rawQuery("SELECT MaSach, SoLuong FROM chitietmuontra WHERE MaMT = ?", new String[]{maMT});
            while (cursor.moveToNext()) {
                String maSach = cursor.getString(0);
                int soLuongMuon = cursor.getInt(1);

                db.execSQL("UPDATE sach SET SoLuong = SoLuong + ? WHERE MaSach = ?", new Object[]{soLuongMuon, maSach});
            }
            cursor.close();

            db.execSQL("UPDATE muontra SET TrangThai = 'Đã trả' WHERE MaMT = ?", new Object[]{maMT});

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db.inTransaction()) {
                db.endTransaction();
            }
            db.close();
        }
    }

    public boolean xoaPhieuMuon(String maMT) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.delete("chitietmuontra", "MaMT = ?", new String[]{maMT});
            int row = db.delete("muontra", "MaMT = ?", new String[]{maMT});
            db.close();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public String taoMaMuonTraMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaMT FROM muontra ORDER BY CAST(SUBSTR(MaMT, 3) AS INTEGER) DESC LIMIT 1", null);

        String maMoi = "MT001";

        if (cursor.moveToFirst()) {
            String maHienTai = cursor.getString(0);
            try {
                int so = Integer.parseInt(maHienTai.substring(2)) + 1;
                maMoi = String.format(Locale.getDefault(), "MT%03d", so);
            } catch (Exception e) {
                maMoi = "MT" + (System.currentTimeMillis() % 100000);
            }
        }

        cursor.close();
        db.close();
        return maMoi;
    }

    public boolean themPhieuMuonVaChiTiet(String maMT, String maDG, String maNV, String ngayMuon, String hanTra,
                                          List<String> listMaSach, List<Integer> listSoLuong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            db.execSQL("INSERT INTO muontra (MaMT, MaDG, MaNV, NgayMuon, HanTra, TrangThai) VALUES (?, ?, ?, ?, ?, ?)",
                    new Object[]{maMT, maDG, maNV, ngayMuon, hanTra, "Chưa trả"});

            for (int i = 0; i < listMaSach.size(); i++) {
                String maSach = listMaSach.get(i);
                int soLuong = listSoLuong.get(i);

                db.execSQL("INSERT INTO chitietmuontra (MaMT, MaSach, SoLuong) VALUES (?, ?, ?)",
                        new Object[]{maMT, maSach, soLuong});

                db.execSQL("UPDATE sach SET SoLuong = SoLuong - ? WHERE MaSach = ?",
                        new Object[]{soLuong, maSach});
            }

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db.inTransaction()) {
                db.endTransaction();
            }
            db.close();
        }
    }

    public List<ChiTietMuonTra> layDanhSachChiTiet(String maMT) {
        List<ChiTietMuonTra> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT ct.MaSach, s.TenSach, ct.SoLuong " +
                        "FROM chitietmuontra ct " +
                        "JOIN sach s ON ct.MaSach = s.MaSach " +
                        "WHERE ct.MaMT = ?",
                new String[]{maMT}
        );
        while (cursor.moveToNext()) {
            ChiTietMuonTra ct = new ChiTietMuonTra();
            ct.setMaSach(cursor.getString(0));
            ct.setTenSach(cursor.getString(1));
            ct.setSoLuong(cursor.getInt(2));
            list.add(ct);
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean capNhatMuonTraToanBo(String maMT, String maDG, String maNV, String ngayMuon, String hanTra, String trangThai,
                                       List<String> listMaSach, List<Integer> listSoLuong) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            Cursor cursor = db.rawQuery("SELECT MaSach, SoLuong FROM chitietmuontra WHERE MaMT = ?", new String[]{maMT});
            while (cursor.moveToNext()) {
                db.execSQL("UPDATE sach SET SoLuong = SoLuong + ? WHERE MaSach = ?",
                        new Object[]{cursor.getInt(1), cursor.getString(0)});
            }
            cursor.close();

            db.delete("chitietmuontra", "MaMT = ?", new String[]{maMT});

            db.execSQL("UPDATE muontra SET MaDG = ?, MaNV = ?, NgayMuon = ?, HanTra = ?, TrangThai = ? WHERE MaMT = ?",
                    new Object[]{maDG, maNV, ngayMuon, hanTra, trangThai, maMT});

            for (int i = 0; i < listMaSach.size(); i++) {
                db.execSQL("INSERT INTO chitietmuontra (MaMT, MaSach, SoLuong) VALUES (?, ?, ?)",
                        new Object[]{maMT, listMaSach.get(i), listSoLuong.get(i)});

                db.execSQL("UPDATE sach SET SoLuong = SoLuong - ? WHERE MaSach = ?",
                        new Object[]{listSoLuong.get(i), listMaSach.get(i)});
            }

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db.inTransaction()) db.endTransaction();
            db.close();
        }
    }

    public List<SpinnerItem> layDanhSachSpinner(String table, String idCol, String nameCol, String title) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + idCol + ", " + nameCol + " FROM " + table, null);

        List<SpinnerItem> list = new ArrayList<>();
        list.add(new SpinnerItem("", title));

        while (cursor.moveToNext()) {
            list.add(new SpinnerItem(cursor.getString(0), cursor.getString(1)));
        }

        cursor.close();
        db.close();
        return list;
    }

    public boolean kiemTraDocGiaCoTheThuVien(String maDG) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM thethuvien WHERE MaDG = ? AND TrangThai = 'Còn hiệu lực'", new String[]{maDG});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    public boolean kiemTraDocGiaDangMuonSach(String maDG) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM muontra WHERE MaDG = ? AND TrangThai = 'Chưa trả'", new String[]{maDG});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }
    public List<MuonTra> layDanhSachMuonTraTheoDocGia(String maDG, String trangThai) {
        List<MuonTra> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT mt.MaMT, mt.MaDG, mt.MaNV, mt.NgayMuon, mt.HanTra, mt.TrangThai, " +
                "d.TenDG, nv.TenNV " +
                "FROM muontra mt " +
                "JOIN docgia d ON mt.MaDG = d.MaDG " +
                "LEFT JOIN nhanvien nv ON mt.MaNV = nv.MaNV " +
                "WHERE mt.MaDG = ?";

        List<String> args = new ArrayList<>();
        args.add(maDG);

        if (trangThai != null) {
            if (trangThai.equals("Đã trả") || trangThai.equals("Chưa trả")) {
                sql += " AND mt.TrangThai = ?";
                args.add(trangThai);
            } else if (trangThai.equals("Quá hạn")) {
                sql += " AND mt.TrangThai = 'Chưa trả' AND mt.HanTra < date('now')";
            }
        }

        sql += " ORDER BY mt.NgayMuon DESC";

        Cursor cursor = db.rawQuery(sql, args.toArray(new String[0]));

        while (cursor.moveToNext()) {
            MuonTra item = new MuonTra();
            item.setMaMT(cursor.getString(0));
            item.setMaDG(cursor.getString(1));
            item.setMaNV(cursor.getString(2));
            item.setNgayMuon(cursor.getString(3));
            item.setHanTra(cursor.getString(4));
            item.setTrangThai(cursor.getString(5));
            item.setTenDG(cursor.getString(6));
            item.setTenNV(cursor.getString(7));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }
    public List<MuonTra> timKiemMuonTra(String query) {
        List<MuonTra> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT mt.MaMT, mt.MaDG, mt.MaNV, mt.NgayMuon, mt.HanTra, mt.TrangThai, " +
                "d.TenDG, nv.TenNV " +
                "FROM muontra mt " +
                "JOIN docgia d ON mt.MaDG = d.MaDG " +
                "JOIN nhanvien nv ON mt.MaNV = nv.MaNV " +
                "WHERE mt.MaMT LIKE ? OR d.TenDG LIKE ? OR nv.TenNV LIKE ? OR mt.TrangThai LIKE ? " +
                "ORDER BY mt.MaMT ASC";

        String search = "%" + query + "%";
        Cursor cursor = db.rawQuery(sql, new String[]{search, search, search, search});

        while (cursor.moveToNext()) {
            MuonTra item = new MuonTra();
            item.setMaMT(cursor.getString(0));
            item.setMaDG(cursor.getString(1));
            item.setMaNV(cursor.getString(2));
            item.setNgayMuon(cursor.getString(3));
            item.setHanTra(cursor.getString(4));
            item.setTrangThai(cursor.getString(5));
            item.setTenDG(cursor.getString(6));
            item.setTenNV(cursor.getString(7));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }
}
