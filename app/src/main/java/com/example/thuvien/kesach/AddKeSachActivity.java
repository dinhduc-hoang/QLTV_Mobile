package com.example.thuvien.kesach;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

public class AddKeSachActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTenKe, edtMoTa;
    Button btnSave;

    KeSachQuery keSachQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kesach);

        imgBack = findViewById(R.id.imgBack);
        edtTenKe = findViewById(R.id.edtTenKe);
        edtMoTa = findViewById(R.id.edtMoTa);
        btnSave = findViewById(R.id.btnSave);

        keSachQuery = new KeSachQuery(this);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luuKeSach();
            }
        });
    }

    private void luuKeSach() {
        String tenKe = edtTenKe.getText().toString().trim();
        String moTa = edtMoTa.getText().toString().trim();

        if (tenKe.isEmpty()) {
            edtTenKe.setError("Nhập tên kệ");
            edtTenKe.requestFocus();
            return;
        }

        if (moTa.isEmpty()) {
            edtMoTa.setError("Nhập mô tả");
            edtMoTa.requestFocus();
            return;
        }

        KeSach item = new KeSach();
        item.setMaViTri(keSachQuery.taoMaMoi());
        item.setTenKe(tenKe);
        item.setMoTa(moTa);

        boolean result = keSachQuery.themKeSach(item);

        if (result) {
            Toast.makeText(this, "Thêm kệ sách thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm kệ sách thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}