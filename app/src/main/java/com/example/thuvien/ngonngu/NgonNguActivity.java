package com.example.thuvien.ngonngu;

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

public class NgonNguActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    RecyclerView rvNgonNgu;

    NgonNguAdapter adapter;
    List<NgonNgu> list;
    NgonNguQuery ngonNguQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngonngu);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvNgonNgu = findViewById(R.id.rvNgonNgu);
        btnAdd = findViewById(R.id.btnAdd);

        ngonNguQuery = new NgonNguQuery(this);

        rvNgonNgu.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new NgonNguAdapter(list, new NgonNguAdapter.OnNgonNguListener() {
            @Override
            public void onLongClick(String maNN, View view) {
                showPopupMenu(view, maNN);
            }
        });

        rvNgonNgu.setAdapter(adapter);
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
                startActivity(new Intent(NgonNguActivity.this, AddNgonNguActivity.class));
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
        list = ngonNguQuery.layDanhSachNgonNgu();
        adapter.capNhatDuLieu(list);
    }

    private void showPopupMenu(View view, final String maNN) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Sửa");
        popupMenu.getMenu().add("Xóa");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Sửa")) {
                Intent intent = new Intent(NgonNguActivity.this, UpdateNgonNguActivity.class);
                intent.putExtra("MaNN", maNN);
                startActivity(intent);
                return true;
            }

            if (title.equals("Xóa")) {
                new AlertDialog.Builder(NgonNguActivity.this)
                        .setTitle("Xóa ngôn ngữ")
                        .setMessage("Bạn có chắc muốn xóa ngôn ngữ này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = ngonNguQuery.xoaNgonNgu(maNN);
                                if (result) {
                                    Toast.makeText(NgonNguActivity.this, "Xóa ngôn ngữ thành công!", Toast.LENGTH_SHORT).show();
                                    loadDanhSach();
                                } else {
                                    Toast.makeText(NgonNguActivity.this, "Xóa ngôn ngữ thất bại!", Toast.LENGTH_SHORT).show();
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