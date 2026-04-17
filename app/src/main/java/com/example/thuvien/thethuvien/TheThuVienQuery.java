package com.example.thuvien.thethuvien;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thuvien.common.SpinnerItem;
import com.example.thuvien.database.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TheThuVienQuery {

    private final SQLiteHelper dbHelper;

    public TheThuVienQuery(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public List<TheThuVien> layDanhSachThe() {
        List<TheThuVien> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT t.MaThe, t.MaDG, t.NgayCap, t.NgayHetHan, t.TrangThai, d.TenDG " +
                "FROM thethuvien t " +
                "JOIN docgia d ON t.MaDG = d.MaDG " +
                "ORDER BY t.MaThe ASC";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            TheThuVien item = new TheThuVien();
            item.setMaThe(cursor.getString(0));
            item.setMaDG(cursor.getString(1));
            item.setNgayCap(cursor.getString(2));
            item.setNgayHetHan(cursor.getString(3));
            item.setTrangThai(cursor.getString(4));
            item.setTenDG(cursor.getString(5));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public List<TheThuVien> timKiemThe(String keyword) {
        List<TheThuVien> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String key = "%" + keyword + "%";

        String sql = "SELECT t.MaThe, t.MaDG, t.NgayCap, t.NgayHetHan, t.TrangThai, d.TenDG " +
                "FROM thethuvien t " +
                "JOIN docgia d ON t.MaDG = d.MaDG " +
                "WHERE t.MaThe LIKE ? OR d.TenDG LIKE ? " +
                "ORDER BY t.MaThe ASC";

        Cursor cursor = db.rawQuery(sql, new String[]{key, key});

        while (cursor.moveToNext()) {
            TheThuVien item = new TheThuVien();
            item.setMaThe(cursor.getString(0));
            item.setMaDG(cursor.getString(1));
            item.setNgayCap(cursor.getString(2));
            item.setNgayHetHan(cursor.getString(3));
            item.setTrangThai(cursor.getString(4));
            item.setTenDG(cursor.getString(5));
            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public TheThuVien layThongTinTheoMa(String maThe) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT MaThe, MaDG, NgayCap, NgayHetHan, TrangThai " +
                "FROM thethuvien WHERE MaThe = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{maThe});
        TheThuVien item = null;

        if (cursor.moveToFirst()) {
            item = new TheThuVien();
            item.setMaThe(cursor.getString(0));
            item.setMaDG(cursor.getString(1));
            item.setNgayCap(cursor.getString(2));
            item.setNgayHetHan(cursor.getString(3));
            item.setTrangThai(cursor.getString(4));
        }

        cursor.close();
        db.close();
        return item;
    }
    public boolean dangMuonSach(String maDG) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM muontra WHERE MaDG = ? AND TrangThai = 'Chưa trả'",
                new String[]{maDG}
        );

        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
    public boolean themThe(TheThuVien item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            String trangThai = tinhTrangThaiThe(item.getNgayHetHan());
            db.execSQL(
                    "INSERT INTO thethuvien (MaThe, MaDG, NgayCap, NgayHetHan, TrangThai) VALUES (?, ?, ?, ?, ?)",
                    new Object[]{
                            item.getMaThe(),
                            item.getMaDG(),
                            item.getNgayCap(),
                            item.getNgayHetHan(),
                            trangThai
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

    public boolean capNhatThe(TheThuVien item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            String trangThai = tinhTrangThaiThe(item.getNgayHetHan());
            db.execSQL(
                    "UPDATE thethuvien SET MaDG = ?, NgayCap = ?, NgayHetHan = ?, TrangThai = ? WHERE MaThe = ?",
                    new Object[]{
                            item.getMaDG(),
                            item.getNgayCap(),
                            item.getNgayHetHan(),
                            trangThai,
                            item.getMaThe()
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

    private String tinhTrangThaiThe(String ngayHetHanStr) {
        if (ngayHetHanStr == null || ngayHetHanStr.isEmpty()) return "Hết hiệu lực";
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sdfShort = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date today = cal.getTime();

            Date ngayHetHan = null;
            if (ngayHetHanStr.length() == 10) {
                ngayHetHan = sdf.parse(ngayHetHanStr);
            } else if (ngayHetHanStr.length() == 8) {
                ngayHetHan = sdfShort.parse(ngayHetHanStr);
            }

            if (ngayHetHan != null && !ngayHetHan.before(today)) {
                return "Còn hiệu lực";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Hết hiệu lực";
    }

    public boolean xoaThe(String maThe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int row = db.delete("thethuvien", "MaThe = ?", new String[]{maThe});
            db.close();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public String taoMaMoi() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT MaThe FROM thethuvien ORDER BY MaThe DESC LIMIT 1", null);

        String ma = "TTV001";

        if (c.moveToFirst()) {
            try {
                int so = Integer.parseInt(c.getString(0).replaceAll("[^0-9]", "")) + 1;
                ma = String.format(Locale.getDefault(), "TTV%03d", so);
            } catch (Exception e) {
                ma = "TTV001";
            }
        }

        c.close();
        db.close();
        return ma;
    }

    public List<SpinnerItem> layDanhSachDocGiaSpinner() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MaDG, TenDG FROM docgia ORDER BY TenDG ASC", null);

        List<SpinnerItem> list = new ArrayList<>();
        list.add(new SpinnerItem("", "--- Chọn độc giả ---"));

        while (cursor.moveToNext()) {
            list.add(new SpinnerItem(cursor.getString(0), cursor.getString(1)));
        }

        cursor.close();
        db.close();
        return list;
    }

    public void tuDongCapNhatTrangThaiThe() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT MaThe, NgayHetHan, TrangThai FROM thethuvien", null);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sdfShort = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date today = cal.getTime();

            while (cursor.moveToNext()) {
                String maThe = cursor.getString(0);
                String ngayHetHanStr = cursor.getString(1);
                String trangThaiHienTai = cursor.getString(2);

                Date ngayHetHan = null;
                try {
                    if (ngayHetHanStr != null) {
                        if (ngayHetHanStr.length() == 10) {
                            ngayHetHan = sdf.parse(ngayHetHanStr);
                        } else if (ngayHetHanStr.length() == 8) {
                            ngayHetHan = sdfShort.parse(ngayHetHanStr);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (ngayHetHan != null) {
                    String trangThaiMoi = ngayHetHan.before(today) ? "Hết hiệu lực" : "Còn hiệu lực";

                    if (!trangThaiMoi.equals(trangThaiHienTai)) {
                        db.execSQL("UPDATE thethuvien SET TrangThai = ? WHERE MaThe = ?", new Object[]{trangThaiMoi, maThe});
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }
}
