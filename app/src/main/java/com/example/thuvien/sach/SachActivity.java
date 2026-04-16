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

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class SachActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageView btnAdd;
    ListView lvData;

    SachQuery sachQuery;

    List<Sach> listGoc = new ArrayList<>();
    List<Sach> listHienThi = new ArrayList<>();

    SachAdapter adapter;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sach);

        edtSearch = findViewById(R.id.edtSearch);
        lvData = findViewById(R.id.lvSach);
        btnAdd = findViewById(R.id.btnAdd);

        sachQuery = new SachQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new SachAdapter(this, R.layout.item_sach, listHienThi);
        lvData.setAdapter(adapter);

        loadData();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadData();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SachActivity.this, AddSachActivity.class);
                startActivity(intent);
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
        String keyword = edtSearch.getText().toString().trim();
        listGoc.clear();
        if (keyword.isEmpty()) {
            listGoc.addAll(sachQuery.layDanhSachSach());
        } else {
            listGoc.addAll(sachQuery.timKiemSach(keyword));
        }

        listHienThi.clear();
        listHienThi.addAll(listGoc);
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

        final Sach sachEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(SachActivity.this, UpdateSachActivity.class);
            intent.putExtra("MaSach", sachEdit.getMaSach());
            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc xóa Sách này?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    xoaSach(sachEdit.getMaSach());
                }
            });
            builder.setNegativeButton("Không", null);
            builder.show();
        }

        return true;
    }

    private void xoaSach(String maSach) {
        if (sachQuery.sachDangDuocMuon(maSach)) {
            Toast.makeText(this, "Không thể xóa! Sách này đang được mượn.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = sachQuery.xoaSach(maSach);
        if (result) {
            Toast.makeText(this, "Xóa sách thành công!", Toast.LENGTH_SHORT).show();
            loadData();
        } else {
            Toast.makeText(this, "Xóa sách thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
