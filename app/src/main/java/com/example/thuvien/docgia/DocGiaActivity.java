package com.example.thuvien.docgia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thuvien.R;

import java.util.ArrayList;
import java.util.List;

public class DocGiaActivity extends AppCompatActivity {

    EditText edtSearch;
    ImageView btnAdd;
    ListView lvData;

    DocGiaQuery docGiaQuery;

    List<DocGia> listGoc = new ArrayList<>();
    List<DocGia> listHienThi = new ArrayList<>();

    DocGiaAdapter adapter;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docgia);

        edtSearch = findViewById(R.id.edtSearch);
        lvData = findViewById(R.id.lvDocGia);
        btnAdd = findViewById(R.id.btnAdd);
        docGiaQuery = new DocGiaQuery(this);

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new DocGiaAdapter(this, R.layout.item_docgia, listHienThi);
        lvData.setAdapter(adapter);

        loadData();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocGiaActivity.this, AddDocGiaActivity.class));
            }
        });

        registerForContextMenu(lvData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        listGoc.clear();
        listGoc.addAll(docGiaQuery.layDanhSachDocGia());
        filterData(edtSearch.getText().toString());
    }

    private void filterData(String keyword) {
        listHienThi.clear();

        String tuKhoa = keyword;
        if (tuKhoa == null) {
            tuKhoa = "";
        }
        tuKhoa = tuKhoa.trim().toLowerCase();

        for (int i = 0; i < listGoc.size(); i++) {
            DocGia dg = listGoc.get(i);

            String tenDG = dg.getTenDG();
            if (tenDG == null) {
                tenDG = "";
            }

            String maDG = dg.getMaDG();
            if (maDG == null) {
                maDG = "";
            }

            if (tuKhoa.equals("")
                    || tenDG.toLowerCase().contains(tuKhoa)
                    || maDG.toLowerCase().contains(tuKhoa)) {
                listHienThi.add(dg);
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedPosition = info.position;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (selectedPosition < 0 || selectedPosition >= listHienThi.size()) {
            return super.onContextItemSelected(item);
        }

        final DocGia dgEdit = listHienThi.get(selectedPosition);

        if (item.getItemId() == R.id.menu_update) {
            Intent intent = new Intent(DocGiaActivity.this, UpdateDocGiaActivity.class);
            intent.putExtra("maDG", dgEdit.getMaDG());
            intent.putExtra("maKhoa", dgEdit.getMaKhoa());
            intent.putExtra("maLop", dgEdit.getMaLop());
            intent.putExtra("tenDG", dgEdit.getTenDG());
            intent.putExtra("namSinh", dgEdit.getNamSinh());
            intent.putExtra("gioiTinh", dgEdit.getGioiTinh());
            intent.putExtra("diaChi", dgEdit.getDiaChi());
            intent.putExtra("email", dgEdit.getEmail());
            intent.putExtra("sdt", dgEdit.getSdt());
            startActivity(intent);

        } else if (item.getItemId() == R.id.menu_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc xóa Độc giả này?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    xoaDocGia(dgEdit.getMaDG());
                }
            });
            builder.setNegativeButton("Không", null);
            builder.show();
        }

        return super.onContextItemSelected(item);
    }

    private void xoaDocGia(String maDG) {
        if (docGiaQuery.docGiaDangMuonSach(maDG)) {
            Toast.makeText(this, "Không thể xóa! Độc giả đang mượn sách hoặc có thẻ thư viện.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = docGiaQuery.xoaDocGia(maDG);
        if (result) {
            Toast.makeText(this, "Xóa độc giả thành công!", Toast.LENGTH_SHORT).show();
            loadData();
        } else {
            Toast.makeText(this, "Xóa độc giả thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}