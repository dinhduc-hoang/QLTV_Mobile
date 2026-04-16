package com.example.thuvien.tacgia;

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

public class TacGiaActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    ListView lvTacGia;

    TacGiaAdapter adapter;
    List<TacGia> list;
    TacGiaQuery tacGiaQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tacgia);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        lvTacGia = findViewById(R.id.lvTacGia);
        btnAdd = findViewById(R.id.btnAdd);

        tacGiaQuery = new TacGiaQuery(this);

        list = new ArrayList<>();

        adapter = new TacGiaAdapter(this, list);

        lvTacGia.setAdapter(adapter);
        registerForContextMenu(lvTacGia);
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
                startActivity(new Intent(TacGiaActivity.this, AddTacGiaActivity.class));
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {
            }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                List<TacGia> ketQua = tacGiaQuery.timKiemTacGia(s.toString());
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
        list = tacGiaQuery.layDanhSachTacGia();
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
        TacGia tgSelect = (TacGia) adapter.getItem(info.position);
        final String maTG = tgSelect.getMaTG();

        switch (item.getItemId()) {
            case 1: // Sửa
                Intent intent = new Intent(TacGiaActivity.this, UpdateTacGiaActivity.class);
                intent.putExtra("MaTG", maTG);
                startActivity(intent);
                return true;
            case 2: // Xóa
                new AlertDialog.Builder(TacGiaActivity.this)
                        .setTitle("Xóa tác giả")
                        .setMessage("Bạn có chắc muốn xóa tác giả này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                xoaTacGia(maTG);
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void xoaTacGia(String maTG) {
        if (tacGiaQuery.tacGiaDangDuocSuDung(maTG)) {
            Toast.makeText(this, "Không thể xóa! Tác giả này đang có sách.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = tacGiaQuery.xoaTacGia(maTG);
        if (result) {
            Toast.makeText(this, "Xóa tác giả thành công!", Toast.LENGTH_SHORT).show();
            loadDanhSach();
        } else {
            Toast.makeText(this, "Xóa tác giả thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
