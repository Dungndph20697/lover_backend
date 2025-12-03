package com.codegym.model.enums;

public enum ProfileStatus {
    ACTIVE("Sẵn sàng cung cấp dịch vụ"),
    INACTIVE("Tạm ngưng cung cấp dịch vụ");

    private final String description;

    ProfileStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
