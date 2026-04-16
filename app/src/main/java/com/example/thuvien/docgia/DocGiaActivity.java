package com.example.thuvien.docgia;

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

public class DocGiaActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    ListView lvDocGia;

    DocGiaAdapter adapter;
    List<DocGia> list;
    DocGiaQuery docGiaQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docgia);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        lvDocGia = findViewById(R.id.lvDocGia);
        btnAdd = findViewById(R.id.btnAdd);

        docGiaQuery = new DocGiaQuery(this);

        list = new ArrayList<>();

        adapter = new DocGiaAdapter(this, list);

        lvDocGia.setAdapter(adapter);
        registerForContextMenu(lvDocGia);
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
                startActivity(new Intent(DocGiaActivity.this, AddDocGiaActivity.class));
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {
            }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                List<DocGia> ketQua = docGiaQuery.timKiemDocGia(s.toString());
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
        list = docGiaQuery.layDanhSachDocGia();
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
        DocGia dgSelect = (DocGia) adapter.getItem(info.position);
        final String maDG = dgSelect.getMaDG();

        switch (item.getItemId()) {
            case 1: // Sửa
                Intent intent = new Intent(DocGiaActivity.this, UpdateDocGiaActivity.class);
                intent.putExtra("MaDG", maDG);
                startActivity(intent);
                return true;
            case 2: // Xóa
                new AlertDialog.Builder(DocGiaActivity.this)
                        .setTitle("Xóa độc giả")
                        .setMessage("Bạn có chắc muốn xóa độc giả này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                xoaDocGia(maDG);
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void xoaDocGia(String maDG) {
        if (docGiaQuery.docGiaDangMuonSach(maDG)) {
            Toast.makeText(this, "Không thể xóa! Độc giả đang mượn sách hoặc có thẻ thư viện.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = docGiaQuery.xoaDocGia(maDG);
        if (result) {
            Toast.makeText(this, "Xóa độc giả thành công!", Toast.LENGTH_SHORT).show();
            loadDanhSach();
        } else {
            Toast.makeText(this, "Xóa độc giả thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
