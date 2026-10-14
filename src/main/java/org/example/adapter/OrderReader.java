package org.example.adapter;

import org.example.model.Order;


import java.util.List;

public interface OrderReader {
    List<Order> readOrders(String filePath);
}
