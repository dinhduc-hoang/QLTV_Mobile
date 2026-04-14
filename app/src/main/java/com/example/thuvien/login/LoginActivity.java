package com.example.thuvien.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.trangchu.TrangChuActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView tvForgotPassword;
    TaiKhoanQuery taiKhoanQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        taiKhoanQuery = new TaiKhoanQuery(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (username.isEmpty()) {
                    edtUsername.setError("Vui lòng nhập tên đăng nhập");
                    edtUsername.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    edtPassword.setError("Vui lòng nhập mật khẩu");
                    edtPassword.requestFocus();
                    return;
                }

                performLogin(username, password);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void performLogin(String username, String password) {
        TaiKhoanQuery.UserInfo userInfo = taiKhoanQuery.dangNhap(username, password);

        if (userInfo == null) {
            Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        saveSession(userInfo.getMaNhanVien(), userInfo.getTenNhanVien(), userInfo.getVaiTro());

        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, TrangChuActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveSession(String maNV, String ten, String vaiTro) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MaNV", maNV);
        editor.putString("TenNV", ten);
        editor.putString("VaiTro", vaiTro);
        editor.apply();
    }
}