package com.example.thuvien.khoa;

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

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class KhoaActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageView btnAdd;
    ListView lvData;

    KhoaQuery khoaQuery;

    List<Khoa> listGoc = new ArrayList<>();
    List<Khoa> listHienThi = new ArrayList<>();

    KhoaAdapter adapter;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa);

        edtSearch = findViewById(R.id.edtSearch);
        lvData = findViewById(R.id.lvKhoa);
        btnAdd = findViewById(R.id.btnAdd);

        khoaQuery = new KhoaQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new KhoaAdapter(this, R.layout.item_khoa, listHienThi);
        lvData.setAdapter(adapter);

        loadData();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KhoaActivity.this, AddKhoaActivity.class));
            }
        });

        registerForContextMenu(lvData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        listGoc.clear();
        listGoc.addAll(khoaQuery.layDanhSachKhoa());
        filterData(edtSearch.getText().toString());
    }

    private void filterData(String keyword) {
        listHienThi.clear();

        String tuKhoa = keyword;
        if (tuKhoa == null) {
            tuKhoa = "";
        }
        tuKhoa = tuKhoa.trim().toLowerCase();

        for (int i = 0; i < listGoc.size(); i++) {
            Khoa khoa = listGoc.get(i);

            String maKhoa = khoa.getMaKhoa();
            String tenKhoa = khoa.getTenKhoa();

            if (maKhoa == null) maKhoa = "";
            if (tenKhoa == null) tenKhoa = "";

            if (tuKhoa.equals("")
                    || maKhoa.toLowerCase().contains(tuKhoa)
                    || tenKhoa.toLowerCase().contains(tuKhoa)) {
                listHienThi.add(khoa);
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedPosition = info.position;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (selectedPosition < 0 || selectedPosition >= listHienThi.size()) {
            return super.onContextItemSelected(item);
        }

        final Khoa khoaEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(KhoaActivity.this, UpdateKhoaActivity.class);
            intent.putExtra("MaKhoa", khoaEdit.getMaKhoa());
            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc xóa Khoa này?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    xoaKhoa(khoaEdit.getMaKhoa());
                }
            });
            builder.setNegativeButton("Không", null);
            builder.show();
        }

        return true;
    }

    private void xoaKhoa(String maKhoa) {
        if (khoaQuery.khoaDangDuocSuDung(maKhoa)) {
            Toast.makeText(this, "Không thể xóa! Khoa này đang có lớp hoặc độc giả thuộc về.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = khoaQuery.xoaKhoa(maKhoa);
        if (result) {
            Toast.makeText(this, "Xóa khoa thành công!", Toast.LENGTH_SHORT).show();
            loadData();
        } else {
            Toast.makeText(this, "Xóa khoa thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
