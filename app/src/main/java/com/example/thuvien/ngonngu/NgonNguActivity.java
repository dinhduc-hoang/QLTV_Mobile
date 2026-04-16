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
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class NgonNguActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    ListView lvNgonNgu;

    NgonNguAdapter adapter;
    List<NgonNgu> list;
    NgonNguQuery ngonNguQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngonngu);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        lvNgonNgu = findViewById(R.id.lvNgonNgu);
        btnAdd = findViewById(R.id.btnAdd);

        ngonNguQuery = new NgonNguQuery(this);

        list = new ArrayList<>();

        adapter = new NgonNguAdapter(this, list);

        lvNgonNgu.setAdapter(adapter);
        registerForContextMenu(lvNgonNgu);
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
                List<NgonNgu> ketQua = ngonNguQuery.timKiemNgonNgu(s.toString());
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
        list = ngonNguQuery.layDanhSachNgonNgu();
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
        NgonNgu nnSelect = (NgonNgu) adapter.getItem(info.position);
        final String maNN = nnSelect.getMaNN();

        switch (item.getItemId()) {
            case 1: // Sửa
                Intent intent = new Intent(NgonNguActivity.this, UpdateNgonNguActivity.class);
                intent.putExtra("MaNN", maNN);
                startActivity(intent);
                return true;
            case 2: // Xóa
                new AlertDialog.Builder(NgonNguActivity.this)
                        .setTitle("Xóa ngôn ngữ")
                        .setMessage("Bạn có chắc muốn xóa ngôn ngữ này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                xoaNgonNgu(maNN);
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void xoaNgonNgu(String maNN) {
        if (ngonNguQuery.ngonNguDangDuocSuDung(maNN)) {
            Toast.makeText(this, "Không thể xóa! Ngôn ngữ này đang được sử dụng cho sách.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = ngonNguQuery.xoaNgonNgu(maNN);
        if (result) {
            Toast.makeText(this, "Xóa ngôn ngữ thành công!", Toast.LENGTH_SHORT).show();
            loadDanhSach();
        } else {
            Toast.makeText(this, "Xóa ngôn ngữ thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
