package com.example.thuvien.nhanvien;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class NhanVienActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageView btnAdd;
    ListView lvData;

    NhanVienQuery nhanVienQuery;

    List<NhanVien> listGoc = new ArrayList<>();

    List<NhanVien> listHienThi = new ArrayList<>();

    NhanVienAdapter adapter;

    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhanvien);

        edtSearch = findViewById(R.id.edtSearch);
        btnAdd = findViewById(R.id.btnAdd);
        lvData = findViewById(R.id.lvNhanVien);
        nhanVienQuery = new NhanVienQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new NhanVienAdapter(this, R.layout.item_nhanvien, listHienThi);
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
                Intent intent = new Intent(NhanVienActivity.this, AddNhanVienActivity.class);
                startActivity(intent);
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
            listGoc.addAll(nhanVienQuery.layDanhSachNhanVien());
        } else {
            listGoc.addAll(nhanVienQuery.timKiemNhanVien(keyword));
        }

        listHienThi.clear();
        listHienThi.addAll(listGoc);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void filterData(String keyword) {
        loadData();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedPosition = info.position;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (selectedPosition < 0 || selectedPosition >= listHienThi.size()) {
            return super.onContextItemSelected(item);
        }

        NhanVien nvEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(NhanVienActivity.this, UpdateNhanVienActivity.class);

            intent.putExtra("maNV", nvEdit.getMaNV());
            intent.putExtra("tenNV", nvEdit.getTenNV());
            intent.putExtra("queQuan", nvEdit.getQueQuan());
            intent.putExtra("gioiTinh", nvEdit.getGioiTinh());
            intent.putExtra("namSinh", nvEdit.getNamSinh());
            intent.putExtra("vaiTro", nvEdit.getVaiTro());
            intent.putExtra("email", nvEdit.getEmail());
            intent.putExtra("sdt", nvEdit.getSdt());
            intent.putExtra("user", nvEdit.getUser());
            intent.putExtra("pass", nvEdit.getPass());

            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc xóa Nhân viên này?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean kq = nhanVienQuery.xoaNhanVien(nvEdit.getMaNV());
                    if (kq == true) {
                        Toast.makeText(NhanVienActivity.this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(NhanVienActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Không", null);
            builder.show();
        }

        return true;
    }
}
