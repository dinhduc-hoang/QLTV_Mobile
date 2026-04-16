package com.example.thuvien.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.docgia.DocGia;
import com.example.thuvien.docgia.DocGiaQuery;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TextView tvProfileName, tvStudentId, tvEmail, tvPhone, tvDepartment;
    private Button btnLogout;
    private DocGiaQuery docGiaQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initViews();
        docGiaQuery = new DocGiaQuery(this);

        loadUserData();

        imgBack.setOnClickListener(v -> finish());

        btnLogout.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this, com.example.thuvien.login.LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void initViews() {
        imgBack = findViewById(R.id.imgBack);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvStudentId = findViewById(R.id.tvStudentId);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvDepartment = findViewById(R.id.tvDepartment);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void loadUserData() {
        SharedPreferences sp = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String maDG = sp.getString("MaDG", null);

        if (maDG != null) {
            DocGia docGia = docGiaQuery.layThongTinTheoMa(maDG);
            if (docGia != null) {
                tvProfileName.setText(docGia.getTenDG());
                tvStudentId.setText(docGia.getMaDG());
                tvEmail.setText(docGia.getEmail());
                tvPhone.setText(docGia.getSdt());
                tvDepartment.setText(docGia.getTenKhoa() != null ? docGia.getTenKhoa() : "Chưa cập nhật");
            }
        }
    }
}
