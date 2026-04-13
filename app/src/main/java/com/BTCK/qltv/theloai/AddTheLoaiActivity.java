package com.BTCK.qltv.theloai;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;

public class AddTheLoaiActivity extends AppCompatActivity {

    EditText edtMaTL, edtTenTL;
    Button btnSaveTheLoai;
    TheLoaiQuery theLoaiQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_the_loai);

        edtMaTL = findViewById(R.id.edtMaTL);
        edtTenTL = findViewById(R.id.edtTenTL);
        btnSaveTheLoai = findViewById(R.id.btnSaveTheLoai);

        theLoaiQuery = new TheLoaiQuery(this);

        btnSaveTheLoai.setOnClickListener(v -> {
            String ma = edtMaTL.getText().toString().trim();
            String ten = edtTenTL.getText().toString().trim();

            if (ma.isEmpty() || ten.isEmpty()) {
                Toast.makeText(this, "Mã và Tên không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            TheLoai theLoai = new TheLoai(ma, ten);
            boolean inserted = theLoaiQuery.themTheLoai(theLoai);

            if (inserted) {
                Toast.makeText(this, "Đã thêm Thể Loại", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm thất bại! Mã đã tồn tại.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}