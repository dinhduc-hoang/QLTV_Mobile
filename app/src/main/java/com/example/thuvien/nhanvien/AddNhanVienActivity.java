package com.example.thuvien.nhanvien;

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

public class AddNhanVienActivity extends AppCompatActivity {

    EditText edtTenNV, edtQueQuan, edtNamSinh, edtEmail, edtSdt, edtUser, edtPass;
    Spinner spnGioiTinh, spnVaiTro;
    Button btnSave;

    NhanVienQuery nhanVienQuery;

    String[] arrGioiTinh = {"Nam", "Nữ"};
    String[] arrVaiTro = {"Thủ thư", "Quản lý"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nhanvien);

        edtTenNV = findViewById(R.id.edtTenNV);
        edtQueQuan = findViewById(R.id.edtQueQuan);
        edtNamSinh = findViewById(R.id.edtNamSinh);
        edtEmail = findViewById(R.id.edtEmail);
        edtSdt = findViewById(R.id.edtSdt);
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);

        spnGioiTinh = findViewById(R.id.spnGioiTinh);
        spnVaiTro = findViewById(R.id.spnVaiTro);

        btnSave = findViewById(R.id.btnSave);

        nhanVienQuery = new NhanVienQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayAdapter<String> adapterGioiTinh = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrGioiTinh);
        adapterGioiTinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGioiTinh.setAdapter(adapterGioiTinh);

        ArrayAdapter<String> adapterVaiTro = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrVaiTro);
        adapterVaiTro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnVaiTro.setAdapter(adapterVaiTro);

        btnSave.setOnClickListener(new View.OnClickListener() {
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

                int viTriGioiTinh = spnGioiTinh.getSelectedItemPosition();
                String gioiTinh = arrGioiTinh[viTriGioiTinh];

                int viTriVaiTro = spnVaiTro.getSelectedItemPosition();
                String vaiTro = arrVaiTro[viTriVaiTro];

                String maNV = nhanVienQuery.taoMaNhanVienMoi();
                NhanVien nv = new NhanVien(maNV, ten, que, gioiTinh, nam, vaiTro, email, sdt, user, pass);

                boolean ketQua = nhanVienQuery.themNhanVien(nv);

                if (ketQua == true) {
                    Toast.makeText(AddNhanVienActivity.this, "Thêm thành công! Mã: " + maNV, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddNhanVienActivity.this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
