package org.example;

import org.example.adapter.HashOrderReader;
import org.example.adapter.TxtOrderReader;
import org.example.model.Order;
import org.example.service.CementOrderService;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        CementOrderService cementOrderService = new CementOrderService(10, 0.05, 0.50);

        TxtOrderReader txtOrderReader = new TxtOrderReader();
        List<Order> txtOrders = txtOrderReader.readOrders("discount_day.txt");

        HashOrderReader hashOrderReader = new HashOrderReader();
        List<Order> hashOrders = hashOrderReader.readOrders("discount_day_without_ext");

        List<Order> allOrders = new ArrayList<>();
        allOrders.addAll(txtOrders);
        allOrders.addAll(hashOrders);

        cementOrderService.processAndSaveOrders(allOrders, "result.txt");
    }
}