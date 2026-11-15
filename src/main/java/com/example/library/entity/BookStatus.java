package com.example.library.entity;

/**
 * 書籍の状態
 */
public enum BookStatus {
    AVAILABLE("貸出可"),
    LENT("貸出中");

    private final String label;

    BookStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
