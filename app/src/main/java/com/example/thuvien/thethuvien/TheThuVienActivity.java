package com.example.thuvien.thethuvien;

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

public class TheThuVienActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    ListView lvData;

    TheThuVienAdapter adapter;
    List<TheThuVien> list;
    TheThuVienQuery theThuVienQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thethuvien);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        lvData = findViewById(R.id.lvData);
        btnAdd = findViewById(R.id.btnAdd);

        theThuVienQuery = new TheThuVienQuery(this);

        list = new ArrayList<>();

        adapter = new TheThuVienAdapter(this, list);

        lvData.setAdapter(adapter);
        registerForContextMenu(lvData);
        loadDanhSach();

        imgBack.setOnClickListener(v -> finish());

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(TheThuVienActivity.this, AddTheThuVienActivity.class))
        );

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                list = theThuVienQuery.timKiemThe(s.toString());
                adapter.capNhatDuLieu(list);
            }

            @Override
            public void afterTextChanged(Editable e) {}
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Sửa");
        menu.add(0, 2, 0, "Xóa");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TheThuVien theSelect = (TheThuVien) adapter.getItem(info.position);
        final String id = theSelect.getMaThe();

        switch (item.getItemId()) {
            case 1: // Sửa
                Intent intent = new Intent(TheThuVienActivity.this, UpdateTheThuVienActivity.class);
                intent.putExtra("MaThe", id);
                startActivity(intent);
                return true;
            case 2: // Xóa
                new AlertDialog.Builder(TheThuVienActivity.this)
                        .setTitle("Xóa thẻ thư viện")
                        .setMessage("Bạn có chắc muốn xóa không?")
                        .setPositiveButton("Xóa", (dialogInterface, i) -> {
                            if (theThuVienQuery.dangMuonSach(theSelect.getMaDG())) {
                                Toast.makeText(TheThuVienActivity.this,
                                        "Độc giả đang mượn sách, không thể xóa!",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            boolean result = theThuVienQuery.xoaThe(id);
                            if (result) {
                                Toast.makeText(TheThuVienActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                loadDanhSach();
                            } else {
                                Toast.makeText(TheThuVienActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
