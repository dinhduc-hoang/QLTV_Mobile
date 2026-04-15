package com.example.thuvien.theloai;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

import java.util.List;

public class UpdateTheLoaiActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTenTL;
    Button btnUpdate;

    String maTL;
    TheLoaiQuery theLoaiQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_theloai);

        imgBack = findViewById(R.id.imgBack);
        edtTenTL = findViewById(R.id.edtTenTL);
        btnUpdate = findViewById(R.id.btnUpdate);

        theLoaiQuery = new TheLoaiQuery(this);
        maTL = getIntent().getStringExtra("MaTL");

        loadThongTin();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capNhatTheLoai();
            }
        });
    }

    private void loadThongTin() {
        TheLoai item = theLoaiQuery.layThongTinTheoMa(maTL);
        if (item != null) {
            edtTenTL.setText(item.getTenTL());
        }
    }

    private void capNhatTheLoai() {
        String tenTL = edtTenTL.getText().toString().trim();

        if (tenTL.isEmpty()) {
            edtTenTL.setError("Nhập tên thể loại");
            edtTenTL.requestFocus();
            return;
        }
        List<TheLoai> list = theLoaiQuery.layDanhSachTheLoai();
        for(TheLoai theloai:list)
        {
            if(theloai.getTenTL().equals(tenTL))
            {
                edtTenTL.setError("Tn thể loại đã tồn tại");
                edtTenTL.requestFocus();
                return;
            }
        }

        TheLoai item = new TheLoai();
        item.setMaTL(maTL);
        item.setTenTL(tenTL);

        boolean result = theLoaiQuery.capNhatTheLoai(item);

        if (result) {
            Toast.makeText(this, "Cập nhật thể loại thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật thể loại thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
