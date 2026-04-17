package com.example.thuvien.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

public class RecoverPasswordActivity extends AppCompatActivity {

    EditText edtUser, edtEmail, edtNewPassword;
    Button btnConfirm, btnCancel;
    TaiKhoanQuery taiKhoanQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        edtUser = findViewById(R.id.edtRecoverUser);
        edtEmail = findViewById(R.id.edtRecoverEmail);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnConfirm = findViewById(R.id.btnConfirmRecover);
        btnCancel = findViewById(R.id.btnCancelRecover);

        taiKhoanQuery = new TaiKhoanQuery(this);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUser.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String newPass = edtNewPassword.getText().toString().trim();

                if (user.isEmpty()) {
                    edtUser.setError("Nhập tên đăng nhập");
                    edtUser.requestFocus();
                    return;
                }
                if(user.length() < 4){
                    edtUser.setError("Tên đăng nhập tối thiểu 4 ký tự");
                    edtUser.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    edtEmail.setError("Nhập email");
                    edtEmail.requestFocus();
                    return;
                }

                if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    edtEmail.setError("Email không đúng định dạng");
                    edtEmail.requestFocus();
                    return;
                }

                if (newPass.isEmpty()) {
                    edtNewPassword.setError("Nhập mật khẩu mới");
                    edtNewPassword.requestFocus();
                    return;
                }

                if (newPass.length() < 4) {
                    edtNewPassword.setError("Mật khẩu tối thiểu 4 ký tự");
                    edtNewPassword.requestFocus();
                    return;
                }

                updatePassword(user, email, newPass);
            }
        });
    }

    private void updatePassword(String username, String email, String newPassword) {
        boolean updated = taiKhoanQuery.doiMatKhau(username, email, newPassword);

        if (updated) {
            Toast.makeText(RecoverPasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(RecoverPasswordActivity.this, "Sai tên đăng nhập hoặc email!", Toast.LENGTH_SHORT).show();
        }
    }
}