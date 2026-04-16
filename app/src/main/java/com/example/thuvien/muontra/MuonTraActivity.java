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

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class MuonTraActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageView btnAdd;
    ListView lvData;

    MuonTraQuery muonTraQuery;

    List<MuonTra> listGoc = new ArrayList<>();
    List<MuonTra> listHienThi = new ArrayList<>();

    MuonTraAdapter adapter;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muontra);

        edtSearch = findViewById(R.id.edtSearch);
        lvData = findViewById(R.id.lvMuonTra);
        btnAdd = findViewById(R.id.btnAdd);

        muonTraQuery = new MuonTraQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new MuonTraAdapter(this, R.layout.item_muontra, listHienThi);
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
                startActivity(new Intent(MuonTraActivity.this, AddMuonTraActivity.class));
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
            listGoc.addAll(muonTraQuery.layDanhSachMuonTra());
        } else {
            listGoc.addAll(muonTraQuery.timKiemMuonTra(keyword));
        }
        
        listHienThi.clear();
        listHienThi.addAll(listGoc);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Trả sách");
        menu.add(0, 2, 0, "Sửa");
        menu.add(0, 3, 0, "Xóa");

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedPosition = info.position;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (selectedPosition < 0 || selectedPosition >= listHienThi.size()) {
            return super.onContextItemSelected(item);
        }

        final MuonTra mtEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == 1) {
            if ("Đã trả".equals(mtEdit.getTrangThai())) {
                Toast.makeText(this, "Phiếu này đã được trả rồi!", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Xác nhận trả sách");
                builder.setMessage("Bạn có chắc chắn muốn xác nhận trả sách cho phiếu " + mtEdit.getMaMT() + "?");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean result = muonTraQuery.capNhatTrangThaiDaTra(mtEdit.getMaMT());
                        if (result) {
                            Toast.makeText(MuonTraActivity.this, "Trả sách thành công!", Toast.LENGTH_SHORT).show();
                            loadData();
                        } else {
                            Toast.makeText(MuonTraActivity.this, "Trả sách thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Hủy", null);
                builder.show();
            }

        } else if (item.getItemId() == 2) {
            Intent intent = new Intent(MuonTraActivity.this, UpdateMuonTraActivity.class);
            intent.putExtra("MaMT", mtEdit.getMaMT());
            startActivity(intent);

        } else if (item.getItemId() == 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc xóa Phiếu mượn này?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean result = muonTraQuery.xoaPhieuMuon(mtEdit.getMaMT());
                    if (result) {
                        Toast.makeText(MuonTraActivity.this, "Xóa phiếu mượn thành công!", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(MuonTraActivity.this, "Xóa phiếu mượn thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Không", null);
            builder.show();
        }

        return true;
    }
}
