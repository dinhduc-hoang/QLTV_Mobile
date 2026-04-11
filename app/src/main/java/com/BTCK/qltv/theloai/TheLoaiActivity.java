package com.BTCK.qltv.theloai;

import android.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TheLoaiActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageButton btnAdd;
    ListView lvData;

    DatabaseReference database;
    List<TheLoai> listGoc = new ArrayList<>();
    List<TheLoai> listHienThi = new ArrayList<>();

    ArrayAdapter<String> adapter;
    List<String> listHienThiString = new ArrayList<>();

    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_loai); // Đã đổi layout riêng cho Thể Loại

        edtSearch = findViewById(R.id.edtSearch);
        btnAdd = findViewById(R.id.btnAdd);
        lvData = findViewById(R.id.lvData);

        // Trỏ vào bảng "theloai"
        database = FirebaseDatabase.getInstance().getReference("theloai");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listHienThiString);
        lvData.setAdapter(adapter);

        loadData();

        // TÌM KIẾM THEO TÊN
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // NÚT THÊM THỂ LOẠI
        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(TheLoaiActivity.this, AddTheLoaiActivity.class));
        });

        // Đăng ký mở Menu để Sửa/Xóa
        registerForContextMenu(lvData);
    }

    private void loadData() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listGoc.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    TheLoai tl = data.getValue(TheLoai.class);
                    if (tl != null) {
                        tl.id = data.getKey();
                        listGoc.add(tl);
                    }
                }
                filterData("");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TheLoaiActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterData(String keyword) {
        listHienThi.clear();
        listHienThiString.clear();
        for (TheLoai tl : listGoc) {
            if (tl.tenTL != null && (keyword.isEmpty() || tl.tenTL.toLowerCase().contains(keyword.toLowerCase()))) {
                listHienThi.add(tl);
                // Hiển thị tên Thể loại trên ListView
                listHienThiString.add(tl.tenTL + " (" + tl.maTL + ")");
            }
        }
        adapter.notifyDataSetChanged();
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
        TheLoai tlEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(TheLoaiActivity.this, UpdateTheLoaiActivity.class);
            // Đẩy dữ liệu cũ sang UpdateActivity
            intent.putExtra("id", tlEdit.id);
            intent.putExtra("maTL", tlEdit.maTL);
            intent.putExtra("tenTL", tlEdit.tenTL);
            intent.putExtra("moTa", tlEdit.moTa);
            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc xóa Thể Loại này?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        database.child(tlEdit.id).removeValue();
                        Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Không", null)
                    .show();
        }
        return super.onContextItemSelected(item);
    }
}
