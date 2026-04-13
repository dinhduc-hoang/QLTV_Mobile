package com.BTCK.qltv.sach;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class AddSachActivity extends AppCompatActivity {

    EditText edtTenSach, edtSoLuong, edtNamXB, edtMaTL, edtMaTG, edtMaNXB, edtMaNN, edtMaViTri;
    Button btnSaveSach;

    SachQuery sachQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sach);

        edtTenSach = findViewById(R.id.edtTenSach);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        edtNamXB = findViewById(R.id.edtNamXB);
        edtMaTL = findViewById(R.id.edtMaTL);
        edtMaTG = findViewById(R.id.edtMaTG);
        edtMaNXB = findViewById(R.id.edtMaNXB);
        edtMaNN = findViewById(R.id.edtMaNN);
        edtMaViTri = findViewById(R.id.edtMaViTri);
        btnSaveSach = findViewById(R.id.btnSaveSach);

        sachQuery = new SachQuery(this);

        btnSaveSach.setOnClickListener(v -> {
            String ten = edtTenSach.getText().toString().trim();
            String maTL = edtMaTL.getText().toString().trim();
            String maTG = edtMaTG.getText().toString().trim();
            String maNXB = edtMaNXB.getText().toString().trim();
            String maNN = edtMaNN.getText().toString().trim();
            String maViTri = edtMaViTri.getText().toString().trim();
            String strSoLuong = edtSoLuong.getText().toString().trim();
            String strNamXB = edtNamXB.getText().toString().trim();

            if (ten.isEmpty() || maTL.isEmpty() || maTG.isEmpty() || maNXB.isEmpty() || maNN.isEmpty() || maViTri.isEmpty() || strSoLuong.isEmpty() || strNamXB.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int soLuong;
            int namXB;
            try {
                soLuong = Integer.parseInt(strSoLuong);
                namXB = Integer.parseInt(strNamXB);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số lượng và năm xuất bản phải là số!", Toast.LENGTH_SHORT).show();
                return;
            }

            String maSach = sachQuery.taoMaSachMoi();
            Sach sach = new Sach(maSach, maTG, maNXB, maTL, ten, maNN, maViTri, namXB, soLuong);

            boolean inserted = sachQuery.themSach(sach);
            if (inserted) {
                Toast.makeText(this, "Thêm sách thành công! Mã: " + maSach, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm sách thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}