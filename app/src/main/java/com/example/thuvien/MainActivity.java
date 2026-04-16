package com.example.thuvien;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.login.LoginActivity;
import com.example.thuvien.trangchu.TrangChuActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
        String maNV = sp.getString("MaNV", null);

        if (maNV != null) {
            Intent intent = new Intent(MainActivity.this, TrangChuActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }
}
