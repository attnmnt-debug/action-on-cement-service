package org.example.model;

import java.time.LocalDateTime;

public class Order {
    private LocalDateTime orderDate;
    private String companyName;
    private long cementCount;

    public Order(LocalDateTime orderDate, String companyName, long cementCount) {
        this.orderDate = orderDate;
        this.companyName = companyName;
        this.cementCount = cementCount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public long getCementCount() {
        return cementCount;
    }
}