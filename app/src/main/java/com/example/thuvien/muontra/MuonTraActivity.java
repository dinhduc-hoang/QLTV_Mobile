package com.example.thuvien.muontra;

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

public class MuonTraActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    RecyclerView rvMuonTra;

    MuonTraAdapter adapter;
    List<MuonTra> list;
    MuonTraQuery muonTraQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muontra);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvMuonTra = findViewById(R.id.rvMuonTra);
        btnAdd = findViewById(R.id.btnAdd);

        muonTraQuery = new MuonTraQuery(this);

        rvMuonTra.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        adapter = new MuonTraAdapter(list, new MuonTraAdapter.OnMuonTraListener() {
            @Override
            public void onItemClick(String maMT) {
                Intent intent = new Intent(MuonTraActivity.this, UpdateMuonTraActivity.class);
                intent.putExtra("MaMT", maMT);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(final String maMT, View view) {
                showPopupMenu(view, maMT);
            }
        });

        rvMuonTra.setAdapter(adapter);
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
                startActivity(new Intent(MuonTraActivity.this, AddMuonTraActivity.class));
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
        list = muonTraQuery.layDanhSachMuonTra();
        adapter.capNhatDuLieu(list);
    }

    private void showPopupMenu(View view, final String maMT) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenu().add("Trả sách");
        popupMenu.getMenu().add("Sửa");
        popupMenu.getMenu().add("Xóa");

        popupMenu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();

            if (title.equals("Trả sách")) {
                MuonTra mt = muonTraQuery.layThongTinMuonTraTheoMa(maMT);
                if (mt != null && "Đã trả".equals(mt.getTrangThai())) {
                    Toast.makeText(this, "Phiếu này đã được trả rồi!", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Xác nhận trả sách")
                            .setMessage("Bạn có chắc chắn muốn xác nhận trả sách cho phiếu " + maMT + "?\nSố lượng sách trong kho sẽ được cập nhật lại.")
                            .setPositiveButton("Xác nhận", (dialog, which) -> {
                                boolean result = muonTraQuery.capNhatTrangThaiDaTra(maMT);
                                if (result) {
                                    Toast.makeText(this, "Trả sách thành công!", Toast.LENGTH_SHORT).show();
                                    loadDanhSach();
                                } else {
                                    Toast.makeText(this, "Trả sách thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }
                return true;
            }

            if (title.equals("Sửa")) {
                Intent intent = new Intent(MuonTraActivity.this, UpdateMuonTraActivity.class);
                intent.putExtra("MaMT", maMT);
                startActivity(intent);
                return true;
            }

            if (title.equals("Xóa")) {
                new AlertDialog.Builder(MuonTraActivity.this)
                        .setTitle("Xóa phiếu mượn")
                        .setMessage("Bạn có chắc muốn xóa phiếu mượn này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result = muonTraQuery.xoaPhieuMuon(maMT);
                                if (result) {
                                    Toast.makeText(MuonTraActivity.this, "Xóa phiếu mượn thành công!", Toast.LENGTH_SHORT).show();
                                    loadDanhSach();
                                } else {
                                    Toast.makeText(MuonTraActivity.this, "Xóa phiếu mượn thất bại!", Toast.LENGTH_SHORT).show();
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
