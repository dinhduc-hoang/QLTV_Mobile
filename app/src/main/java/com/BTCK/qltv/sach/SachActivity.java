package com.BTCK.qltv.sach;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class SachActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageButton btnAdd;
    ListView lvData;

    DatabaseReference mDatabase;
    List<Sach> listGoc = new ArrayList<>();
    List<Sach> listHienThi = new ArrayList<>();

    // Tạm dùng ArrayAdapter hiển thị Tên Sách + Số lượng
    ArrayAdapter<String> adapter;
    List<String> listHienThiString = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management); // Tái sử dụng layout chung

        edtSearch = findViewById(R.id.edtSearch);
        btnAdd = findViewById(R.id.btnAdd);
        lvData = findViewById(R.id.lvData);

        // Trỏ vào bảng "sach"
        mDatabase = FirebaseDatabase.getInstance().getReference("sach");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listHienThiString);
        lvData.setAdapter(adapter);

        loadData();

        // TÌM KIẾM THEO TÊN SÁCH
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

        // NÚT THÊM SÁCH
        btnAdd.setOnClickListener(v -> showAddEditDialog(null));

        // CLICK VÀO SÁCH ĐỂ SỬA/XÓA
        lvData.setOnItemClickListener((parent, view, position, id) -> {
            Sach selectedSach = listHienThi.get(position);
            showOptionsDialog(selectedSach);
        });
    }

    private void loadData() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listGoc.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Sach sach = data.getValue(Sach.class);
                    if (sach != null) {
                        sach.setId(data.getKey()); // Lấy S001, S002...
                        listGoc.add(sach);
                    }
                }
                filterData("");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SachActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterData(String keyword) {
        listHienThi.clear();
        listHienThiString.clear();
        for (Sach sach : listGoc) {
            if (keyword.isEmpty() || sach.getTenSach().toLowerCase().contains(keyword.toLowerCase())) {
                listHienThi.add(sach);
                // Hiển thị tên sách và số lượng trên ListView
                listHienThiString.add(sach.getTenSach() + " (SL: " + sach.getSoLuong() + ")");
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showOptionsDialog(Sach sach) {
        String[] options = {"Sửa thông tin", "Xóa sách"};
        new AlertDialog.Builder(this)
                .setTitle("Tùy chọn: " + sach.getTenSach())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) showAddEditDialog(sach); // Sửa
                    else deleteSach(sach); // Xóa
                }).show();
    }

    private void deleteSach(Sach sach) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa sách")
                .setMessage("Chắc chắn xóa cuốn '" + sach.getTenSach() + "'?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    mDatabase.child(sach.getId()).removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", null).show();
    }

    // HÀM CHUNG DÙNG CHO CẢ THÊM VÀ SỬA (Tiết kiệm code)
    private void showAddEditDialog(Sach sachToEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(sachToEdit == null ? "Thêm Sách Mới" : "Sửa Sách");

        // Dựng Layout nhập liệu bằng Java
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        EditText edtTen = new EditText(this); edtTen.setHint("Tên sách");
        EditText edtSoLuong = new EditText(this); edtSoLuong.setHint("Số lượng (VD: 20)"); edtSoLuong.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        EditText edtNamXB = new EditText(this); edtNamXB.setHint("Năm XB (VD: 2021)"); edtNamXB.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        // Khóa ngoại (Mức cơ bản: Nhập mã. Mức nâng cao: Dùng Spinner)
        EditText edtMaTL = new EditText(this); edtMaTL.setHint("Mã Thể Loại (VD: TL006)");
        EditText edtMaTG = new EditText(this); edtMaTG.setHint("Mã Tác Giả (VD: TG005)");
        EditText edtMaNXB = new EditText(this); edtMaNXB.setHint("Mã NXB (VD: NXB001)");
        EditText edtMaNN = new EditText(this); edtMaNN.setHint("Mã Ngôn Ngữ (VD: NN001)");
        EditText edtMaViTri = new EditText(this); edtMaViTri.setHint("Mã Vị Trí Kệ (VD: KS001)");

        layout.addView(edtTen); layout.addView(edtSoLuong); layout.addView(edtNamXB);
        layout.addView(edtMaTL); layout.addView(edtMaTG); layout.addView(edtMaNXB);
        layout.addView(edtMaNN); layout.addView(edtMaViTri);
        builder.setView(layout);

        // Nếu là "Sửa", đổ dữ liệu cũ vào các ô
        if (sachToEdit != null) {
            edtTen.setText(sachToEdit.getTenSach());
            edtSoLuong.setText(String.valueOf(sachToEdit.getSoLuong()));
            edtNamXB.setText(String.valueOf(sachToEdit.getNamXB()));
            edtMaTL.setText(sachToEdit.getMaTL());
            edtMaTG.setText(sachToEdit.getMaTG());
            edtMaNXB.setText(sachToEdit.getMaNXB());
            edtMaNN.setText(sachToEdit.getMaNN());
            edtMaViTri.setText(sachToEdit.getMaViTri());
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            try {
                String ten = edtTen.getText().toString();
                int soLuong = Integer.parseInt(edtSoLuong.getText().toString());
                int namXB = Integer.parseInt(edtNamXB.getText().toString());
                String maTL = edtMaTL.getText().toString();
                String maTG = edtMaTG.getText().toString();
                String maNXB = edtMaNXB.getText().toString();
                String maNN = edtMaNN.getText().toString();
                String maViTri = edtMaViTri.getText().toString();

                if (ten.isEmpty() || maTL.isEmpty()) {
                    Toast.makeText(this, "Tên sách và Mã TL không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Nếu Thêm mới -> Tạo mã "S..." tự động. Nếu Sửa -> Dùng lại mã cũ
                String bookId = (sachToEdit == null) ? mDatabase.push().getKey() : sachToEdit.getId();

                Sach newSach = new Sach(bookId, maTG, maNXB, maTL, ten, maNN, maViTri, namXB, soLuong);

                mDatabase.child(bookId).setValue(newSach)
                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "Lưu thành công!", Toast.LENGTH_SHORT).show());

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số lượng và Năm XB phải là số!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null).show();
    }
}