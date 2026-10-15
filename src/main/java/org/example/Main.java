package org.example;

import org.example.adapter.HashOrderReader;
import org.example.adapter.OrderReader;
import org.example.adapter.TxtOrderReader;
import org.example.exception.OrderReadException;
import org.example.model.Order;
import org.example.service.CementOrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        CementOrderService cementOrderService = new CementOrderService(
                new BigDecimal("10.0"),
                new BigDecimal("0.05"),
                new BigDecimal("0.50")
        );

        List<OrderReader> readers = List.of(
                new TxtOrderReader(),
                new HashOrderReader()
        );

        try {
            List<Order> allOrders = new ArrayList<>();

            allOrders.addAll(readers.get(0).readOrders("discount_day.txt"));
            allOrders.addAll(readers.get(1).readOrders("discount_day_without_ext"));

            cementOrderService.processAndSaveOrders(allOrders, "result.txt");
            System.out.println("Orders processed successfully");

        } catch (OrderReadException e) {
            System.out.println("Critical error while reading file, file are corrupted or missing : ");
            System.out.println(e.getMessage());
        }
    }
}