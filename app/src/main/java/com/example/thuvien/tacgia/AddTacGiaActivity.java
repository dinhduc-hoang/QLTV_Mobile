package com.example.thuvien.tacgia;

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

import java.util.Calendar;

public class AddTacGiaActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTenTG, edtNamSinh, edtQuocTich;
    Spinner spnGioiTinh;
    Button btnSave;

    TacGiaQuery tacGiaQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tacgia);

        imgBack = findViewById(R.id.imgBack);
        edtTenTG = findViewById(R.id.edtTenTG);
        edtNamSinh = findViewById(R.id.edtNamSinh);
        edtQuocTich = findViewById(R.id.edtQuocTich);
        spnGioiTinh = findViewById(R.id.spnGioiTinh);
        btnSave = findViewById(R.id.btnSave);

        tacGiaQuery = new TacGiaQuery(this);
        loadGioiTinhSpinner();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luuTacGia();
            }
        });
    }

    private void loadGioiTinhSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                tacGiaQuery.layDanhSachGioiTinh()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGioiTinh.setAdapter(adapter);
    }

    private void luuTacGia() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String tenTG = edtTenTG.getText().toString().trim();
        String namSinh = edtNamSinh.getText().toString().trim();
        String quocTich = edtQuocTich.getText().toString().trim();
        String gioiTinh = spnGioiTinh.getSelectedItem().toString();

        if (tenTG.isEmpty()) {
            edtTenTG.setError("Nhập tên tác giả");
            edtTenTG.requestFocus();
            return;
        }

        if (namSinh.isEmpty()) {
            edtNamSinh.setError("Nhập năm sinh");
            edtNamSinh.requestFocus();
            return;
        }

        int namSinhInt;
        try {
            namSinhInt = Integer.parseInt(namSinh);
        } catch (NumberFormatException e) {
            edtNamSinh.setError("Năm sinh phải là số");
            edtNamSinh.requestFocus();
            return;
        }

        if (namSinhInt < 1000 || namSinhInt > year) {
            edtNamSinh.setError("Năm sinh không hợp lệ");
            edtNamSinh.requestFocus();
            return;
        }

        if (quocTich.isEmpty()) {
            edtQuocTich.setError("Nhập quốc tịch");
            edtQuocTich.requestFocus();
            return;
        }

        TacGia item = new TacGia();
        item.setMaTG(tacGiaQuery.taoMaMoi());
        item.setTenTG(tenTG);
        item.setNamSinh(namSinh);
        item.setGioiTinh(gioiTinh);
        item.setQuocTich(quocTich);

        boolean result = tacGiaQuery.themTacGia(item);

        if (result) {
            Toast.makeText(this, "Thêm tác giả thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm tác giả thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
