package com.example.thuvien.nhanvien;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class NhanVienActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    ListView lvNhanVien;

    NhanVienAdapter adapter;
    List<NhanVien> list;
    NhanVienQuery nhanVienQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhanvien);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        lvNhanVien = findViewById(R.id.lvNhanVien);
        btnAdd = findViewById(R.id.btnAdd);

        nhanVienQuery = new NhanVienQuery(this);

        list = new ArrayList<>();

        adapter = new NhanVienAdapter(this, list);

        lvNhanVien.setAdapter(adapter);
        registerForContextMenu(lvNhanVien);
        loadDanhSach();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NhanVienActivity.this, AddNhanVienActivity.class));
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {
            }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                List<NhanVien> ketQua = nhanVienQuery.timKiemNhanVien(s.toString());
                adapter.capNhatDuLieu(ketQua);
            }

            @Override
            public void afterTextChanged(Editable e) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDanhSach();
    }

    private void loadDanhSach() {
        list = nhanVienQuery.layDanhSachNhanVien();
        adapter.capNhatDuLieu(list);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Sửa");
        menu.add(0, 2, 0, "Xóa");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        NhanVien nvSelect = (NhanVien) adapter.getItem(info.position);
        final String maNV = nvSelect.getMaNV();

        switch (item.getItemId()) {
            case 1: // Sửa
                Intent intent = new Intent(NhanVienActivity.this, UpdateNhanVienActivity.class);
                intent.putExtra("MaNV", maNV);
                startActivity(intent);
                return true;
            case 2: // Xóa
                if (nhanVienQuery.nhanVienDangDuocSuDung(maNV)) {
                    Toast.makeText(NhanVienActivity.this, "Không thể xóa nhân viên này vì đang có phiếu mượn liên quan!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                new AlertDialog.Builder(NhanVienActivity.this)
                        .setTitle("Xóa nhân viên")
                        .setMessage("Bạn có chắc muốn xóa nhân viên này không?")
                        .setPositiveButton("Xóa", (dialogInterface, i) -> {
                            boolean result = nhanVienQuery.xoaNhanVien(maNV);
                            if (result) {
                                Toast.makeText(NhanVienActivity.this, "Xóa nhân viên thành công!", Toast.LENGTH_SHORT).show();
                                loadDanhSach();
                            } else {
                                Toast.makeText(NhanVienActivity.this, "Xóa nhân viên thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
