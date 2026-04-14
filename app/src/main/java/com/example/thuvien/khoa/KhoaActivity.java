package com.example.thuvien.khoa;

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

public class KhoaActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    RecyclerView rvKhoa;

    KhoaAdapter adapter;
    List<Khoa> list;
    KhoaQuery khoaQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvKhoa = findViewById(R.id.rvKhoa);
        btnAdd = findViewById(R.id.btnAdd);

        khoaQuery = new KhoaQuery(this);

        rvKhoa.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new KhoaAdapter(list, new KhoaAdapter.OnKhoaListener() {
            @Override
            public void onLongClick(String maKhoa, View view) {
                showPopupMenu(view, maKhoa);
            }
        });

        rvKhoa.setAdapter(adapter);
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
                startActivity(new Intent(KhoaActivity.this, AddKhoaActivity.class));
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
        list = khoaQuery.layDanhSachKhoa();
        adapter.capNhatDuLieu(list);
    }

    private void showPopupMenu(View view, final String maKhoa) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Sửa");
        popupMenu.getMenu().add("Xóa");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Sửa")) {
                Intent intent = new Intent(KhoaActivity.this, UpdateKhoaActivity.class);
                intent.putExtra("MaKhoa", maKhoa);
                startActivity(intent);
                return true;
            }

            if (title.equals("Xóa")) {
                new AlertDialog.Builder(KhoaActivity.this)
                        .setTitle("Xóa khoa")
                        .setMessage("Bạn có chắc muốn xóa khoa này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = khoaQuery.xoaKhoa(maKhoa);
                                if (result) {
                                    Toast.makeText(KhoaActivity.this, "Xóa khoa thành công!", Toast.LENGTH_SHORT).show();
                                    loadDanhSach();
                                } else {
                                    Toast.makeText(KhoaActivity.this, "Xóa khoa thất bại!", Toast.LENGTH_SHORT).show();
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