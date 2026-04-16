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

public class AddKhoaActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTenKhoa;
    Button btnSave;

    KhoaQuery khoaQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_khoa);

        imgBack = findViewById(R.id.imgBack);
        edtTenKhoa = findViewById(R.id.edtTenKhoa);
        btnSave = findViewById(R.id.btnSave);

        khoaQuery = new KhoaQuery(this);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luuKhoa();
            }
        });
    }

    private void luuKhoa() {
        List<Khoa> list = khoaQuery.layDanhSachKhoa();
        String tenKhoa = edtTenKhoa.getText().toString().trim();

        if (tenKhoa.isEmpty()) {
            edtTenKhoa.setError("Nhập tên khoa");
            edtTenKhoa.requestFocus();
            return;
        }

        if (tenKhoa.length() < 2 || tenKhoa.length() > 100) {
            edtTenKhoa.setError("Tên khoa phải từ 2-100 ký tự");
            edtTenKhoa.requestFocus();
            return;
        }

        if (!tenKhoa.matches("[\\p{L}0-9\\s.&-]+")) {
            edtTenKhoa.setError("Tên khoa chứa ký tự không hợp lệ");
            edtTenKhoa.requestFocus();
            return;
        }

        for (Khoa k : list) {
            if (k.getTenKhoa() != null && k.getTenKhoa().equalsIgnoreCase(tenKhoa)) {
                edtTenKhoa.setError("Tên khoa đã tồn tại");
                edtTenKhoa.requestFocus();
                return;
            }
        }
        Khoa item = new Khoa();
        item.setMaKhoa(khoaQuery.taoMaMoi());
        item.setTenKhoa(tenKhoa);

        boolean result = khoaQuery.themKhoa(item);

        if (result) {
            Toast.makeText(this, "Thêm khoa thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm khoa thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
