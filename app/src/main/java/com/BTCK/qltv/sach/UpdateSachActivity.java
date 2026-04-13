package com.BTCK.qltv.sach;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class UpdateSachActivity extends AppCompatActivity {

    EditText edtTenSach, edtSoLuong, edtNamXB, edtMaTL, edtMaTG, edtMaNXB, edtMaNN, edtMaViTri;
    Button btnSaveSach;
    SachQuery sachQuery;
    String maSach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sach);

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

        maSach = getIntent().getStringExtra("maSach");
        edtTenSach.setText(getIntent().getStringExtra("tenSach"));
        edtSoLuong.setText(String.valueOf(getIntent().getIntExtra("soLuong", 0)));
        edtNamXB.setText(String.valueOf(getIntent().getIntExtra("namXB", 0)));
        edtMaTL.setText(getIntent().getStringExtra("maTL"));
        edtMaTG.setText(getIntent().getStringExtra("maTG"));
        edtMaNXB.setText(getIntent().getStringExtra("maNXB"));
        edtMaNN.setText(getIntent().getStringExtra("maNN"));
        edtMaViTri.setText(getIntent().getStringExtra("maViTri"));

        btnSaveSach.setOnClickListener(v -> {
            String ten = edtTenSach.getText().toString().trim();
            String maTL = edtMaTL.getText().toString().trim();
            String maTG = edtMaTG.getText().toString().trim();
            String maNXB = edtMaNXB.getText().toString().trim();
            String maNN = edtMaNN.getText().toString().trim();
            String maViTri = edtMaViTri.getText().toString().trim();
            String strSoLuong = edtSoLuong.getText().toString().trim();
            String strNamXB = edtNamXB.getText().toString().trim();

            if (maSach == null || maSach.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy mã sách!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ten.isEmpty() || maTL.isEmpty() || maTG.isEmpty() || maNXB.isEmpty() || maNN.isEmpty() || maViTri.isEmpty() || strSoLuong.isEmpty() || strNamXB.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int soLuongMoi;
            int namXBMoi;
            try {
                soLuongMoi = Integer.parseInt(strSoLuong);
                namXBMoi = Integer.parseInt(strNamXB);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số lượng và năm xuất bản phải là số!", Toast.LENGTH_SHORT).show();
                return;
            }

            Sach sachMoi = new Sach(maSach, maTG, maNXB, maTL, ten, maNN, maViTri, namXBMoi, soLuongMoi);
            boolean updated = sachQuery.suaSach(sachMoi);

            if (updated) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}