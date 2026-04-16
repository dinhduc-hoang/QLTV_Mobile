package com.example.thuvien.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thuvien.R;
import com.example.thuvien.sach.Sach;
import com.example.thuvien.sach.SachQuery;

import java.util.ArrayList;
import java.util.List;

public class UserBookListActivity extends AppCompatActivity {

    private ImageView imgBack;
    private EditText edtSearch;
    private RecyclerView rvBooks;
    private SachQuery sachQuery;
    private UserBookAdapter adapter;
    private List<Sach> listSach = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_list);

        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        rvBooks = findViewById(R.id.rvBooks);

        sachQuery = new SachQuery(this);
        
        imgBack.setOnClickListener(v -> finish());

        setupRecyclerView();
        loadData("");

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupRecyclerView() {
        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserBookAdapter(this, listSach, false);
        rvBooks.setAdapter(adapter);
    }

    private void loadData(String keyword) {
        listSach.clear();
        if (keyword.isEmpty()) {
            listSach.addAll(sachQuery.layDanhSachSach());
        } else {
            listSach.addAll(sachQuery.timKiemSach(keyword));
        }
        adapter.notifyDataSetChanged();
    }
}
