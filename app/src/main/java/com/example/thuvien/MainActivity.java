package com.example.thuvien;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.database.SQLiteHelper;
import com.example.thuvien.login.LoginActivity;
import com.example.thuvien.trangchu.TrangChuActivity;

public class MainActivity extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
