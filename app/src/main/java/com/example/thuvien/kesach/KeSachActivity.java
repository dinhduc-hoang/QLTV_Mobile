package com.example.thuvien.kesach;

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

public class KeSachActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    RecyclerView rvKeSach;

    KeSachAdapter adapter;
    List<KeSach> list;
    KeSachQuery keSachQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kesach);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvKeSach = findViewById(R.id.rvKeSach);
        btnAdd = findViewById(R.id.btnAdd);

        keSachQuery = new KeSachQuery(this);

        rvKeSach.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new KeSachAdapter(list, new KeSachAdapter.OnKeSachListener() {
            @Override
            public void onLongClick(String maViTri, View view) {
                showPopupMenu(view, maViTri);
            }
        });

        rvKeSach.setAdapter(adapter);
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
                startActivity(new Intent(KeSachActivity.this, AddKeSachActivity.class));
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
        list = keSachQuery.layDanhSachKeSach();
        adapter.capNhatDuLieu(list);
    }

    private void showPopupMenu(View view, final String maViTri) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Sửa");
        popupMenu.getMenu().add("Xóa");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Sửa")) {
                Intent intent = new Intent(KeSachActivity.this, UpdateKeSachActivity.class);
                intent.putExtra("MaViTri", maViTri);
                startActivity(intent);
                return true;
            }

            if (title.equals("Xóa")) {
                new AlertDialog.Builder(KeSachActivity.this)
                        .setTitle("Xóa kệ sách")
                        .setMessage("Bạn có chắc muốn xóa kệ sách này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = keSachQuery.xoaKeSach(maViTri);
                                if (result) {
                                    Toast.makeText(KeSachActivity.this, "Xóa kệ sách thành công!", Toast.LENGTH_SHORT).show();
                                    loadDanhSach();
                                } else {
                                    Toast.makeText(KeSachActivity.this, "Xóa kệ sách thất bại!", Toast.LENGTH_SHORT).show();
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