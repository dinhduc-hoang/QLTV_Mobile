package com.example.thuvien.thethuvien;

import android.app.DatePickerDialog;
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

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UpdateTheThuVienActivity extends AppCompatActivity {

    ImageView imgBack;
    Spinner spnDocGia;
    EditText edtNgayCap, edtNgayHetHan, edtTrangThai;
    Button btnUpdate;

    String id;
    TheThuVienQuery theThuVienQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_thethuvien);

        imgBack = findViewById(R.id.imgBack);
        spnDocGia = findViewById(R.id.spnDocGia);
        edtNgayCap = findViewById(R.id.edtNgayCap);
        edtNgayHetHan = findViewById(R.id.edtNgayHetHan);
        edtTrangThai = findViewById(R.id.edtTrangThai);
        btnUpdate = findViewById(R.id.btnUpdate);

        theThuVienQuery = new TheThuVienQuery(this);
        id = getIntent().getStringExtra("MaThe");

        loadDocGiaSpinner();
        setupDateField(edtNgayCap);
        setupDateField(edtNgayHetHan);
        loadThongTin();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capNhat();
            }
        });
    }

    private void loadDocGiaSpinner() {
        List<SpinnerItem> list = theThuVienQuery.layDanhSachDocGiaSpinner();

        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                list
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDocGia.setAdapter(adapter);
    }

    private void setupDateField(EditText editText) {
        editText.setFocusable(false);
        editText.setClickable(true);
        editText.setCursorVisible(false);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(editText);
            }
        });
    }

    private void showDatePicker(final EditText targetEditText) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                UpdateTheThuVienActivity.this,
                (view, year, month, dayOfMonth) -> {
                    String ngay = String.format(
                            Locale.getDefault(),
                            "%02d/%02d/%04d",
                            dayOfMonth,
                            month + 1,
                            year
                    );
                    targetEditText.setText(ngay);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void loadThongTin() {
        TheThuVien item = theThuVienQuery.layThongTinTheoMa(id);

        if (item != null) {
            setSpinnerSelected(spnDocGia, item.getMaDG());
            edtNgayCap.setText(item.getNgayCap());
            edtNgayHetHan.setText(item.getNgayHetHan());
            edtTrangThai.setText(item.getTrangThai());
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

    private void capNhat() {
        SpinnerItem docGia = (SpinnerItem) spnDocGia.getSelectedItem();
        String ngayCap = edtNgayCap.getText().toString().trim();
        String ngayHetHan = edtNgayHetHan.getText().toString().trim();
        String trangThai = edtTrangThai.getText().toString().trim();

        if (docGia.getId().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn độc giả", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ngayCap.isEmpty()) {
            edtNgayCap.setError("Không được để trống");
            return;
        }

        if (ngayHetHan.isEmpty()) {
            edtNgayHetHan.setError("Không được để trống");
            return;
        }

        if (trangThai.isEmpty()) {
            edtTrangThai.setError("Không được để trống");
            edtTrangThai.requestFocus();
            return;
        }

        TheThuVien item = new TheThuVien();
        item.setMaThe(id);
        item.setMaDG(docGia.getId());
        item.setNgayCap(ngayCap);
        item.setNgayHetHan(ngayHetHan);
        item.setTrangThai(trangThai);

        boolean result = theThuVienQuery.capNhatThe(item);

        if (result) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}