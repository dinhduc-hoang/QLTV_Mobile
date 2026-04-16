package com.example.thuvien.muontra;

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

public class MuonTraActivity extends AppCompatActivity {

    ImageView imgBack, btnAdd;
    EditText edtSearch;
    ListView lvMuonTra;

    MuonTraAdapter adapter;
    List<MuonTra> list;
    MuonTraQuery muonTraQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muontra);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        lvMuonTra = findViewById(R.id.lvMuonTra);
        btnAdd = findViewById(R.id.btnAdd);

        muonTraQuery = new MuonTraQuery(this);

        list = new ArrayList<>();

        adapter = new MuonTraAdapter(this, list);

        lvMuonTra.setAdapter(adapter);
        registerForContextMenu(lvMuonTra);
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                list = muonTraQuery.timKiemMuonTra(query);
                adapter.capNhatDuLieu(list);
            }

            @Override
            public void afterTextChanged(Editable s) {
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Trả sách");
        menu.add(0, 2, 0, "Sửa");
        menu.add(0, 3, 0, "Xóa");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        MuonTra mtSelect = (MuonTra) adapter.getItem(info.position);
        final String maMT = mtSelect.getMaMT();

        switch (item.getItemId()) {
            case 1: // Trả sách
                if ("Đã trả".equals(mtSelect.getTrangThai())) {
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
            case 2: // Sửa
                Intent intent = new Intent(MuonTraActivity.this, UpdateMuonTraActivity.class);
                intent.putExtra("MaMT", maMT);
                startActivity(intent);
                return true;
            case 3: // Xóa
                new AlertDialog.Builder(MuonTraActivity.this)
                        .setTitle("Xóa phiếu mượn")
                        .setMessage("Bạn có chắc muốn xóa phiếu mượn này không?")
                        .setPositiveButton("Xóa", (dialogInterface, i) -> {
                            boolean result = muonTraQuery.xoaPhieuMuon(maMT);
                            if (result) {
                                Toast.makeText(MuonTraActivity.this, "Xóa phiếu mượn thành công!", Toast.LENGTH_SHORT).show();
                                loadDanhSach();
                            } else {
                                Toast.makeText(MuonTraActivity.this, "Xóa phiếu mượn thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
