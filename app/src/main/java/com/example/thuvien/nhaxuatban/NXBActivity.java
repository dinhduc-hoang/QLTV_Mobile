package com.example.thuvien.nhaxuatban;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class NXBActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    ListView lvNXB;

    NXBAdapter adapter;
    List<NXB> list;
    NXBQuery nxbQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nxb);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        lvNXB = findViewById(R.id.lvNXB);
        btnAdd = findViewById(R.id.btnAdd);

        nxbQuery = new NXBQuery(this);

        list = new ArrayList<>();

        adapter = new NXBAdapter(this, list);

        lvNXB.setAdapter(adapter);
        registerForContextMenu(lvNXB);
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
                List<NXB> ketQua = nxbQuery.timKiemNXB(s.toString());
                adapter.capNhatDuLieu(ketQua);
            }

            @Override
            public void afterTextChanged(Editable e) {
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Chọn hành động");
        menu.add(0, 1, 0, "Sửa");
        menu.add(0, 2, 1, "Xóa");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        NXB selectedNXB = (NXB) adapter.getItem(info.position);

        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(NXBActivity.this, UpdateNXBActivity.class);
                intent.putExtra("MaNXB", selectedNXB.getMaNXB());
                startActivity(intent);
                return true;
            case 2:
                new AlertDialog.Builder(NXBActivity.this)
                        .setTitle("Xóa nhà xuất bản")
                        .setMessage("Bạn có chắc muốn xóa nhà xuất bản này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                xoaNXB(selectedNXB.getMaNXB());
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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

    private void xoaNXB(String maNXB) {
        if (nxbQuery.nxbDangDuocSuDung(maNXB)) {
            Toast.makeText(this, "Không thể xóa! Nhà xuất bản này đang có sách.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = nxbQuery.xoa(maNXB);
        if (result) {
            Toast.makeText(this, "Xóa nhà xuất bản thành công!", Toast.LENGTH_SHORT).show();
            loadDanhSach();
        } else {
            Toast.makeText(this, "Xóa nhà xuất bản thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
