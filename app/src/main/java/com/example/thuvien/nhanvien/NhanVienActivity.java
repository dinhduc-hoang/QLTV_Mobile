package com.example.thuvien.nhanvien;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class NhanVienActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    RecyclerView rvNhanVien;

    NhanVienAdapter adapter;
    List<NhanVien> list;
    NhanVienQuery nhanVienQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhanvien);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvNhanVien = findViewById(R.id.rvNhanVien);
        btnAdd = findViewById(R.id.btnAdd);

        nhanVienQuery = new NhanVienQuery(this);

        rvNhanVien.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new NhanVienAdapter(list, new NhanVienAdapter.OnNhanVienListener() {
            @Override
            public void onLongClick(String maNV, View view) {
                showPopupMenu(view, maNV);
            }
        });

        rvNhanVien.setAdapter(adapter);
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
                adapter.getFilter().filter(s);
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

    private void showPopupMenu(View view, final String maNV) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Sửa");
        popupMenu.getMenu().add("Xóa");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Sửa")) {
                Intent intent = new Intent(NhanVienActivity.this, UpdateNhanVienActivity.class);
                intent.putExtra("MaNV", maNV);
                startActivity(intent);
                return true;
            }

            if (title.equals("Xóa")) {
                if (nhanVienQuery.nhanVienDangDuocSuDung(maNV)) {
                    Toast.makeText(NhanVienActivity.this, "Không thể xóa nhân viên này vì đang có phiếu mượn liên quan!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                new AlertDialog.Builder(NhanVienActivity.this)
                        .setTitle("Xóa nhân viên")
                        .setMessage("Bạn có chắc muốn xóa nhân viên này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = nhanVienQuery.xoaNhanVien(maNV);
                                if (result) {
                                    Toast.makeText(NhanVienActivity.this, "Xóa nhân viên thành công!", Toast.LENGTH_SHORT).show();
                                    loadDanhSach();
                                } else {
                                    Toast.makeText(NhanVienActivity.this, "Xóa nhân viên thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return true;
            }

            return false;
        });

        popupMenu.show();
    }
}
