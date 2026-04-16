package com.example.thuvien.ngonngu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

import java.util.List;

public class AddNgonNguActivity extends AppCompatActivity {

    ImageView imgBack;
    EditText edtTenNN;
    Button btnSave;

    NgonNguQuery ngonNguQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ngonngu);

        imgBack = findViewById(R.id.imgBack);
        edtTenNN = findViewById(R.id.edtTenNN);
        btnSave = findViewById(R.id.btnSave);

        ngonNguQuery = new NgonNguQuery(this);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luuNgonNgu();
            }
        });
    }
    private void luuNgonNgu() {
        String tenNN = edtTenNN.getText().toString().trim();

        if (tenNN.isEmpty()) {
            edtTenNN.setError("Nhập tên ngôn ngữ");
            edtTenNN.requestFocus();
            return;
        }

        if (tenNN.length() < 2 || tenNN.length() > 50) {
            edtTenNN.setError("Tên ngôn ngữ phải từ 2-50 ký tự");
            edtTenNN.requestFocus();
            return;
        }

        if (!tenNN.matches("[\\p{L}0-9\\s.&-]+")) {
            edtTenNN.setError("Tên ngôn ngữ chứa ký tự không hợp lệ");
            edtTenNN.requestFocus();
            return;
        }

        List<NgonNgu> list = ngonNguQuery.layDanhSachNgonNgu();

        for (NgonNgu nn : list) {
            if (nn.getTenNN() != null && nn.getTenNN().equalsIgnoreCase(tenNN)) {
                edtTenNN.setError("Tên ngôn ngữ đã tồn tại");
                edtTenNN.requestFocus();
                return;
            }
        }

        NgonNgu item = new NgonNgu();
        item.setMaNN(ngonNguQuery.taoMaMoi());
        item.setTenNN(tenNN);

        boolean result = ngonNguQuery.themNgonNgu(item);

        if (result) {
            Toast.makeText(this, "Thêm ngôn ngữ thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Thêm ngôn ngữ thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
