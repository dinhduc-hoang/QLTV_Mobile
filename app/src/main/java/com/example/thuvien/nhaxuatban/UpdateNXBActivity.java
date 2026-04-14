package com.example.thuvien.nhaxuatban;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

public class UpdateNXBActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTen, edtDiaChi, edtEmail, edtSdt;
    Button btnUpdate;

    String maNXB;
    NXBQuery nxbQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nxb);

        imgBack = findViewById(R.id.imgBack);
        edtTen = findViewById(R.id.edtTen);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtEmail = findViewById(R.id.edtEmail);
        edtSdt = findViewById(R.id.edtSdt);
        btnUpdate = findViewById(R.id.btnUpdate);

        nxbQuery = new NXBQuery(this);
        maNXB = getIntent().getStringExtra("MaNXB");

        loadData();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    private void loadData() {
        NXB item = nxbQuery.layTheoMa(maNXB);

        if (item != null) {
            edtTen.setText(item.getTenNXB());
            edtDiaChi.setText(item.getDiaChi());
            edtEmail.setText(item.getEmail());
            edtSdt.setText(item.getSdt());
        }
    }

    private void update() {
        String ten = edtTen.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String sdt = edtSdt.getText().toString().trim();

        if (ten.isEmpty()) {
            edtTen.setError("Nhập tên nhà xuất bản");
            edtTen.requestFocus();
            return;
        }

        if (diaChi.isEmpty()) {
            edtDiaChi.setError("Nhập địa chỉ");
            edtDiaChi.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtEmail.setError("Nhập email");
            edtEmail.requestFocus();
            return;
        }

        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            edtEmail.setError("Email không đúng định dạng");
            edtEmail.requestFocus();
            return;
        }

        if (sdt.isEmpty()) {
            edtSdt.setError("Nhập số điện thoại");
            edtSdt.requestFocus();
            return;
        }

        NXB item = new NXB();
        item.setMaNXB(maNXB);
        item.setTenNXB(ten);
        item.setDiaChi(diaChi);
        item.setEmail(email);
        item.setSdt(sdt);

        if (nxbQuery.capNhat(item)) {
            Toast.makeText(this, "Cập nhật nhà xuất bản thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật nhà xuất bản thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}