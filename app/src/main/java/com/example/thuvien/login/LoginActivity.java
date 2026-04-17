package com.example.thuvien.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.docgia.DocGia;
import com.example.thuvien.login.TaiKhoanQuery;
import com.example.thuvien.trangchu.TrangChuActivity;
import com.example.thuvien.user.UserHomeActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private TextView tvForgotPassword;
    private Button btnLogin;
    private ImageView imgShowPassword;
    private TaiKhoanQuery taiKhoanQuery;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnLogin = findViewById(R.id.btnLogin);
        imgShowPassword = findViewById(R.id.imgShowPassword);

        taiKhoanQuery = new TaiKhoanQuery(this);

        if (imgShowPassword != null) {
            imgShowPassword.setOnClickListener(v -> {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgShowPassword.setImageResource(R.drawable.view);
                } else {
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgShowPassword.setImageResource(R.drawable.hide);
                }
                edtPassword.setSelection(edtPassword.getText().length());
            });
        }
        if (tvForgotPassword != null) {
            tvForgotPassword.setOnClickListener(v -> {
                startActivity(new Intent(LoginActivity.this, RecoverPasswordActivity.class));
            });
        }


        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            edtUsername.setError(null);
            edtPassword.setError(null);

            boolean hasError = false;
            if (username.isEmpty()) {
                edtUsername.setError("Vui lòng nhập tên đăng nhập hoặc email");
                edtUsername.requestFocus();
                hasError = true;
            }

            if (password.isEmpty()) {
                edtPassword.setError("Vui lòng nhập mật khẩu");
                if (!hasError) {
                    edtPassword.requestFocus();
                }
                hasError = true;
            }

            if (hasError) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            TaiKhoanQuery.UserInfo nhanvien = taiKhoanQuery.dangNhap(username, password);
            if (nhanvien != null) {
                savenhanvienSession(nhanvien);
                Toast.makeText(this, "Đăng nhập quản lý thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, TrangChuActivity.class));
                finish();
                return;
            }

            DocGia docgia = taiKhoanQuery.dangNhapDocGia(username, password);
            if (docgia != null) {
                savedocgiaSession(docgia);
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, UserHomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savedocgiaSession(DocGia docgia) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MaDG", docgia.getMaDG());
        editor.putString("TenDG", docgia.getTenDG());
        editor.putString("Role", "USER");
        editor.apply();
    }

    private void savenhanvienSession(TaiKhoanQuery.UserInfo info) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MaNV", info.getMaNhanVien());
        editor.putString("TenNV", info.getTenNhanVien());
        editor.putString("VaiTro", info.getVaiTro());
        editor.putString("Role", "Nhân Viên");
        editor.apply();
    }
}
