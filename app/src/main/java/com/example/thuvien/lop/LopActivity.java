package com.example.thuvien.lop;

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

public class LopActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageView btnAdd;
    ListView lvData;

    LopQuery lopQuery;

    List<Lop> listGoc = new ArrayList<>();
    List<Lop> listHienThi = new ArrayList<>();

    LopAdapter adapter;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lop);

        edtSearch = findViewById(R.id.edtSearch);
        lvData = findViewById(R.id.lvLop);
        btnAdd = findViewById(R.id.btnAdd);

        lopQuery = new LopQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new LopAdapter(this, R.layout.item_lop, listHienThi);
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
                startActivity(new Intent(LopActivity.this, AddLopActivity.class));
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
            listGoc.addAll(lopQuery.layDanhSachLop());
        } else {
            listGoc.addAll(lopQuery.timKiemLop(keyword));
        }

        listHienThi.clear();
        listHienThi.addAll(listGoc);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void filterData(String keyword) {
        loadData();
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

        final Lop lopEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(LopActivity.this, UpdateLopActivity.class);
            intent.putExtra("MaLop", lopEdit.getMaLop());
            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc xóa Lớp này?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    xoaLop(lopEdit.getMaLop());
                }
            });
            builder.setNegativeButton("Không", null);
            builder.show();
        }

        return true;
    }

    private void xoaLop(String maLop) {
        if (lopQuery.lopDangDuocSuDung(maLop)) {
            Toast.makeText(this, "Không thể xóa! Lớp này đang có độc giả thuộc về.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = lopQuery.xoaLop(maLop);
        if (result) {
            Toast.makeText(this, "Xóa lớp thành công!", Toast.LENGTH_SHORT).show();
            loadData();
        } else {
            Toast.makeText(this, "Xóa lớp thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
