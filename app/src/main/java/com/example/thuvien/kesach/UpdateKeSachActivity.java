package com.example.thuvien.kesach;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

import java.util.List;

public class UpdateKeSachActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTenKe, edtMoTa;
    Button btnUpdate;

    String maViTri;
    KeSachQuery keSachQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_kesach);

        imgBack = findViewById(R.id.imgBack);
        edtTenKe = findViewById(R.id.edtTenKe);
        edtMoTa = findViewById(R.id.edtMoTa);
        btnUpdate = findViewById(R.id.btnUpdate);

        keSachQuery = new KeSachQuery(this);
        maViTri = getIntent().getStringExtra("MaViTri");

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
                capNhatKeSach();
            }
        });
    }

    private void loadThongTin() {
        KeSach item = keSachQuery.layThongTinTheoMa(maViTri);
        if (item != null) {
            edtTenKe.setText(item.getTenKe());
            edtMoTa.setText(item.getMoTa());
        }
    }

    private void capNhatKeSach() {
        List<KeSach> list = keSachQuery.layDanhSachKeSach();
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

        for (KeSach ks : list) {
            if (ks.getTenKe().equalsIgnoreCase(tenKe) && !ks.getMaViTri().equals(maViTri)) {
                edtTenKe.setError("Tên kệ đã tồn tại");
                edtTenKe.requestFocus();
                return;
            }
        }
        KeSach item = new KeSach();
        item.setMaViTri(maViTri);
        item.setTenKe(tenKe);
        item.setMoTa(moTa);

        boolean result = keSachQuery.capNhatKeSach(item);

        if (result) {
            Toast.makeText(this, "Cập nhật kệ sách thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật kệ sách thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
