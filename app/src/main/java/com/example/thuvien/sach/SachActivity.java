package com.example.thuvien.sach;

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

public class SachActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    RecyclerView rvSach;

    SachAdapter adapter;
    List<Sach> list;
    SachQuery sachQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sach);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvSach = findViewById(R.id.rvSach);
        btnAdd = findViewById(R.id.btnAdd);

        sachQuery = new SachQuery(this);

        rvSach.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new SachAdapter(list, new SachAdapter.OnSachListener() {
            @Override
            public void onLongClick(String maSach, View view) {
                showPopupMenu(view, maSach);
            }
        });

        rvSach.setAdapter(adapter);
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
                Intent intent = new Intent(SachActivity.this, AddSachActivity.class);
                startActivity(intent);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDanhSach();
    }

    private void loadDanhSach() {
        list = sachQuery.layDanhSachSach();
        adapter.capNhatDuLieu(list);
    }

    private void showPopupMenu(View view, final String maSach) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Sửa");
        popupMenu.getMenu().add("Xóa");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Sửa")) {
                Intent intent = new Intent(SachActivity.this, UpdateSachActivity.class);
                intent.putExtra("MaSach", maSach);
                startActivity(intent);
                return true;
            }

            if (title.equals("Xóa")) {
                new AlertDialog.Builder(SachActivity.this)
                        .setTitle("Xóa sách")
                        .setMessage("Bạn có chắc muốn xóa sách này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                xoaSach(maSach);
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

    private void xoaSach(String maSach) {
        if (sachQuery.sachDangDuocMuon(maSach)) {
            Toast.makeText(this, "Không thể xóa! Sách này đang được mượn.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = sachQuery.xoaSach(maSach);

        if (result) {
            Toast.makeText(this, "Xóa sách thành công!", Toast.LENGTH_SHORT).show();
            loadDanhSach();
        } else {
            Toast.makeText(this, "Xóa sách thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}