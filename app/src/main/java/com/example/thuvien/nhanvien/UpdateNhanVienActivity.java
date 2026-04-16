package com.example.thuvien.nhanvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

import java.util.Calendar;
import java.util.List;

public class UpdateNhanVienActivity extends AppCompatActivity {

    EditText edtTenNV, edtQueQuan, edtNamSinh, edtEmail, edtSdt, edtUser, edtPass;
    Spinner spnGioiTinh, spnVaiTro;
    Button btnUpdate;
    
    NhanVienQuery nhanVienQuery;
    String maNV;

    // Dữ liệu mảng đơn giản
    String[] arrGioiTinh = {"Nam", "Nữ"};
    String[] arrVaiTro = {"Thủ thư", "Quản lý"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nhanvien);

        edtTenNV = findViewById(R.id.edtTenNV);
        edtQueQuan = findViewById(R.id.edtQueQuan);
        edtNamSinh = findViewById(R.id.edtNamSinh);
        edtEmail = findViewById(R.id.edtEmail);
        edtSdt = findViewById(R.id.edtSdt);
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        
        spnGioiTinh = findViewById(R.id.spnGioiTinh);
        spnVaiTro = findViewById(R.id.spnVaiTro);
        
        btnUpdate = findViewById(R.id.btnUpdate);
        
        nhanVienQuery = new NhanVienQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Đọc dữ liệu Intent gửi qua
        Intent intent = getIntent();
        maNV = intent.getStringExtra("maNV");
        edtTenNV.setText(intent.getStringExtra("tenNV"));
        edtQueQuan.setText(intent.getStringExtra("queQuan"));
        edtNamSinh.setText(intent.getStringExtra("namSinh"));
        edtEmail.setText(intent.getStringExtra("email"));
        edtSdt.setText(intent.getStringExtra("sdt"));
        edtUser.setText(intent.getStringExtra("user"));
        edtPass.setText(intent.getStringExtra("pass"));
        
        String gioiTinhCu = intent.getStringExtra("gioiTinh");
        String vaiTroCu = intent.getStringExtra("vaiTro");

        // Thiết lập Adapter
        ArrayAdapter<String> adapterGioiTinh = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrGioiTinh);
        adapterGioiTinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGioiTinh.setAdapter(adapterGioiTinh);
        
        // So sánh chuỗi (thủ công) để gán vị trí Spinner Chọn
        if (gioiTinhCu != null && gioiTinhCu.equals("Nữ")) {
            spnGioiTinh.setSelection(1);
        } else {
            spnGioiTinh.setSelection(0); // Nam
        }

        ArrayAdapter<String> adapterVaiTro = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrVaiTro);
        adapterVaiTro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnVaiTro.setAdapter(adapterVaiTro);
        
        if (vaiTroCu != null && vaiTroCu.equals("Quản lý")) {
            spnVaiTro.setSelection(1);
        } else {
            spnVaiTro.setSelection(0); // Thủ thư
        }

        // Bấm Cập nhật
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<NhanVien> list = nhanVienQuery.layDanhSachNhanVien();
                int year = Calendar.getInstance().get(Calendar.YEAR);

                String ten = edtTenNV.getText().toString().trim();
                String que = edtQueQuan.getText().toString().trim();
                String nam = edtNamSinh.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String sdt = edtSdt.getText().toString().trim();
                String user = edtUser.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();

                if (maNV == null || maNV.equals("")) {
                    Toast.makeText(UpdateNhanVienActivity.this, "Lỗi mã!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ten.isEmpty()) {
                    edtTenNV.setError("Nhập tên nhân viên");
                    edtTenNV.requestFocus();
                    return;
                }

                if (nam.isEmpty()) {
                    edtNamSinh.setError("Nhập năm sinh");
                    edtNamSinh.requestFocus();
                    return;
                }

                int namSinhInt;
                try {
                    namSinhInt = Integer.parseInt(nam);
                } catch (NumberFormatException e) {
                    edtNamSinh.setError("Năm sinh phải là số");
                    edtNamSinh.requestFocus();
                    return;
                }

                if (namSinhInt < 1900 || namSinhInt > year) {
                    edtNamSinh.setError("Năm sinh không hợp lệ");
                    edtNamSinh.requestFocus();
                    return;
                }

                if (que.isEmpty()) {
                    edtQueQuan.setError("Nhập quê quán");
                    edtQueQuan.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    edtEmail.setError("Nhập email");
                    edtEmail.requestFocus();
                    return;
                }

                if (!email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                    edtEmail.setError("Email không đúng định dạng");
                    edtEmail.requestFocus();
                    return;
                }

                if (sdt.isEmpty()) {
                    edtSdt.setError("Nhập số điện thoại");
                    edtSdt.requestFocus();
                    return;
                }

                if (!sdt.matches("0\\d{9,11}")) {
                    edtSdt.setError("SĐT phải từ 10-12 số, bắt đầu bằng 0");
                    edtSdt.requestFocus();
                    return;
                }

                if (user.isEmpty()) {
                    edtUser.setError("Nhập tài khoản");
                    edtUser.requestFocus();
                    return;
                }

                if (pass.isEmpty()) {
                    edtPass.setError("Nhập mật khẩu");
                    edtPass.requestFocus();
                    return;
                }

                for (NhanVien nvCheck : list) {
                    if (nvCheck.getMaNV() != null && nvCheck.getMaNV().equals(maNV)) {
                        continue;
                    }

                    if (nvCheck.getSdt() != null && nvCheck.getSdt().equals(sdt)) {
                        edtSdt.setError("SĐT đã tồn tại");
                        edtSdt.requestFocus();
                        return;
                    }

                    if (nvCheck.getEmail() != null && nvCheck.getEmail().equalsIgnoreCase(email)) {
                        edtEmail.setError("Email đã tồn tại");
                        edtEmail.requestFocus();
                        return;
                    }

                    if (nvCheck.getUser() != null && nvCheck.getUser().equalsIgnoreCase(user)) {
                        edtUser.setError("Tài khoản đã tồn tại");
                        edtUser.requestFocus();
                        return;
                    }
                }

                if (spnGioiTinh.getSelectedItemPosition() < 0 || spnVaiTro.getSelectedItemPosition() < 0) {
                    Toast.makeText(UpdateNhanVienActivity.this, "Vui lòng chọn giới tính và vai trò!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String gioiTinhMoi = arrGioiTinh[spnGioiTinh.getSelectedItemPosition()];
                String vaiTroMoi = arrVaiTro[spnVaiTro.getSelectedItemPosition()];

                NhanVien nvMoi = new NhanVien(maNV, ten, que, gioiTinhMoi, nam, vaiTroMoi, email, sdt, user, pass);
                
                boolean ketQua = nhanVienQuery.suaNhanVien(nvMoi);

                if (ketQua == true) {
                    Toast.makeText(UpdateNhanVienActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateNhanVienActivity.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}