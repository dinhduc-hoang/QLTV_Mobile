package com.example.thuvien.docgia;

import android.content.Intent;
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

public class UpdateDocGiaActivity extends AppCompatActivity {

    EditText edtTenDG, edtNamSinh, edtDiaChi, edtEmail, edtSdt;
    Spinner spnGioiTinh, spnKhoa, spnLop;
    Button btnUpdate;

    DocGiaQuery docGiaQuery;

    String maDG;
    String maKhoaCu;
    String maLopCu;

    String[] arrGioiTinh = {"Nam", "Nữ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_docgia);

        edtTenDG = findViewById(R.id.edtTenDG);
        edtNamSinh = findViewById(R.id.edtNamSinh);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtEmail = findViewById(R.id.edtEmail);
        edtSdt = findViewById(R.id.edtSdt);

        spnGioiTinh = findViewById(R.id.spnGioiTinh);
        spnKhoa = findViewById(R.id.spnKhoa);
        spnLop = findViewById(R.id.spnLop);

        btnUpdate = findViewById(R.id.btnUpdateDocGia);
        docGiaQuery = new DocGiaQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        maDG = intent.getStringExtra("maDG");
        maKhoaCu = intent.getStringExtra("maKhoa");
        maLopCu = intent.getStringExtra("maLop");
        edtTenDG.setText(intent.getStringExtra("tenDG"));
        edtNamSinh.setText(intent.getStringExtra("namSinh"));
        edtDiaChi.setText(intent.getStringExtra("diaChi"));
        edtEmail.setText(intent.getStringExtra("email"));
        edtSdt.setText(intent.getStringExtra("sdt"));

        String gioiTinhCu = intent.getStringExtra("gioiTinh");

        setupGioiTinhSpinner(gioiTinhCu);
        loadKhoaSpinner(maKhoaCu);
        loadLopSpinner(maKhoaCu, maLopCu);
        setupKhoaLopDependency();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suaDocGia();
            }
        });
    }

    private void setupGioiTinhSpinner(String gioiTinhCu) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrGioiTinh);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGioiTinh.setAdapter(adapter);

        if (gioiTinhCu != null && gioiTinhCu.equals("Nữ")) {
            spnGioiTinh.setSelection(1);
        } else {
            spnGioiTinh.setSelection(0);
        }
    }

    private void loadKhoaSpinner(String maKhoaCanChon) {
        List<SpinnerItem> listKhoa = docGiaQuery.layDanhSachKhoaSpinner();
        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listKhoa);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnKhoa.setAdapter(adapter);
        setSpinnerSelected(spnKhoa, maKhoaCanChon);
    }

    private void loadLopSpinner(String maKhoa, String maLopCanChon) {
        List<SpinnerItem> listLop = docGiaQuery.layDanhSachLopTheoKhoa(maKhoa);
        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listLop);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLop.setAdapter(adapter);
        setSpinnerSelected(spnLop, maLopCanChon);
    }

    private void setupKhoaLopDependency() {
        spnKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem khoa = (SpinnerItem) spnKhoa.getSelectedItem();
                if (khoa != null) {
                    loadLopSpinner(khoa.getId(), "");
                } else {
                    loadLopSpinner("", "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setSpinnerSelected(Spinner spinner, String selectedId) {
        if (selectedId == null) {
            return;
        }

        for (int i = 0; i < spinner.getCount(); i++) {
            Object obj = spinner.getItemAtPosition(i);
            if (obj instanceof SpinnerItem) {
                SpinnerItem spin = (SpinnerItem) obj;
                if (spin.getId() != null && spin.getId().equalsIgnoreCase(selectedId.trim())) {
                    spinner.setSelection(i);
                    return;
                }
            }
        }
    }

    private void suaDocGia() {
        List<DocGia> list = docGiaQuery.layDanhSachDocGia();
        int year = Calendar.getInstance().get(Calendar.YEAR);

        String tenDG = edtTenDG.getText().toString().trim();
        String namSinh = edtNamSinh.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String sdt = edtSdt.getText().toString().trim();
        String gioiTinh = "";

        if (maDG == null || maDG.equals("")) {
            Toast.makeText(this, "Lỗi mã độc giả!", Toast.LENGTH_SHORT).show();
            return;
        }

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
            if (dgCheck.getMaDG() != null && dgCheck.getMaDG().equals(maDG)) {
                continue;
            }

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
        gioiTinh = arrGioiTinh[spnGioiTinh.getSelectedItemPosition()];

        SpinnerItem khoa = (SpinnerItem) spnKhoa.getSelectedItem();
        SpinnerItem lop = (SpinnerItem) spnLop.getSelectedItem();
        if (khoa == null || lop == null || khoa.getId().equals("") || lop.getId().equals("")) {
            Toast.makeText(this, "Vui lòng chọn khoa và lớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        DocGia dg = new DocGia();
        dg.setMaDG(maDG);
        dg.setMaKhoa(khoa.getId());
        dg.setMaLop(lop.getId());
        dg.setTenDG(tenDG);
        dg.setNamSinh(namSinh);
        dg.setGioiTinh(gioiTinh);
        dg.setDiaChi(diaChi);
        dg.setEmail(email);
        dg.setSdt(sdt);

        boolean result = docGiaQuery.suaDocGia(dg);

        if (result) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
