package com.example.thuvien.ngonngu;

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

public class NgonNguActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageView btnAdd;
    ListView lvData;

    NgonNguQuery ngonNguQuery;

    List<NgonNgu> listGoc = new ArrayList<>();
    List<NgonNgu> listHienThi = new ArrayList<>();

    NgonNguAdapter adapter;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngonngu);

        edtSearch = findViewById(R.id.edtSearch);
        lvData = findViewById(R.id.lvNgonNgu);
        btnAdd = findViewById(R.id.btnAdd);

        ngonNguQuery = new NgonNguQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new NgonNguAdapter(this, R.layout.item_ngonngu, listHienThi);
        lvData.setAdapter(adapter);

        loadData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NgonNguActivity.this, AddNgonNguActivity.class));
            }
        });

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

        registerForContextMenu(lvData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        listGoc.clear();
        listGoc.addAll(ngonNguQuery.layDanhSachNgonNgu());
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
            NgonNgu ngonNgu = listGoc.get(i);

            String maNN = ngonNgu.getMaNN();
            String tenNN = ngonNgu.getTenNN();

            if (maNN == null) maNN = "";
            if (tenNN == null) tenNN = "";

            if (tuKhoa.equals("")
                    || maNN.toLowerCase().contains(tuKhoa)
                    || tenNN.toLowerCase().contains(tuKhoa)) {
                listHienThi.add(ngonNgu);
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

        final NgonNgu nnEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(NgonNguActivity.this, UpdateNgonNguActivity.class);
            intent.putExtra("MaNN", nnEdit.getMaNN());
            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc xóa Ngôn ngữ này?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    xoaNgonNgu(nnEdit.getMaNN());
                }
            });
            builder.setNegativeButton("Không", null);
            builder.show();
        }

        return true;
    }

    private void xoaNgonNgu(String maNN) {
        if (ngonNguQuery.ngonNguDangDuocSuDung(maNN)) {
            Toast.makeText(this, "Không thể xóa! Ngôn ngữ này đang được sử dụng cho sách.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = ngonNguQuery.xoaNgonNgu(maNN);
        if (result) {
            Toast.makeText(this, "Xóa ngôn ngữ thành công!", Toast.LENGTH_SHORT).show();
            loadData();
        } else {
            Toast.makeText(this, "Xóa ngôn ngữ thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
