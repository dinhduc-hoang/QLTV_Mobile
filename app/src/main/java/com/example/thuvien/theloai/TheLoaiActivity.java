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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class TheLoaiActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageView btnAdd;
    ListView lvData;

    TheLoaiQuery theLoaiQuery;

    List<TheLoai> listGoc = new ArrayList<>();
    List<TheLoai> listHienThi = new ArrayList<>();

    TheLoaiAdapter adapter;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theloai);

        edtSearch = findViewById(R.id.edtSearch);
        lvData = findViewById(R.id.lvTheLoai);
        btnAdd = findViewById(R.id.btnAdd);

        theLoaiQuery = new TheLoaiQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new TheLoaiAdapter(this, R.layout.item_theloai, listHienThi);
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
                startActivity(new Intent(TheLoaiActivity.this, AddTheLoaiActivity.class));
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
        listGoc.addAll(theLoaiQuery.layDanhSachTheLoai());
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
            TheLoai tl = listGoc.get(i);

            String maTL = tl.getMaTL();
            String tenTL = tl.getTenTL();

            if (maTL == null) maTL = "";
            if (tenTL == null) tenTL = "";

            if (tuKhoa.equals("")
                    || maTL.toLowerCase().contains(tuKhoa)
                    || tenTL.toLowerCase().contains(tuKhoa)) {
                listHienThi.add(tl);
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

        final TheLoai tlEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(TheLoaiActivity.this, UpdateTheLoaiActivity.class);
            intent.putExtra("MaTL", tlEdit.getMaTL());
            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc xóa Thể loại này?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    xoaTheLoai(tlEdit.getMaTL());
                }
            });
            builder.setNegativeButton("Không", null);
            builder.show();
        }

        return true;
    }

    private void xoaTheLoai(String maTL) {
        if (theLoaiQuery.theLoaiDangDuocSuDung(maTL)) {
            Toast.makeText(this, "Không thể xóa! Thể loại này đang có sách.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = theLoaiQuery.xoaTheLoai(maTL);
        if (result) {
            Toast.makeText(this, "Xóa thể loại thành công!", Toast.LENGTH_SHORT).show();
            loadData();
        } else {
            Toast.makeText(this, "Xóa thể loại thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
