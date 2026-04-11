package com.BTCK.qltv.theloai;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateTheLoaiActivity extends AppCompatActivity {

    EditText edtMaTL, edtTenTL, edtMoTa;
    Button btnSaveTheLoai;
    DatabaseReference database;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_the_loai);

        edtMaTL = findViewById(R.id.edtMaTL);
        edtTenTL = findViewById(R.id.edtTenTL);
        edtMoTa = findViewById(R.id.edtMoTa);
        btnSaveTheLoai = findViewById(R.id.btnSaveTheLoai);

        database = FirebaseDatabase.getInstance().getReference("theloai");

        id = getIntent().getStringExtra("id");
        edtMaTL.setText(getIntent().getStringExtra("maTL"));
        edtTenTL.setText(getIntent().getStringExtra("tenTL"));
        edtMoTa.setText(getIntent().getStringExtra("moTa"));

        btnSaveTheLoai.setOnClickListener(v -> {
            String ma = edtMaTL.getText().toString();
            String ten = edtTenTL.getText().toString();
            String mota = edtMoTa.getText().toString();

            if (ma.isEmpty() || ten.isEmpty()) {
                Toast.makeText(this, "Mã và Tên không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            TheLoai tlUpdate = new TheLoai(id, ma, ten, mota);
            database.child(id).setValue(tlUpdate);

            Toast.makeText(this, "Đã cập nhật Thể Loại", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}