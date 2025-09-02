package com.jong.h2_db.model;

import lombok.Getter;

@Getter
public enum ProductStatus {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    DISCONTINUED("단종");

    private final String displayName;

    ProductStatus(String displayName) {
        this.displayName = displayName;
    }
}