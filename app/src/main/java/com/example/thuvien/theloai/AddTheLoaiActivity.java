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

public class AddTheLoaiActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTenTL;
    Button btnSave;

    TheLoaiQuery theLoaiQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_theloai);

        imgBack = findViewById(R.id.imgBack);
        edtTenTL = findViewById(R.id.edtTenTL);
        btnSave = findViewById(R.id.btnSave);

        theLoaiQuery = new TheLoaiQuery(this);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luuTheLoai();
            }
        });
    }

    private void luuTheLoai() {
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
        item.setMaTL(theLoaiQuery.taoMaMoi());
        item.setTenTL(tenTL);

        boolean result = theLoaiQuery.themTheLoai(item);

        if (result) {
            Toast.makeText(this, "Thêm thể loại thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm thể loại thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}