package com.BTCK.qltv.dashboard; // Đã sửa lỗi xóa dấu chấm thừa

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.BTCK.qltv.dashboard.ModuleAdapter;
import com.BTCK.qltv.R; // R file luôn nằm ở thư mục gốc
import com.BTCK.qltv.login.LoginActivity; // Đã khác thư mục nên bắt buộc phải import
import com.BTCK.qltv.sach.SachActivity;
import com.BTCK.qltv.theloai.TheLoaiActivity;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    TextView tvAppName, tvRole;
    ImageView imgMenu;
    ListView lvModules;
    List<Module> moduleList;
    ModuleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // 1. Ánh xạ giao diện
        tvAppName = findViewById(R.id.tvAppName);
        tvRole = findViewById(R.id.tvRole);
        imgMenu = findViewById(R.id.imgMenu);
        lvModules = findViewById(R.id.lvModules);

        // 2. Lấy thông tin người dùng từ SharedPreferences
        loadUserProfile();

        // 3. Setup ListView các chức năng
        setupListView();

        // 4. Bắt sự kiện click vào Menu 3 gạch (Để Đăng xuất)
        imgMenu.setOnClickListener(v -> showPopupMenu());
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        // Lấy tên và vai trò đã lưu lúc đăng nhập (mặc định là Rỗng nếu không thấy)
        String tenNV = prefs.getString("TenNV", "Người dùng");
        String vaiTro = prefs.getString("VaiTro", "Chưa xác định");

        tvAppName.setText("Xin chào, " + tenNV);
        tvRole.setText("Vai trò: " + vaiTro);
    }

    private void setupListView() {
        moduleList = new ArrayList<>();
        // Nhớ đảm bảo bạn có các icon này trong thư mục drawable
        moduleList.add(new Module("Quản lý sách", R.drawable.ic_book));
        moduleList.add(new Module("Quản lý thể loại", R.drawable.ic_category));
        moduleList.add(new Module("Quản lý tác giả", R.drawable.ic_author));
        moduleList.add(new Module("Quản lý nhà xuất bản", R.drawable.ic_publisher));
        moduleList.add(new Module("Quản lý độc giả", R.drawable.ic_reader));
        moduleList.add(new Module("Quản lý kệ sách", R.drawable.ic_bookshelf));
        moduleList.add(new Module("Quản lý ngôn ngữ", R.drawable.ic_language));
        moduleList.add(new Module("Quản lý mượn - trả sách", R.drawable.ic_borrow_return));

        adapter = new ModuleAdapter(this, moduleList);
        lvModules.setAdapter(adapter);

        // Bắt sự kiện khi click vào từng dòng trong ListView
// Bắt sự kiện khi click vào từng dòng trong ListView
        lvModules.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0: // Quản lý sách (Đã làm xong)
                    startActivity(new Intent(DashboardActivity.this, SachActivity.class));
                    break;
                case 1: // Quản lý thể loại (Đã làm xong)
                    startActivity(new Intent(DashboardActivity.this, TheLoaiActivity.class));
                    break;

                // --- KHI NÀO TẠO XONG CÁC TRANG KHÁC THÌ BẠN BỔ SUNG VÀO ĐÂY ---
                // case 2: // Quản lý tác giả
                //     startActivity(new Intent(DashboardActivity.this, TacGiaActivity.class));
                //     break;
                // case 3: // Quản lý nhà xuất bản
                //     startActivity(new Intent(DashboardActivity.this, NXBActivity.class));
                //     break;

                default:
                    Toast.makeText(this, "Chức năng đang phát triển!", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, imgMenu);
        popupMenu.getMenu().add("Đăng xuất");
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Đăng xuất")) {
                // Xóa phiên đăng nhập
                SharedPreferences prefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                prefs.edit().clear().apply();

                // Quay lại trang Login
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish(); // Đóng trang Dashboard
            }
            return true;
        });
        popupMenu.show();
    }
}