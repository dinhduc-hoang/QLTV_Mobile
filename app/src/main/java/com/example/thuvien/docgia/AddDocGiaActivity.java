package com.example.thuvien.docgia;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

    EditText edtTenDG, edtNamSinh, edtDiaChi, edtEmail, edtSdt;
    Spinner spnGioiTinh, spnKhoa, spnLop;
    Button btnSave;

    DocGiaQuery docGiaQuery;
    String[] arrGioiTinh = {"Nam", "Nữ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_docgia);

        edtTenDG = findViewById(R.id.edtTenDG);
        edtNamSinh = findViewById(R.id.edtNamSinh);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtEmail = findViewById(R.id.edtEmail);
        edtSdt = findViewById(R.id.edtSdt);

        spnGioiTinh = findViewById(R.id.spnGioiTinh);
        spnKhoa = findViewById(R.id.spnKhoa);
        spnLop = findViewById(R.id.spnLop);

        btnSave = findViewById(R.id.btnSaveDocGia);

        docGiaQuery = new DocGiaQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupGioiTinhSpinner();
        loadKhoaSpinner();
        loadLopTheoKhoa("");

        spnKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem khoa = (SpinnerItem) spnKhoa.getSelectedItem();
                if (khoa != null) {
                    loadLopTheoKhoa(khoa.getId());
                } else {
                    loadLopTheoKhoa("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themDocGia();
            }
        });
    }

    private void setupGioiTinhSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrGioiTinh);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGioiTinh.setAdapter(adapter);
    }

    private void loadKhoaSpinner() {
        List<SpinnerItem> listKhoa = docGiaQuery.layDanhSachKhoaSpinner();
        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listKhoa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnKhoa.setAdapter(adapter);
    }

    private void loadLopTheoKhoa(String maKhoa) {
        List<SpinnerItem> listLop = docGiaQuery.layDanhSachLopTheoKhoa(maKhoa);
        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listLop);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLop.setAdapter(adapter);
    }

    private void themDocGia() {
        List<DocGia> list = docGiaQuery.layDanhSachDocGia();
        int year = Calendar.getInstance().get(Calendar.YEAR);

        String tenDG = edtTenDG.getText().toString().trim();
        String namSinh = edtNamSinh.getText().toString().trim();
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

        for (DocGia dgCheck : list) {
            if (dgCheck.getSdt() != null && dgCheck.getSdt().equals(sdt)) {
                edtSdt.setError("SĐT đã tồn tại");
                edtSdt.requestFocus();
                return;
            }

            if (dgCheck.getEmail() != null && dgCheck.getEmail().equalsIgnoreCase(email)) {
                edtEmail.setError("Email đã tồn tại");
                edtEmail.requestFocus();
                return;
            }
        }

        if (arrGioiTinh.length == 0 || spnGioiTinh.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Vui lòng chọn giới tính!", Toast.LENGTH_SHORT).show();
            return;
        }

        SpinnerItem khoa = (SpinnerItem) spnKhoa.getSelectedItem();
        SpinnerItem lop = (SpinnerItem) spnLop.getSelectedItem();
        if (khoa == null || lop == null || khoa.getId().equals("") || lop.getId().equals("")) {
            Toast.makeText(this, "Vui lòng chọn khoa và lớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        String gioiTinh = arrGioiTinh[spnGioiTinh.getSelectedItemPosition()];

        DocGia dg = new DocGia();
        dg.setMaDG(docGiaQuery.taoMaMoi());
        dg.setMaKhoa(khoa.getId());
        dg.setMaLop(lop.getId());
        dg.setTenDG(tenDG);
        dg.setNamSinh(namSinh);
        dg.setGioiTinh(gioiTinh);
        dg.setDiaChi(diaChi);
        dg.setEmail(email);
        dg.setSdt(sdt);

        boolean ketQua = docGiaQuery.themDocGia(dg);
        if (ketQua == true) {
            Toast.makeText(this, "Thêm thành công! Mã: " + dg.getMaDG(), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
