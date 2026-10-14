package org.example.service;

import org.example.model.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CementOrderService {

    private double basePricePerKg;
    private double startDiscount;
    private double discountStep;

    public CementOrderService(double basePricePerKg, double discountStep, double startDiscount) {
        this.basePricePerKg = basePricePerKg;
        this.discountStep = discountStep;
        this.startDiscount = startDiscount;
    }


    public void processAndSaveOrders(List<Order> rawOrders, String outputFilePath) {

        List<Order> sortedOrders = rawOrders.stream()
                .sorted(Comparator.comparing(order -> order.getOrderDate()))
                .toList();

        Map<String, Double> companyCosts = new HashMap<>();

        double currentDiscount = this.startDiscount;

        for (Order order : sortedOrders) {

            double orderCost = order.getCementCount() * this.basePricePerKg * (1 - currentDiscount);

            if (companyCosts.containsKey(order.getCompanyName())) {

                double oldCost = companyCosts.get(order.getCompanyName());
                companyCosts.put(order.getCompanyName(), oldCost + orderCost);

            } else {

                companyCosts.put(order.getCompanyName(), orderCost);
            }

            currentDiscount = currentDiscount - this.discountStep;
            if (currentDiscount < 0) {
                currentDiscount = 0;
            }
        }

        List<String> linesToWrite = new ArrayList<>();

        for(String companyName : companyCosts.keySet()) {

            double totalCost = companyCosts.get(companyName);
            String resultLine = companyName + " - " + totalCost;

            linesToWrite.add(resultLine);
        }

        try {
            Files.write(Paths.get(outputFilePath), linesToWrite);
        } catch (IOException e) {
            System.out.println("Error writing output file " + e.getMessage());
        }
    }
}