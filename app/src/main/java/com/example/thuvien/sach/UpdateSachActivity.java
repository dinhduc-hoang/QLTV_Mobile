package com.example.thuvien.sach;

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

import java.util.List;

public class UpdateSachActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTenSach, edtSoLuong, edtNamXB;
    Spinner spnMaTL, spnMaTG, spnMaNXB, spnMaNN, spnMaViTri;
    Button btnUpdateSach;

    String maSach;
    SachQuery sachQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sach);

        imgBack = findViewById(R.id.imgBack);
        edtTenSach = findViewById(R.id.edtTenSach);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        edtNamXB = findViewById(R.id.edtNamXB);
        spnMaTL = findViewById(R.id.spnMaTL);
        spnMaTG = findViewById(R.id.spnMaTG);
        spnMaNXB = findViewById(R.id.spnMaNXB);
        spnMaNN = findViewById(R.id.spnMaNN);
        spnMaViTri = findViewById(R.id.spnMaViTri);
        btnUpdateSach = findViewById(R.id.btnUpdateSach);

        maSach = getIntent().getStringExtra("MaSach");
        sachQuery = new SachQuery(this);

        loadSpinnerData();
        loadThongTinSach();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdateSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capNhatSach();
            }
        });
    }

    private void loadSpinnerData() {
        setSpinner(spnMaTL, sachQuery.layDanhSachSpinner("theloai", "MaTL", "TenTL", "--- Chọn thể loại ---"));
        setSpinner(spnMaTG, sachQuery.layDanhSachSpinner("tacgia", "MaTG", "TenTG", "--- Chọn tác giả ---"));
        setSpinner(spnMaNXB, sachQuery.layDanhSachSpinner("nhaxuatban", "MaNXB", "TenNXB", "--- Chọn nhà xuất bản ---"));
        setSpinner(spnMaNN, sachQuery.layDanhSachSpinner("ngonngu", "MaNN", "TenNN", "--- Chọn ngôn ngữ ---"));
        setSpinner(spnMaViTri, sachQuery.layDanhSachSpinner("kesach", "MaViTri", "TenKe", "--- Chọn vị trí kệ ---"));
    }

    private void setSpinner(Spinner spinner, List<SpinnerItem> list) {
        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<SpinnerItem>(
                this,
                android.R.layout.simple_spinner_item,
                list
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadThongTinSach() {
        Sach sach = sachQuery.layThongTinSachTheoMa(maSach);

        if (sach != null) {
            setSpinnerSelected(spnMaTG, sach.getMaTG());
            setSpinnerSelected(spnMaNXB, sach.getMaNXB());
            setSpinnerSelected(spnMaTL, sach.getMaTL());
            edtTenSach.setText(sach.getTenSach());
            setSpinnerSelected(spnMaNN, sach.getMaNN());
            setSpinnerSelected(spnMaViTri, sach.getMaViTri());
            edtNamXB.setText(String.valueOf(sach.getNamXB()));
            edtSoLuong.setText(String.valueOf(sach.getSoLuong()));
        }
    }

    private void capNhatSach() {
        String ten = edtTenSach.getText().toString().trim();
        String strSoLuong = edtSoLuong.getText().toString().trim();
        String strNamXB = edtNamXB.getText().toString().trim();

        if (ten.isEmpty()) {
            edtTenSach.setError("Nhập tên sách");
            edtTenSach.requestFocus();
            return;
        }

        if (strSoLuong.isEmpty()) {
            edtSoLuong.setError("Nhập số lượng");
            edtSoLuong.requestFocus();
            return;
        }

        if (strNamXB.isEmpty()) {
            edtNamXB.setError("Nhập năm xuất bản");
            edtNamXB.requestFocus();
            return;
        }

        SpinnerItem theLoai = (SpinnerItem) spnMaTL.getSelectedItem();
        SpinnerItem tacGia = (SpinnerItem) spnMaTG.getSelectedItem();
        SpinnerItem nxb = (SpinnerItem) spnMaNXB.getSelectedItem();
        SpinnerItem ngonNgu = (SpinnerItem) spnMaNN.getSelectedItem();
        SpinnerItem viTri = (SpinnerItem) spnMaViTri.getSelectedItem();

        if (theLoai.getId().isEmpty() || tacGia.getId().isEmpty() || nxb.getId().isEmpty()
                || ngonNgu.getId().isEmpty() || viTri.getId().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int soLuong;
        int namXB;

        try {
            soLuong = Integer.parseInt(strSoLuong);
            namXB = Integer.parseInt(strNamXB);
        } catch (Exception e) {
            Toast.makeText(this, "Số lượng và năm xuất bản phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        if (soLuong < 0) {
            edtSoLuong.setError("Số lượng không hợp lệ");
            edtSoLuong.requestFocus();
            return;
        }

        Sach sach = new Sach();
        sach.setMaSach(maSach);
        sach.setMaTG(tacGia.getId());
        sach.setMaNXB(nxb.getId());
        sach.setMaTL(theLoai.getId());
        sach.setTenSach(ten);
        sach.setMaNN(ngonNgu.getId());
        sach.setMaViTri(viTri.getId());
        sach.setNamXB(namXB);
        sach.setSoLuong(soLuong);

        boolean result = sachQuery.suaSach(sach);

        if (result) {
            Toast.makeText(this, "Cập nhật sách thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật sách thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setSpinnerSelected(Spinner spinner, String selectedId) {
        for (int i = 0; i < spinner.getCount(); i++) {
            SpinnerItem item = (SpinnerItem) spinner.getItemAtPosition(i);
            if (item.getId().equals(selectedId)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}