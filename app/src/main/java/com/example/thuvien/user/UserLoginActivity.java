package com.example.thuvien.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;
import com.example.thuvien.docgia.DocGia;
import com.example.thuvien.docgia.DocGiaQuery;
import com.example.thuvien.login.TaiKhoanQuery;
import com.example.thuvien.trangchu.TrangChuActivity;

public class UserLoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private ImageView imgShowPassword;
    private DocGiaQuery docGiaQuery;
    private TaiKhoanQuery taiKhoanQuery;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        imgShowPassword = findViewById(R.id.imgShowPassword);

        docGiaQuery = new DocGiaQuery(this);
        taiKhoanQuery = new TaiKhoanQuery(this);

        imgShowPassword.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imgShowPassword.setImageResource(R.drawable.ic_book); // Use a different icon if available
            } else {
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imgShowPassword.setImageResource(R.drawable.ic_book_blue);
            }
            edtPassword.setSelection(edtPassword.getText().length());
        });

        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Kiểm tra quyền Admin/NhanVien trước
            TaiKhoanQuery.UserInfo staffInfo = taiKhoanQuery.dangNhap(username, password);
            if (staffInfo != null) {
                saveStaffSession(staffInfo);
                Toast.makeText(this, "Đăng nhập Admin thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, TrangChuActivity.class));
                finish();
                return;
            }

            // 2. Kiểm tra quyền User/DocGia
            DocGia docGia = docGiaQuery.dangNhap(username, password);
            if (docGia != null) {
                saveUserSession(docGia);
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, UserHomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserSession(DocGia docGia) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MaDG", docGia.getMaDG());
        editor.putString("TenDG", docGia.getTenDG());
        editor.putString("Role", "USER");
        editor.apply();
    }

    private void saveStaffSession(TaiKhoanQuery.UserInfo info) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MaNV", info.getMaNhanVien());
        editor.putString("TenNV", info.getTenNhanVien());
        editor.putString("VaiTro", info.getVaiTro());
        editor.putString("Role", "ADMIN");
        editor.apply();
    }
}
