package com.example.thuvien.nhaxuatban;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

import java.util.List;

public class AddNXBActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTen, edtDiaChi, edtEmail, edtSdt;
    Button btnSave;

    NXBQuery nxbQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nxb);

        imgBack = findViewById(R.id.imgBack);
        edtTen = findViewById(R.id.edtTen);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtEmail = findViewById(R.id.edtEmail);
        edtSdt = findViewById(R.id.edtSdt);
        btnSave = findViewById(R.id.btnSave);

        nxbQuery = new NXBQuery(this);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void save() {
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

        List<NXB> list = nxbQuery.layDanhSachNXB();

        for (NXB nxb : list) {

            if (nxb.getTenNXB().equalsIgnoreCase(ten)) {
                edtTen.setError("Tên NXB đã tồn tại");
                edtTen.requestFocus();
                return;
            }

            if (nxb.getEmail().equalsIgnoreCase(email)) {
                edtEmail.setError("Email đã tồn tại");
                edtEmail.requestFocus();
                return;
            }

            if (nxb.getSdt().equals(sdt)) {
                edtSdt.setError("SĐT đã tồn tại");
                edtSdt.requestFocus();
                return;
            }
        }
        NXB item = new NXB();
        item.setMaNXB(nxbQuery.taoMaMoi());
        item.setTenNXB(ten);
        item.setDiaChi(diaChi);
        item.setEmail(email);
        item.setSdt(sdt);

        if (nxbQuery.them(item)) {
            Toast.makeText(this, "Thêm nhà xuất bản thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm nhà xuất bản thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}