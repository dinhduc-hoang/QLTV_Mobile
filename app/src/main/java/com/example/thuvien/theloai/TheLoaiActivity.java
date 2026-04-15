package com.example.thuvien.theloai;

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

public class TheLoaiActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    RecyclerView rvTheLoai;

    TheLoaiAdapter adapter;
    List<TheLoai> list;
    TheLoaiQuery theLoaiQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theloai);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvTheLoai = findViewById(R.id.rvTheLoai);
        btnAdd = findViewById(R.id.btnAdd);

        theLoaiQuery = new TheLoaiQuery(this);

        rvTheLoai.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new TheLoaiAdapter(list, new TheLoaiAdapter.OnTheLoaiListener() {
            @Override
            public void onLongClick(String maTL, View view) {
                showPopupMenu(view, maTL);
            }
        });

        rvTheLoai.setAdapter(adapter);
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
                startActivity(new Intent(TheLoaiActivity.this, AddTheLoaiActivity.class));
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
        list = theLoaiQuery.layDanhSachTheLoai();
        adapter.capNhatDuLieu(list);
    }

    private void showPopupMenu(View view, final String maTL) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Sửa");
        popupMenu.getMenu().add("Xóa");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Sửa")) {
                Intent intent = new Intent(TheLoaiActivity.this, UpdateTheLoaiActivity.class);
                intent.putExtra("MaTL", maTL);
                startActivity(intent);
                return true;
            }

            if (title.equals("Xóa")) {
                new AlertDialog.Builder(TheLoaiActivity.this)
                        .setTitle("Xóa thể loại")
                        .setMessage("Bạn có chắc muốn xóa thể loại này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = theLoaiQuery.xoaTheLoai(maTL);
                                if (result) {
                                    Toast.makeText(TheLoaiActivity.this, "Xóa thể loại thành công!", Toast.LENGTH_SHORT).show();
                                    loadDanhSach();
                                } else {
                                    Toast.makeText(TheLoaiActivity.this, "Xóa thể loại thất bại!", Toast.LENGTH_SHORT).show();
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
