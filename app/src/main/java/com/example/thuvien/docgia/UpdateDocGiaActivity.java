package com.example.thuvien.docgia;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class UpdateDocGiaActivity extends AppCompatActivity {

    private ImageView imgBack;
    private EditText edtTenDG, edtNamSinh, edtDiaChi, edtEmail, edtSdt;
    private Spinner spnGioiTinh, spnKhoa, spnLop;
    private Button btnUpdateDocGia;

    private String maDG;
    private DocGiaQuery docGiaQuery;

    private boolean isLoadingData = false;
    private String pendingMaLop = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_docgia);

        initViews();

        docGiaQuery = new DocGiaQuery(this);
        maDG = getIntent().getStringExtra("MaDG");

        loadKhoaSpinner();
        loadGioiTinhSpinner();
        setupKhoaLopDependency();
        loadThongTin();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdateDocGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capNhatDocGia();
            }
        });
    }

    private void initViews() {
        imgBack = findViewById(R.id.imgBack);
        edtTenDG = findViewById(R.id.edtTenDG);
        edtNamSinh = findViewById(R.id.edtNamSinh);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtEmail = findViewById(R.id.edtEmail);
        edtSdt = findViewById(R.id.edtSdt);
        spnGioiTinh = findViewById(R.id.spnGioiTinh);
        spnKhoa = findViewById(R.id.spnKhoa);
        spnLop = findViewById(R.id.spnLop);
        btnUpdateDocGia = findViewById(R.id.btnUpdateDocGia);
    }

    private void loadKhoaSpinner() {
        setSpinner(spnKhoa, docGiaQuery.layDanhSachKhoaSpinner());
        setSpinner(spnLop, docGiaQuery.layDanhSachLopTheoKhoa(""));
    }

    private void loadGioiTinhSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                docGiaQuery.layDanhSachGioiTinh()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGioiTinh.setAdapter(adapter);
    }

    private void setupKhoaLopDependency() {
        spnKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem khoa = (SpinnerItem) spnKhoa.getSelectedItem();

                if (khoa != null && !khoa.getId().isEmpty()) {
                    setSpinner(spnLop, docGiaQuery.layDanhSachLopTheoKhoa(khoa.getId()));
                } else {
                    setSpinner(spnLop, docGiaQuery.layDanhSachLopTheoKhoa(""));
                }

                // Nếu đang load dữ liệu cũ, chọn lại lớp cũ ngay sau khi nạp danh sách lớp
                if (pendingMaLop != null) {
                    setSpinnerSelected(spnLop, pendingMaLop);
                    pendingMaLop = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setSpinner(Spinner spinner, List<SpinnerItem> list) {
        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                list
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadThongTin() {
        if (maDG == null || maDG.trim().isEmpty()) {
            Toast.makeText(this, "Không tìm thấy mã độc giả", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DocGia item = docGiaQuery.layThongTinTheoMa(maDG);

        if (item == null) {
            Toast.makeText(this, "Không tải được thông tin độc giả", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        isLoadingData = true;

        edtTenDG.setText(item.getTenDG());
        edtNamSinh.setText(item.getNamSinh());
        edtDiaChi.setText(item.getDiaChi());
        edtEmail.setText(item.getEmail());
        edtSdt.setText(item.getSdt());
        setGioiTinhSelected(item.getGioiTinh());

        // Lưu mã lớp cần chọn trước
        pendingMaLop = item.getMaLop();

        // Chọn khoa, listener của spnKhoa sẽ tự load lớp và chọn pendingMaLop
        setSpinnerSelected(spnKhoa, item.getMaKhoa());

        isLoadingData = false;
    }

    private void setSpinnerSelected(Spinner spinner, String selectedId) {
        if (selectedId == null) return;

        String normalizedSelectedId = selectedId.trim();

        for (int i = 0; i < spinner.getCount(); i++) {
            Object obj = spinner.getItemAtPosition(i);
            if (obj instanceof SpinnerItem) {
                SpinnerItem item = (SpinnerItem) obj;
                if (item.getId() != null && item.getId().trim().equalsIgnoreCase(normalizedSelectedId)) {
                    spinner.setSelection(i);
                    return;
                }
            }
        }
    }

    private void setGioiTinhSelected(String gioiTinh) {
        if (gioiTinh == null) return;

        String normalized = gioiTinh.trim();

        for (int i = 0; i < spnGioiTinh.getCount(); i++) {
            String item = spnGioiTinh.getItemAtPosition(i).toString();
            if (item.trim().equalsIgnoreCase(normalized)) {
                spnGioiTinh.setSelection(i);
                return;
            }
        }
    }

    private void capNhatDocGia() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        List<DocGia> list = docGiaQuery.layDanhSachDocGia();
        String tenDG = edtTenDG.getText().toString().trim();
        String namSinh = edtNamSinh.getText().toString().trim();
        int namSinhInt = Integer.parseInt(namSinh);
        String diaChi = edtDiaChi.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String sdt = edtSdt.getText().toString().trim();
        String gioiTinh = spnGioiTinh.getSelectedItem() != null
                ? spnGioiTinh.getSelectedItem().toString()
                : "";

        if (tenDG.isEmpty()) {
            edtTenDG.setError("Nhập tên độc giả");
            edtTenDG.requestFocus();
            return;
        }

        if (namSinh.isEmpty()) {
            edtNamSinh.setError("Nhập năm sinh");
            edtNamSinh.requestFocus();
            return;
        }
        if (namSinhInt < 1900 || namSinhInt > year) {
            edtNamSinh.setError("Năm sinh không hợp lệ");
            edtNamSinh.requestFocus();
            return;
        }
        if (diaChi.isEmpty()) {
            edtDiaChi.setError("Nhập địa chỉ");
            edtDiaChi.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtEmail.setError("Nhập email");
            edtEmail.requestFocus();
            return;
        }

        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            edtEmail.setError("Email không đúng định dạng");
            edtEmail.requestFocus();
            return;
        }

        if (sdt.isEmpty()) {
            edtSdt.setError("Nhập số điện thoại");
            edtSdt.requestFocus();
            return;
        }

        if (!sdt.matches("0\\d{9}")) {
            edtSdt.setError("SĐT phải 10 số, bắt đầu bằng 0");
            edtSdt.requestFocus();
            return;
        }
        for (DocGia dg : list) {

            if (dg.getSdt().equals(sdt)) {
                edtSdt.setError("SĐT đã tồn tại");
                edtSdt.requestFocus();
                return;
            }

            if (dg.getEmail().equalsIgnoreCase(email)) {
                edtEmail.setError("Email đã tồn tại");
                edtEmail.requestFocus();
                return;
            }
        }
        SpinnerItem khoa = spnKhoa.getSelectedItem() instanceof SpinnerItem
                ? (SpinnerItem) spnKhoa.getSelectedItem()
                : null;
        SpinnerItem lop = spnLop.getSelectedItem() instanceof SpinnerItem
                ? (SpinnerItem) spnLop.getSelectedItem()
                : null;

        if (khoa == null || khoa.getId().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn khoa", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lop == null || lop.getId().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn lớp", Toast.LENGTH_SHORT).show();
            return;
        }

        DocGia item = new DocGia();
        item.setMaDG(maDG);
        item.setMaKhoa(khoa.getId());
        item.setMaLop(lop.getId());
        item.setTenDG(tenDG);
        item.setNamSinh(namSinh);
        item.setGioiTinh(gioiTinh);
        item.setDiaChi(diaChi);
        item.setEmail(email);
        item.setSdt(sdt);

        boolean result = docGiaQuery.capNhatDocGia(item);

        if (result) {
            Toast.makeText(this, "Cập nhật độc giả thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật độc giả thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}