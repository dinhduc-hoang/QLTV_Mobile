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

public class AddTheThuVienActivity extends AppCompatActivity {

    ImageView imgBack;
    Spinner spnDocGia;
    EditText edtNgayCap, edtNgayHetHan, edtTrangThai;
    Button btnSave;

    TheThuVienQuery theThuVienQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thethuvien);

        imgBack = findViewById(R.id.imgBack);
        spnDocGia = findViewById(R.id.spnDocGia);
        edtNgayCap = findViewById(R.id.edtNgayCap);
        edtNgayHetHan = findViewById(R.id.edtNgayHetHan);
        edtTrangThai = findViewById(R.id.edtTrangThai);
        btnSave = findViewById(R.id.btnSave);

        theThuVienQuery = new TheThuVienQuery(this);

        loadDocGiaSpinner();
        setupDateField(edtNgayCap);
        setupDateField(edtNgayHetHan);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luu();
            }
        });
        setDefaultDates();
        edtTrangThai.setEnabled(false);
        edtNgayCap.setEnabled(false);

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
                AddTheThuVienActivity.this,
                (view, year, month, dayOfMonth) -> {
                    String ngay = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                            dayOfMonth, month + 1, year);
                    targetEditText.setText(ngay);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        if (targetEditText.getId() == R.id.edtNgayHetHan) {
            // Lấy ngày cấp thẻ từ EditText edtNgayCap
            EditText edtNgayCap = findViewById(R.id.edtNgayCap);

            if (edtNgayCap != null && !edtNgayCap.getText().toString().isEmpty()) {
                try {
                    String ngayCapStr = edtNgayCap.getText().toString();
                    String[] parts = ngayCapStr.split("/");

                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]) - 1;
                    int year = Integer.parseInt(parts[2]);

                    Calendar minDate = Calendar.getInstance();
                    minDate.set(year, month, day);

                    datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            }
        }
        datePickerDialog.show();
    }
    private void setDefaultDates() {
        Calendar calendar = Calendar.getInstance();


        String ngayCap = String.format(
                Locale.getDefault(),
                "%02d/%02d/%04d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR)
        );

        edtNgayCap.setText(ngayCap);

        calendar.add(Calendar.YEAR, 1);

        String ngayHetHan = String.format(
                Locale.getDefault(),
                "%02d/%02d/%04d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR)
        );

        edtNgayHetHan.setText(ngayHetHan);
    }
    private void luu() {

        SpinnerItem docGia = (SpinnerItem) spnDocGia.getSelectedItem();
        String ngayCap = edtNgayCap.getText().toString().trim();
        String ngayHetHan = edtNgayHetHan.getText().toString().trim();
        String trangThai = "Còn hiệu lực";
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

        List<TheThuVien> list = theThuVienQuery.layDanhSachThe();

        for (TheThuVien t : list) {
            if (t.getMaDG().equals(docGia.getId())) {
                Toast.makeText(this, "Độc giả này đã có thẻ thư viện", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        TheThuVien item = new TheThuVien();
        item.setMaThe(theThuVienQuery.taoMaMoi());
        item.setMaDG(docGia.getId());
        item.setNgayCap(ngayCap);
        item.setNgayHetHan(ngayHetHan);
        item.setTrangThai(trangThai);

        boolean result = theThuVienQuery.themThe(item);

        if (result) {
            Toast.makeText(this, "Thêm thành công: " + item.getMaThe(), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
