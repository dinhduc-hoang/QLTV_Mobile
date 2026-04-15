package com.example.thuvien.lop;

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

public class AddLopActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTenLop;
    Spinner spnKhoa;
    Button btnSave;

    LopQuery lopQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lop);

        imgBack = findViewById(R.id.imgBack);
        edtTenLop = findViewById(R.id.edtTenLop);
        spnKhoa = findViewById(R.id.spnKhoa);
        btnSave = findViewById(R.id.btnSave);

        lopQuery = new LopQuery(this);
        loadKhoaSpinner();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luuLop();
            }
        });
    }

    private void loadKhoaSpinner() {
        List<SpinnerItem> list = lopQuery.layDanhSachKhoaSpinner();
        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                list
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnKhoa.setAdapter(adapter);
    }

    private void luuLop() {
        List<Lop> list = lopQuery.layDanhSachLop();
        String tenLop = edtTenLop.getText().toString().trim();
        SpinnerItem khoa = (SpinnerItem) spnKhoa.getSelectedItem();

        if (tenLop.isEmpty()) {
            edtTenLop.setError("Nhập tên lớp");
            edtTenLop.requestFocus();
            return;
        }

        if (khoa.getId().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn khoa", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Lop l : list) {
            if (l.getTenLop().equalsIgnoreCase(tenLop)) {
                edtTenLop.setError("Tên lớp đã tồn tại");
                edtTenLop.requestFocus();
                return;
            }
        }
        Lop item = new Lop();
        item.setMaLop(lopQuery.taoMaMoi());
        item.setTenLop(tenLop);
        item.setMaKhoa(khoa.getId());

        boolean result = lopQuery.themLop(item);

        if (result) {
            Toast.makeText(this, "Thêm lớp thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm lớp thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
