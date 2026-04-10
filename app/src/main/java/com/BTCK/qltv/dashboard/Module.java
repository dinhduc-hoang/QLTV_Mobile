package com.BTCK.qltv.dashboard; // Sửa lại đúng package của bạn

public class Module {
    private String name;
    private int iconId; // ID của file ảnh trong thư mục drawable

    public Module(String name, int iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }
    public int getIconId() {
        return iconId;
    }
}