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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class LopActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    ListView lvLop;

    LopAdapter adapter;
    List<Lop> list;
    LopQuery lopQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lop);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        lvLop = findViewById(R.id.lvLop);
        btnAdd = findViewById(R.id.btnAdd);

        lopQuery = new LopQuery(this);

        list = new ArrayList<>();

        adapter = new LopAdapter(this, list);

        lvLop.setAdapter(adapter);
        registerForContextMenu(lvLop);
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
                startActivity(new Intent(LopActivity.this, AddLopActivity.class));
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {
            }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                List<Lop> ketQua = lopQuery.timKiemLop(s.toString());
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
        Lop selectedLop = (Lop) adapter.getItem(info.position);

        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(LopActivity.this, UpdateLopActivity.class);
                intent.putExtra("MaLop", selectedLop.getMaLop());
                startActivity(intent);
                return true;
            case 2:
                new AlertDialog.Builder(LopActivity.this)
                        .setTitle("Xóa lớp")
                        .setMessage("Bạn có chắc muốn xóa lớp này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                xoaLop(selectedLop.getMaLop());
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
        list = lopQuery.layDanhSachLop();
        adapter.capNhatDuLieu(list);
    }

    private void xoaLop(String maLop) {
        if (lopQuery.lopDangDuocSuDung(maLop)) {
            Toast.makeText(this, "Không thể xóa! Lớp này đang có độc giả thuộc về.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = lopQuery.xoaLop(maLop);
        if (result) {
            Toast.makeText(this, "Xóa lớp thành công!", Toast.LENGTH_SHORT).show();
            loadDanhSach();
        } else {
            Toast.makeText(this, "Xóa lớp thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
