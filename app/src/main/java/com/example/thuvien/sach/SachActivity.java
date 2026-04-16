package com.example.thuvien.sach;

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

public class SachActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    ListView lvSach;

    SachAdapter adapter;
    List<Sach> list;
    SachQuery sachQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sach);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        lvSach = findViewById(R.id.lvSach);
        btnAdd = findViewById(R.id.btnAdd);

        sachQuery = new SachQuery(this);

        list = new ArrayList<>();

        adapter = new SachAdapter(this, list);

        lvSach.setAdapter(adapter);
        registerForContextMenu(lvSach);
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
                // Gọi SQL tìm kiếm và cập nhật thẳng vào adapter
                List<Sach> ketQua = sachQuery.timKiemSach(charSequence.toString());
                adapter.capNhatDuLieu(ketQua);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Sửa");
        menu.add(0, 2, 0, "Xóa");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Sach sachSelect = (Sach) adapter.getItem(info.position);
        final String maSach = sachSelect.getMaSach();

        switch (item.getItemId()) {
            case 1: // Sửa
                Intent intent = new Intent(SachActivity.this, UpdateSachActivity.class);
                intent.putExtra("MaSach", maSach);
                startActivity(intent);
                return true;
            case 2: // Xóa
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
        return super.onContextItemSelected(item);
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
