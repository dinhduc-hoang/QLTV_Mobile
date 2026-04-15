package com.example.thuvien.docgia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class DocGiaActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    RecyclerView rvDocGia;

    DocGiaAdapter adapter;
    List<DocGia> list;
    DocGiaQuery docGiaQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docgia);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvDocGia = findViewById(R.id.rvDocGia);
        btnAdd = findViewById(R.id.btnAdd);

        docGiaQuery = new DocGiaQuery(this);

        rvDocGia.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new DocGiaAdapter(list, new DocGiaAdapter.OnDocGiaListener() {
            @Override
            public void onLongClick(String maDG, View view) {
                showPopupMenu(view, maDG);
            }
        });

        rvDocGia.setAdapter(adapter);
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
                adapter.getFilter().filter(s);
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

    private void showPopupMenu(View view, final String maDG) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Sửa");
        popupMenu.getMenu().add("Xóa");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Sửa")) {
                Intent intent = new Intent(DocGiaActivity.this, UpdateDocGiaActivity.class);
                intent.putExtra("MaDG", maDG);
                startActivity(intent);
                return true;
            }

            if (title.equals("Xóa")) {
                new AlertDialog.Builder(DocGiaActivity.this)
                        .setTitle("Xóa độc giả")
                        .setMessage("Bạn có chắc muốn xóa độc giả này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = docGiaQuery.xoaDocGia(maDG);
                                if (result) {
                                    Toast.makeText(DocGiaActivity.this, "Xóa độc giả thành công!", Toast.LENGTH_SHORT).show();
                                    loadDanhSach();
                                } else {
                                    Toast.makeText(DocGiaActivity.this, "Xóa độc giả thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return true;
            }

            return false;
        });

        popupMenu.show();
    }
}
