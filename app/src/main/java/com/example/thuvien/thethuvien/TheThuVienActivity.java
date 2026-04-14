package com.example.thuvien.thethuvien;

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

public class TheThuVienActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    RecyclerView rvData;

    TheThuVienAdapter adapter;
    List<TheThuVien> list;
    TheThuVienQuery theThuVienQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thethuvien);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvData = findViewById(R.id.rvData);
        btnAdd = findViewById(R.id.btnAdd);

        theThuVienQuery = new TheThuVienQuery(this);

        rvData.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new TheThuVienAdapter(list, new TheThuVienAdapter.OnItemListener() {
            @Override
            public void onLongClick(String id, View view) {
                showPopupMenu(view, id);
            }
        });

        rvData.setAdapter(adapter);
        loadDanhSach();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TheThuVienActivity.this, AddTheThuVienActivity.class));
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
        list = theThuVienQuery.layDanhSachThe();
        adapter.capNhatDuLieu(list);
    }

    private void showPopupMenu(View view, final String id) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Sửa");
        popupMenu.getMenu().add("Xóa");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Sửa")) {
                Intent intent = new Intent(TheThuVienActivity.this, UpdateTheThuVienActivity.class);
                intent.putExtra("MaThe", id);
                startActivity(intent);
                return true;
            }

            if (title.equals("Xóa")) {
                new AlertDialog.Builder(TheThuVienActivity.this)
                        .setTitle("Xóa thẻ thư viện")
                        .setMessage("Bạn có chắc muốn xóa không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = theThuVienQuery.xoaThe(id);
                                if (result) {
                                    Toast.makeText(TheThuVienActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                    loadDanhSach();
                                } else {
                                    Toast.makeText(TheThuVienActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
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