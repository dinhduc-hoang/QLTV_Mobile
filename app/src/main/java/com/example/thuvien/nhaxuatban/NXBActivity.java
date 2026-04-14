package com.example.thuvien.nhaxuatban;

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

public class NXBActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    RecyclerView rvNXB;

    NXBAdapter adapter;
    List<NXB> list;
    NXBQuery nxbQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nxb);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvNXB = findViewById(R.id.rvNXB);
        btnAdd = findViewById(R.id.btnAdd);

        nxbQuery = new NXBQuery(this);

        rvNXB.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new NXBAdapter(list, new NXBAdapter.OnNXBListener() {
            @Override
            public void onLongClick(String maNXB, View view) {
                showPopupMenu(view, maNXB);
            }
        });

        rvNXB.setAdapter(adapter);
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
                startActivity(new Intent(NXBActivity.this, AddNXBActivity.class));
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
        list = nxbQuery.layDanhSachNXB();
        adapter.capNhatDuLieu(list);
    }

    private void showPopupMenu(View view, final String maNXB) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Sửa");
        popupMenu.getMenu().add("Xóa");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Sửa")) {
                Intent intent = new Intent(NXBActivity.this, UpdateNXBActivity.class);
                intent.putExtra("MaNXB", maNXB);
                startActivity(intent);
                return true;
            }

            if (title.equals("Xóa")) {
                new AlertDialog.Builder(NXBActivity.this)
                        .setTitle("Xóa nhà xuất bản")
                        .setMessage("Bạn có chắc muốn xóa nhà xuất bản này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = nxbQuery.xoa(maNXB);
                                if (result) {
                                    Toast.makeText(NXBActivity.this, "Xóa nhà xuất bản thành công!", Toast.LENGTH_SHORT).show();
                                    loadDanhSach();
                                } else {
                                    Toast.makeText(NXBActivity.this, "Xóa nhà xuất bản thất bại!", Toast.LENGTH_SHORT).show();
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