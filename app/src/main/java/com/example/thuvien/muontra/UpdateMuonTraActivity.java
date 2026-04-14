package com.example.thuvien.muontra;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateMuonTraActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView tvMaMT, tvDocGia, tvNhanVien, tvNgayMuon, tvHanTra, tvTrangThai;
    Button btnDaTra;
    ListView lvChiTiet;

    String maMT;
    ArrayAdapter<String> adapter;
    ArrayList<String> listChiTiet;
    MuonTraQuery muonTraQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_muontra);

        imgBack = findViewById(R.id.imgBack);
        tvMaMT = findViewById(R.id.tvMaMT);
        tvDocGia = findViewById(R.id.tvDocGia);
        tvNhanVien = findViewById(R.id.tvNhanVien);
        tvNgayMuon = findViewById(R.id.tvNgayMuon);
        tvHanTra = findViewById(R.id.tvHanTra);
        tvTrangThai = findViewById(R.id.tvTrangThai);
        btnDaTra = findViewById(R.id.btnDaTra);
        lvChiTiet = findViewById(R.id.lvChiTiet);

        muonTraQuery = new MuonTraQuery(this);

        listChiTiet = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listChiTiet);
        lvChiTiet.setAdapter(adapter);

        maMT = getIntent().getStringExtra("MaMT");
        loadThongTin();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDaTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = muonTraQuery.capNhatTrangThaiDaTra(maMT);
                if (result) {
                    Toast.makeText(UpdateMuonTraActivity.this, "Đã cập nhật trạng thái Đã trả", Toast.LENGTH_SHORT).show();
                    loadThongTin();
                } else {
                    Toast.makeText(UpdateMuonTraActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadThongTin() {
        listChiTiet.clear();

        MuonTra item = muonTraQuery.layThongTinMuonTraTheoMa(maMT);
        if (item != null) {
            tvMaMT.setText("Mã phiếu: " + item.getMaMT());
            tvDocGia.setText("Độc giả: " + item.getTenDG());
            tvNhanVien.setText("Nhân viên: " + item.getTenNV());
            tvNgayMuon.setText("Ngày mượn: " + item.getNgayMuon());
            tvHanTra.setText("Hạn trả: " + item.getHanTra());
            tvTrangThai.setText("Trạng thái: " + item.getTrangThai());
        }

        List<String> chiTiet = muonTraQuery.layChiTietSachTrongPhieu(maMT);
        listChiTiet.addAll(chiTiet);
        adapter.notifyDataSetChanged();
    }
}