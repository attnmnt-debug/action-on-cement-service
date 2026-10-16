package org.example.service;

import org.example.exception.OrderReadException;
import org.example.exception.OrderWriteException;
import org.example.model.Order;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.math.RoundingMode;

public class CementOrderService {

    private final BigDecimal basePricePerKg;
    private final BigDecimal startDiscount;
    private final BigDecimal discountStep;

    public CementOrderService(BigDecimal basePricePerKg, BigDecimal discountStep, BigDecimal startDiscount) {

        if(basePricePerKg == null || basePricePerKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("basePricePerKg cannot be zero or negative");
        }
        if(discountStep == null || discountStep.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("discountStep cannot be negative");
        }
        if(startDiscount == null || startDiscount.compareTo(BigDecimal.ZERO) < 0 || startDiscount.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("startDiscount can be in range from 0.0 to 1.0");
        }

        this.basePricePerKg = basePricePerKg;
        this.discountStep = discountStep;
        this.startDiscount = startDiscount;
    }


    public void processAndSaveOrders(List<Order> rawOrders, String outputFilePath) {

        List<Order> sortedOrders = rawOrders.stream()
                .sorted(Comparator.comparing(order -> order.getOrderDate()))
                .toList();

        Map<String, BigDecimal> companyCosts = new HashMap<>();

        BigDecimal currentDiscount = this.startDiscount;

        for (Order order : sortedOrders) {

            BigDecimal count = BigDecimal.valueOf(order.getCementCount());

            BigDecimal discountMultiplier = BigDecimal.ONE.subtract(currentDiscount);

            BigDecimal orderCost = count.multiply(this.basePricePerKg).multiply(discountMultiplier);


            orderCost = orderCost.setScale(2, RoundingMode.HALF_UP);

            if (companyCosts.containsKey(order.getCompanyName())) {

                BigDecimal oldCost = companyCosts.get(order.getCompanyName());
                companyCosts.put(order.getCompanyName(), oldCost.add(orderCost));

            } else {

                companyCosts.put(order.getCompanyName(), orderCost);
            }

            currentDiscount = currentDiscount.subtract(this.discountStep);
            if (currentDiscount.compareTo(BigDecimal.ZERO) < 0) {
                currentDiscount = BigDecimal.ZERO;
            }
        }

        List<String> linesToWrite = new ArrayList<>();

        for(String companyName : companyCosts.keySet()) {

            BigDecimal totalCost = companyCosts.get(companyName);
            String resultLine = companyName + " - " + totalCost;

            linesToWrite.add(resultLine);
        }

        try {
            Files.write(Paths.get(outputFilePath), linesToWrite);
        } catch (IOException e) {
            throw new OrderWriteException("Error writing output file: " + outputFilePath, e);
        }
    }
}