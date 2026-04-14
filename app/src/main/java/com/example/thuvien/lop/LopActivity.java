package com.example.thuvien.lop;

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

public class LopActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    RecyclerView rvLop;

    LopAdapter adapter;
    List<Lop> list;
    LopQuery lopQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lop);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvLop = findViewById(R.id.rvLop);
        btnAdd = findViewById(R.id.btnAdd);

        lopQuery = new LopQuery(this);

        rvLop.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new LopAdapter(list, new LopAdapter.OnLopListener() {
            @Override
            public void onLongClick(String maLop, View view) {
                showPopupMenu(view, maLop);
            }
        });

        rvLop.setAdapter(adapter);
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
                startActivity(new Intent(LopActivity.this, AddLopActivity.class));
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
        list = lopQuery.layDanhSachLop();
        adapter.capNhatDuLieu(list);
    }

    private void showPopupMenu(View view, final String maLop) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Sửa");
        popupMenu.getMenu().add("Xóa");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Sửa")) {
                Intent intent = new Intent(LopActivity.this, UpdateLopActivity.class);
                intent.putExtra("MaLop", maLop);
                startActivity(intent);
                return true;
            }

            if (title.equals("Xóa")) {
                new AlertDialog.Builder(LopActivity.this)
                        .setTitle("Xóa lớp")
                        .setMessage("Bạn có chắc muốn xóa lớp này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = lopQuery.xoaLop(maLop);
                                if (result) {
                                    Toast.makeText(LopActivity.this, "Xóa lớp thành công!", Toast.LENGTH_SHORT).show();
                                    loadDanhSach();
                                } else {
                                    Toast.makeText(LopActivity.this, "Xóa lớp thất bại!", Toast.LENGTH_SHORT).show();
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