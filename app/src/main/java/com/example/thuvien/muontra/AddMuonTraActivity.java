package com.example.thuvien.muontra;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.common.SpinnerItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddMuonTraActivity extends AppCompatActivity {

    ImageView imgBack;
    Spinner spnDocGia, spnNhanVien, spnSach;
    EditText edtNgayMuon, edtHanTra, edtSoLuong;
    Button btnThemSach, btnSaveMuonTra;
    ListView lvSachDaChon;

    ArrayList<String> listHienThi;
    ArrayList<String> listMaSach;
    ArrayList<Integer> listSoLuong;
    ArrayAdapter<String> adapter;

    MuonTraQuery muonTraQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_muontra);

        imgBack = findViewById(R.id.imgBack);
        spnDocGia = findViewById(R.id.spnDocGia);
        spnNhanVien = findViewById(R.id.spnNhanVien);
        spnSach = findViewById(R.id.spnSach);
        edtNgayMuon = findViewById(R.id.edtNgayMuon);
        edtHanTra = findViewById(R.id.edtHanTra);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        btnThemSach = findViewById(R.id.btnThemSach);
        btnSaveMuonTra = findViewById(R.id.btnSaveMuonTra);
        lvSachDaChon = findViewById(R.id.lvSachDaChon);

        muonTraQuery = new MuonTraQuery(this);

        listHienThi = new ArrayList<>();
        listMaSach = new ArrayList<>();
        listSoLuong = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listHienThi);
        lvSachDaChon.setAdapter(adapter);

        loadSpinnerData();
        setupDateField(edtNgayMuon);
        setupDateField(edtHanTra);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnThemSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themSachVaoPhieu();
            }
        });

        btnSaveMuonTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luuPhieuMuon();
            }
        });

        lvSachDaChon.setOnItemLongClickListener((parent, view, position, id) -> {
            listHienThi.remove(position);
            listMaSach.remove(position);
            listSoLuong.remove(position);
            adapter.notifyDataSetChanged();
            return true;
        });
    }

    private void loadSpinnerData() {
        setSpinner(spnDocGia, muonTraQuery.layDanhSachSpinner("docgia", "MaDG", "TenDG", "--- Chọn độc giả ---"));
        setSpinner(spnNhanVien, muonTraQuery.layDanhSachSpinner("nhanvien", "MaNV", "TenNV", "--- Chọn nhân viên ---"));
        setSpinner(spnSach, muonTraQuery.layDanhSachSpinner("sach", "MaSach", "TenSach", "--- Chọn sách ---"));
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

    private void setupDateField(EditText editText) {
        editText.setFocusable(false);
        editText.setClickable(true);
        editText.setCursorVisible(false);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(editText);
            }
        });
    }

    private void showDatePicker(final EditText targetEditText) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddMuonTraActivity.this,
                (view, year, month, dayOfMonth) -> {
                    String ngay = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                            dayOfMonth, month + 1, year);
                    targetEditText.setText(ngay);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void themSachVaoPhieu() {
        SpinnerItem sach = (SpinnerItem) spnSach.getSelectedItem();
        String strSoLuong = edtSoLuong.getText().toString().trim();

        if (sach.getId().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn sách", Toast.LENGTH_SHORT).show();
            return;
        }

        if (strSoLuong.isEmpty()) {
            edtSoLuong.setError("Nhập số lượng");
            edtSoLuong.requestFocus();
            return;
        }

        int soLuong;
        try {
            soLuong = Integer.parseInt(strSoLuong);
        } catch (Exception e) {
            edtSoLuong.setError("Số lượng phải là số");
            edtSoLuong.requestFocus();
            return;
        }

        if (soLuong <= 0) {
            edtSoLuong.setError("Số lượng phải lớn hơn 0");
            edtSoLuong.requestFocus();
            return;
        }

        listMaSach.add(sach.getId());
        listSoLuong.add(soLuong);
        listHienThi.add(sach.getName() + " - SL: " + soLuong);
        adapter.notifyDataSetChanged();
        edtSoLuong.setText("");
    }

    private void luuPhieuMuon() {
        SpinnerItem docGia = (SpinnerItem) spnDocGia.getSelectedItem();
        SpinnerItem nhanVien = (SpinnerItem) spnNhanVien.getSelectedItem();
        String ngayMuon = edtNgayMuon.getText().toString().trim();
        String hanTra = edtHanTra.getText().toString().trim();

        if (docGia.getId().isEmpty()) {
            Toast.makeText(this, "Chọn độc giả", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nhanVien.getId().isEmpty()) {
            Toast.makeText(this, "Chọn nhân viên", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ngayMuon.isEmpty()) {
            edtNgayMuon.setError("Chọn ngày mượn");
            return;
        }

        if (hanTra.isEmpty()) {
            edtHanTra.setError("Chọn hạn trả");
            return;
        }

        if (listMaSach.size() == 0) {
            Toast.makeText(this, "Phải chọn ít nhất 1 sách", Toast.LENGTH_SHORT).show();
            return;
        }

        String maMT = muonTraQuery.taoMaMuonTraMoi();
        boolean result = muonTraQuery.themPhieuMuonVaChiTiet(
                maMT,
                docGia.getId(),
                nhanVien.getId(),
                ngayMuon,
                hanTra,
                listMaSach,
                listSoLuong
        );

        if (result) {
            Toast.makeText(this, "Lưu phiếu mượn thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lưu phiếu mượn thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}