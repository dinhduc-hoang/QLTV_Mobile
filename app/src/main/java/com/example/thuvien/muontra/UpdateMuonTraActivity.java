package com.example.thuvien.muontra;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.common.SpinnerItem;
import com.example.thuvien.sach.Sach;
import com.example.thuvien.sach.SachQuery;

import java.util.ArrayList;
import java.util.List;

public class UpdateMuonTraActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView tvMaMT;
    EditText edtNgayMuon, edtHanTra, edtSoLuong;
    Spinner spnMaDG, spnMaNV, spnTrangThai, spnSach;
    Button btnUpdateMuonTra, btnThemSach;
    ListView lvChiTiet;

    String maMT;
    ArrayList<String> listHienThi;
    ArrayList<String> listMaSach;
    ArrayList<Integer> listSoLuong;
    ArrayAdapter<String> adapter;

    MuonTraQuery muonTraQuery;
    SachQuery sachQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_muontra);

        imgBack = findViewById(R.id.imgBack);
        tvMaMT = findViewById(R.id.tvMaMT);
        edtNgayMuon = findViewById(R.id.edtNgayMuon);
        edtHanTra = findViewById(R.id.edtHanTra);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        spnMaDG = findViewById(R.id.spnMaDG);
        spnMaNV = findViewById(R.id.spnMaNV);
        spnTrangThai = findViewById(R.id.spnTrangThai);
        spnSach = findViewById(R.id.spnSach);
        btnUpdateMuonTra = findViewById(R.id.btnUpdateMuonTra);
        btnThemSach = findViewById(R.id.btnThemSach);
        lvChiTiet = findViewById(R.id.lvChiTiet);

        muonTraQuery = new MuonTraQuery(this);
        sachQuery = new SachQuery(this);

        listHienThi = new ArrayList<>();
        listMaSach = new ArrayList<>();
        listSoLuong = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listHienThi);
        lvChiTiet.setAdapter(adapter);

        maMT = getIntent().getStringExtra("MaMT");

        loadSpinnerData();
        loadThongTinPhieu();

        imgBack.setOnClickListener(v -> finish());

        btnThemSach.setOnClickListener(v -> themSachVaoDanhSach());

        btnUpdateMuonTra.setOnClickListener(v -> capNhatToanBoPhieu());

        lvChiTiet.setOnItemLongClickListener((parent, view, position, id) -> {
            listHienThi.remove(position);
            listMaSach.remove(position);
            listSoLuong.remove(position);
            adapter.notifyDataSetChanged();
            return true;
        });
    }

    private void loadSpinnerData() {
        setSpinner(spnMaDG, muonTraQuery.layDanhSachSpinner("docgia", "MaDG", "TenDG", "--- Chọn độc giả ---"));
        setSpinner(spnMaNV, muonTraQuery.layDanhSachSpinner("nhanvien", "MaNV", "TenNV", "--- Chọn nhân viên ---"));
        setSpinner(spnSach, muonTraQuery.layDanhSachSpinner("sach", "MaSach", "TenSach", "--- Chọn sách ---"));

        List<SpinnerItem> listTrangThai = new ArrayList<>();
        listTrangThai.add(new SpinnerItem("Chưa trả", "Chưa trả"));
        listTrangThai.add(new SpinnerItem("Đã trả", "Đã trả"));
        setSpinner(spnTrangThai, listTrangThai);
    }

    private void setSpinner(Spinner spinner, List<SpinnerItem> list) {
        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadThongTinPhieu() {
        MuonTra item = muonTraQuery.layThongTinMuonTraTheoMa(maMT);
        if (item != null) {
            tvMaMT.setText("Mã phiếu: " + item.getMaMT());
            edtNgayMuon.setText(item.getNgayMuon());
            edtHanTra.setText(item.getHanTra());

            setSpinnerSelected(spnMaDG, item.getMaDG());
            setSpinnerSelected(spnMaNV, item.getMaNV());
            setSpinnerSelected(spnTrangThai, item.getTrangThai());
        }

        // Load chi tiết sách đang có trong phiếu
        List<ChiTietMuonTra> details = muonTraQuery.layDanhSachChiTiet(maMT);
        listHienThi.clear();
        listMaSach.clear();
        listSoLuong.clear();
        for (ChiTietMuonTra ct : details) {
            listMaSach.add(ct.getMaSach());
            listSoLuong.add(ct.getSoLuong());
            listHienThi.add(ct.getTenSach() + " - SL: " + ct.getSoLuong());
        }
        adapter.notifyDataSetChanged();
    }

    private void themSachVaoDanhSach() {
        SpinnerItem sach = (SpinnerItem) spnSach.getSelectedItem();
        String strSoLuong = edtSoLuong.getText().toString().trim();

        if (sach.getId().isEmpty()) {
            Toast.makeText(this, "Chọn sách", Toast.LENGTH_SHORT).show();
            return;
        }
        if (strSoLuong.isEmpty()) {
            Toast.makeText(this, "Nhập số lượng", Toast.LENGTH_SHORT).show();
            return;
        }

        int sl = Integer.parseInt(strSoLuong);
        Sach s = sachQuery.layThongTinSachTheoMa(sach.getId());
        if (s != null && s.getSoLuong() < sl) {
            Toast.makeText(this, "Kho không đủ sách", Toast.LENGTH_SHORT).show();
            return;
        }

        listMaSach.add(sach.getId());
        listSoLuong.add(sl);
        listHienThi.add(sach.getName() + " - SL: " + sl);
        adapter.notifyDataSetChanged();
        edtSoLuong.setText("");
    }

    private void capNhatToanBoPhieu() {
        SpinnerItem dg = (SpinnerItem) spnMaDG.getSelectedItem();
        SpinnerItem nv = (SpinnerItem) spnMaNV.getSelectedItem();
        SpinnerItem tt = (SpinnerItem) spnTrangThai.getSelectedItem();
        String ngay = edtNgayMuon.getText().toString();
        String han = edtHanTra.getText().toString();

        if (dg.getId().isEmpty() || nv.getId().isEmpty() || listMaSach.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean ok = muonTraQuery.capNhatMuonTraToanBo(maMT, dg.getId(), nv.getId(), ngay, han, tt.getId(), listMaSach, listSoLuong);
        if (ok) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi cập nhật!", Toast.LENGTH_SHORT).show();
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
}