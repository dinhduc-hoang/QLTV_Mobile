package com.example.thuvien.khoa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

import java.util.List;

public class UpdateKhoaActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTenKhoa;
    Button btnUpdate;

    String maKhoa;
    KhoaQuery khoaQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_khoa);

        imgBack = findViewById(R.id.imgBack);
        edtTenKhoa = findViewById(R.id.edtTenKhoa);
        btnUpdate = findViewById(R.id.btnUpdate);

        khoaQuery = new KhoaQuery(this);
        maKhoa = getIntent().getStringExtra("MaKhoa");

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
                capNhatKhoa();
            }
        });
    }

    private void loadThongTin() {
        Khoa item = khoaQuery.layThongTinTheoMa(maKhoa);
        if (item != null) {
            edtTenKhoa.setText(item.getTenKhoa());
        }
    }

    private void capNhatKhoa() {
        List<Khoa> list = khoaQuery.layDanhSachKhoa();
        String tenKhoa = edtTenKhoa.getText().toString().trim();

        if (tenKhoa.isEmpty()) {
            edtTenKhoa.setError("Nhập tên khoa");
            edtTenKhoa.requestFocus();
            return;
        }
        for (Khoa k : list) {
            if (k.getTenKhoa().equalsIgnoreCase(tenKhoa)) {
                edtTenKhoa.setError("Tên khoa đã tồn tại");
                edtTenKhoa.requestFocus();
                return;
            }
        }
        Khoa item = new Khoa();
        item.setMaKhoa(maKhoa);
        item.setTenKhoa(tenKhoa);

        boolean result = khoaQuery.capNhatKhoa(item);

        if (result) {
            Toast.makeText(this, "Cập nhật khoa thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật khoa thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
