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

public class UpdateNhanVienActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTenNV, edtQueQuan, edtNamSinh, edtEmail, edtSdt, edtUser, edtPass;
    Spinner spnGioiTinh, spnVaiTro;
    Button btnUpdate;

    String maNV;
    NhanVienQuery nhanVienQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nhanvien);

        imgBack = findViewById(R.id.imgBack);
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
        maNV = getIntent().getStringExtra("MaNV");

        loadSpinnerData();
        loadThongTin();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capNhatNhanVien();
            }
        });
    }

    private void loadSpinnerData() {
        ArrayAdapter<String> gioiTinhAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nhanVienQuery.layDanhSachGioiTinh()
        );
        gioiTinhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGioiTinh.setAdapter(gioiTinhAdapter);

        ArrayAdapter<String> vaiTroAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nhanVienQuery.layDanhSachVaiTro()
        );
        vaiTroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnVaiTro.setAdapter(vaiTroAdapter);
    }

    private void loadThongTin() {
        NhanVien item = nhanVienQuery.layThongTinTheoMa(maNV);
        if (item != null) {
            edtTenNV.setText(item.getTenNV());
            edtQueQuan.setText(item.getQueQuan());
            edtNamSinh.setText(item.getNamSinh());
            edtEmail.setText(item.getEmail());
            edtSdt.setText(item.getSdt());
            edtUser.setText(item.getUser());
            edtPass.setText(item.getPass());
            setSpinnerSelected(spnGioiTinh, item.getGioiTinh());
            setSpinnerSelected(spnVaiTro, item.getVaiTro());
        }
    }

    private void setSpinnerSelected(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            String item = spinner.getItemAtPosition(i).toString();
            if (item.equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void capNhatNhanVien() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String tenNV = edtTenNV.getText().toString().trim();
        String queQuan = edtQueQuan.getText().toString().trim();
        String namSinh = edtNamSinh.getText().toString().trim();
        int namSinhInt = Integer.parseInt(namSinh);
        String email = edtEmail.getText().toString().trim();
        String sdt = edtSdt.getText().toString().trim();
        String user = edtUser.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String gioiTinh = spnGioiTinh.getSelectedItem().toString();
        String vaiTro = spnVaiTro.getSelectedItem().toString();

        if (tenNV.isEmpty()) {
            edtTenNV.setError("Nhập tên nhân viên");
            edtTenNV.requestFocus();
            return;
        }
        if (queQuan.isEmpty()) {
            edtQueQuan.setError("Nhập quê quán");
            edtQueQuan.requestFocus();
            return;
        }
        if (namSinh.isEmpty()) {
            edtNamSinh.setError("Nhập năm sinh");
            edtNamSinh.requestFocus();
            return;
        }
        if (namSinhInt < 1900 || namSinhInt > year || (year - namSinhInt) < 18 ) {
            edtNamSinh.setError("Năm sinh không hợp lệ");
            edtNamSinh.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtEmail.setError("Nhập email");
            edtEmail.requestFocus();
            return;
        }
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            edtEmail.setError("Email không đúng định dạng");
            edtEmail.requestFocus();
            return;
        }
        if (sdt.isEmpty()) {
            edtSdt.setError("Nhập số điện thoại");
            edtSdt.requestFocus();
            return;
        }
        if (!sdt.matches("0\\d{9}")) {
            edtSdt.setError("SĐT phải 10 số, bắt đầu bằng 0");
            edtSdt.requestFocus();
            return;
        }
        if (user.isEmpty()) {
            edtUser.setError("Nhập user");
            edtUser.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            edtPass.setError("Nhập mật khẩu");
            edtPass.requestFocus();
            return;
        }

        List<NhanVien> list = nhanVienQuery.layDanhSachNhanVien();

        for (NhanVien nv : list) {

            if (nv.getSdt().equals(sdt)) {
                edtSdt.setError("SĐT đã tồn tại");
                edtSdt.requestFocus();
                return;
            }

            if (nv.getEmail().equalsIgnoreCase(email)) {
                edtEmail.setError("Email đã tồn tại");
                edtEmail.requestFocus();
                return;
            }

            if (nv.getUser().equalsIgnoreCase(user)) {
                edtUser.setError("User đã tồn tại");
                edtUser.requestFocus();
                return;
            }
        }
        NhanVien item = new NhanVien();
        item.setMaNV(maNV);
        item.setTenNV(tenNV);
        item.setQueQuan(queQuan);
        item.setGioiTinh(gioiTinh);
        item.setNamSinh(namSinh);
        item.setVaiTro(vaiTro);
        item.setEmail(email);
        item.setSdt(sdt);
        item.setUser(user);
        item.setPass(pass);

        boolean result = nhanVienQuery.capNhatNhanVien(item);

        if (result) {
            Toast.makeText(this, "Cập nhật nhân viên thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật nhân viên thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}