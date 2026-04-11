package com.BTCK.qltv.theloai;

public class TheLoai {
    public String id;
    public String maTL;
    public String tenTL;
    public String moTa; // Thêm mô tả nếu cần

    public TheLoai() {
        // Firebase yêu cầu hàm tạo rỗng
    }

    public TheLoai(String id, String maTL, String tenTL, String moTa) {
        this.id = id;
        this.maTL = maTL;
        this.tenTL = tenTL;
        this.moTa = moTa;
    }
}