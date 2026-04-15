package com.example.thuvien.docgia;

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
import com.example.thuvien.common.SpinnerItem;

import java.util.Calendar;
import java.util.List;

public class AddDocGiaActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTenDG, edtNamSinh, edtDiaChi, edtEmail, edtSdt;
    Spinner spnGioiTinh, spnKhoa, spnLop;
    Button btnSaveDocGia;

    DocGiaQuery docGiaQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_docgia);

        imgBack = findViewById(R.id.imgBack);
        edtTenDG = findViewById(R.id.edtTenDG);
        edtNamSinh = findViewById(R.id.edtNamSinh);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtEmail = findViewById(R.id.edtEmail);
        edtSdt = findViewById(R.id.edtSdt);
        spnGioiTinh = findViewById(R.id.spnGioiTinh);
        spnKhoa = findViewById(R.id.spnKhoa);
        spnLop = findViewById(R.id.spnLop);
        btnSaveDocGia = findViewById(R.id.btnSaveDocGia);

        docGiaQuery = new DocGiaQuery(this);

        loadKhoaSpinner();
        loadGioiTinhSpinner();
        setupKhoaLopDependency();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSaveDocGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luuDocGia();
            }
        });
    }

    private void loadKhoaSpinner() {
        setSpinner(spnKhoa, docGiaQuery.layDanhSachKhoaSpinner());
        setSpinner(spnLop, docGiaQuery.layDanhSachLopTheoKhoa(""));
    }

    private void setupKhoaLopDependency() {
        spnKhoa.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem khoa = (SpinnerItem) spnKhoa.getSelectedItem();
                if (khoa != null && !khoa.getId().isEmpty()) {
                    setSpinner(spnLop, docGiaQuery.layDanhSachLopTheoKhoa(khoa.getId()));
                } else {
                    setSpinner(spnLop, docGiaQuery.layDanhSachLopTheoKhoa(""));
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void loadSpinnerData() {
        setSpinner(spnKhoa, docGiaQuery.layDanhSachSpinner("khoa", "MaKhoa", "TenKhoa", "--- Chọn khoa ---"));
        setSpinner(spnLop, docGiaQuery.layDanhSachSpinner("lop", "MaLop", "TenLop", "--- Chọn lớp ---"));
    }

    private void loadGioiTinhSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                docGiaQuery.layDanhSachGioiTinh()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGioiTinh.setAdapter(adapter);
    }

    private void setSpinner(Spinner spinner, List<SpinnerItem> list) {
        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                list
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void luuDocGia() {
        List<DocGia> list = docGiaQuery.layDanhSachDocGia();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String tenDG = edtTenDG.getText().toString().trim();
        String namSinh = edtNamSinh.getText().toString().trim();
        String gioiTinh = spnGioiTinh.getSelectedItem().toString();
        String diaChi = edtDiaChi.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String sdt = edtSdt.getText().toString().trim();

        if (tenDG.isEmpty()) {
            edtTenDG.setError("Nhập tên độc giả");
            edtTenDG.requestFocus();
            return;
        }
        if (namSinh.isEmpty()) {
            edtNamSinh.setError("Nhập năm sinh");
            edtNamSinh.requestFocus();
            return;
        }

        int namSinhInt;
        try {
            namSinhInt = Integer.parseInt(namSinh);
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
        if (diaChi.isEmpty()) {
            edtDiaChi.setError("Nhập địa chỉ");
            edtDiaChi.requestFocus();
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
        for (DocGia dg : list) {

            if (dg.getSdt().equals(sdt)) {
                edtSdt.setError("SĐT đã tồn tại");
                edtSdt.requestFocus();
                return;
            }

            if (dg.getEmail().equalsIgnoreCase(email)) {
                edtEmail.setError("Email đã tồn tại");
                edtEmail.requestFocus();
                return;
            }
        }
            SpinnerItem khoa = (SpinnerItem) spnKhoa.getSelectedItem();
            SpinnerItem lop = (SpinnerItem) spnLop.getSelectedItem();

            if (khoa.getId().isEmpty() || lop.getId().isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn khoa và lớp", Toast.LENGTH_SHORT).show();
                return;
            }

            DocGia item = new DocGia();
            item.setMaDG(docGiaQuery.taoMaMoi());
            item.setMaKhoa(khoa.getId());
            item.setMaLop(lop.getId());
            item.setTenDG(tenDG);
            item.setNamSinh(namSinh);
            item.setGioiTinh(gioiTinh);
            item.setDiaChi(diaChi);
            item.setEmail(email);
            item.setSdt(sdt);

            boolean result = docGiaQuery.themDocGia(item);

            if (result) {
                Toast.makeText(this, "Thêm độc giả thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm độc giả thất bại!", Toast.LENGTH_SHORT).show();
            }
        }
    }
