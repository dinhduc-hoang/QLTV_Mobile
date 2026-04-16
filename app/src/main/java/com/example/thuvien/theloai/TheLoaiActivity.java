package com.example.thuvien.theloai;

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
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class TheLoaiActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    ListView lvTheLoai;

    TheLoaiAdapter adapter;
    List<TheLoai> list;
    TheLoaiQuery theLoaiQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theloai);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        lvTheLoai = findViewById(R.id.lvTheLoai);
        btnAdd = findViewById(R.id.btnAdd);

        theLoaiQuery = new TheLoaiQuery(this);

        list = new ArrayList<>();

        adapter = new TheLoaiAdapter(this, list);

        lvTheLoai.setAdapter(adapter);
        registerForContextMenu(lvTheLoai);
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
                List<TheLoai> ketQua = theLoaiQuery.timKiemTheLoai(s.toString());
                adapter.capNhatDuLieu(ketQua);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Sửa");
        menu.add(0, 2, 0, "Xóa");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TheLoai tlSelect = (TheLoai) adapter.getItem(info.position);
        final String maTL = tlSelect.getMaTL();

        switch (item.getItemId()) {
            case 1: // Sửa
                Intent intent = new Intent(TheLoaiActivity.this, UpdateTheLoaiActivity.class);
                intent.putExtra("MaTL", maTL);
                startActivity(intent);
                return true;
            case 2: // Xóa
                new AlertDialog.Builder(TheLoaiActivity.this)
                        .setTitle("Xóa thể loại")
                        .setMessage("Bạn có chắc muốn xóa thể loại này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                xoaTheLoai(maTL);
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void xoaTheLoai(String maTL) {
        if (theLoaiQuery.theLoaiDangDuocSuDung(maTL)) {
            Toast.makeText(this, "Không thể xóa! Thể loại này đang có sách.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = theLoaiQuery.xoaTheLoai(maTL);
        if (result) {
            Toast.makeText(this, "Xóa thể loại thành công!", Toast.LENGTH_SHORT).show();
            loadDanhSach();
        } else {
            Toast.makeText(this, "Xóa thể loại thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
