package com.example.thuvien.tacgia;

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

public class TacGiaActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    RecyclerView rvTacGia;

    TacGiaAdapter adapter;
    List<TacGia> list;
    TacGiaQuery tacGiaQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tacgia);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvTacGia = findViewById(R.id.rvTacGia);
        btnAdd = findViewById(R.id.btnAdd);

        tacGiaQuery = new TacGiaQuery(this);

        rvTacGia.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new TacGiaAdapter(list, new TacGiaAdapter.OnTacGiaListener() {
            @Override
            public void onLongClick(String maTG, View view) {
                showPopupMenu(view, maTG);
            }
        });

        rvTacGia.setAdapter(adapter);
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
        list = tacGiaQuery.layDanhSachTacGia();
        adapter.capNhatDuLieu(list);
    }

    private void showPopupMenu(View view, final String maTG) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Sửa");
        popupMenu.getMenu().add("Xóa");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Sửa")) {
                Intent intent = new Intent(TacGiaActivity.this, UpdateTacGiaActivity.class);
                intent.putExtra("MaTG", maTG);
                startActivity(intent);
                return true;
            }

            if (title.equals("Xóa")) {
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

            return false;
        });

        popupMenu.show();
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
