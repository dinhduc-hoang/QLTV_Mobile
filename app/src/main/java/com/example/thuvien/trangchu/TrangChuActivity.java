package com.example.thuvien.trangchu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.docgia.DocGiaActivity;
import com.example.thuvien.kesach.KeSachActivity;
import com.example.thuvien.khoa.KhoaActivity;
import com.example.thuvien.login.LoginActivity;
import com.example.thuvien.lop.LopActivity;
import com.example.thuvien.muontra.MuonTraActivity;
import com.example.thuvien.muontra.MuonTraQuery;
import com.example.thuvien.ngonngu.NgonNguActivity;
import com.example.thuvien.nhanvien.NhanVienActivity;
import com.example.thuvien.nhaxuatban.NXBActivity;
import com.example.thuvien.sach.SachActivity;
import com.example.thuvien.tacgia.TacGiaActivity;
import com.example.thuvien.theloai.TheLoaiActivity;
import com.example.thuvien.thethuvien.TheThuVienActivity;
import com.example.thuvien.thethuvien.TheThuVienQuery;
import com.example.thuvien.thongke.ThongKeActivity;

public class TrangChuActivity extends AppCompatActivity {

    TextView tvWelcome, tvRole, tvLogout;
    LinearLayout layoutSach, layoutTheLoai, layoutNhanVien, layoutKeSach, layoutThongKe;
    LinearLayout layoutNXB, layoutDocGia, layoutNgonNgu, layoutMuonTra, layoutTheThuVien;
    LinearLayout layoutTacGia, layoutLop, layoutKhoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvRole = findViewById(R.id.tvRole);
        tvLogout = findViewById(R.id.tvLogout);

        layoutSach = findViewById(R.id.layoutSach);
        layoutTheLoai = findViewById(R.id.layoutTheLoai);
        layoutNhanVien = findViewById(R.id.layoutNhanVien);
        layoutKeSach = findViewById(R.id.layoutKeSach);
        layoutThongKe = findViewById(R.id.layoutThongKe);
        layoutNXB = findViewById(R.id.layoutNXB);
        layoutDocGia = findViewById(R.id.layoutDocGia);
        layoutNgonNgu = findViewById(R.id.layoutNgonNgu);
        layoutMuonTra = findViewById(R.id.layoutMuonTra);
        layoutTheThuVien = findViewById(R.id.layoutTheThuVien);
        layoutTacGia = findViewById(R.id.layoutTacGia);
        layoutLop = findViewById(R.id.layoutLop);
        layoutKhoa = findViewById(R.id.layoutKhoa);

        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        tvWelcome.setText("Xin chào, " + sp.getString("TenNV", "Nhân viên"));
        tvRole.setText("Vai trò: " + sp.getString("VaiTro", "Không rõ"));

        new TheThuVienQuery(this).tuDongCapNhatTrangThaiThe();
        new MuonTraQuery(this).tuDongCapNhatTrangThaiQuaHan();

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("UserSession", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(TrangChuActivity.this, LoginActivity.class));
                finish();
            }
        });

        setOpen(layoutSach, SachActivity.class);
        setOpen(layoutMuonTra, MuonTraActivity.class);
        setOpen(layoutTheThuVien, TheThuVienActivity.class);
        setOpen(layoutDocGia, DocGiaActivity.class);
        setOpen(layoutTacGia, TacGiaActivity.class);
        setOpen(layoutTheLoai, TheLoaiActivity.class);
        setOpen(layoutNXB, NXBActivity.class);
        setOpen(layoutNgonNgu, NgonNguActivity.class);
        setOpen(layoutKeSach, KeSachActivity.class);
        setOpen(layoutLop, LopActivity.class);
        setOpen(layoutKhoa, KhoaActivity.class);
        setOpen(layoutNhanVien, NhanVienActivity.class);
        setOpen(layoutThongKe, ThongKeActivity.class);
    }

    private void setOpen(LinearLayout layout, final Class<?> cls) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(TrangChuActivity.this, cls));
            }
        });
    }
}
