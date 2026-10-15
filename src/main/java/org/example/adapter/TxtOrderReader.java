package org.example.adapter;

import org.example.exception.OrderReadException;
import org.example.model.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Stream;

public class TxtOrderReader implements OrderReader {


    @Override
    public List<Order> readOrders(String filePath) {

        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.map(line -> parseLine(line))
                    .filter(order -> order != null)
                    .toList();
        } catch (IOException e) {
            throw new OrderReadException("Critical error while reading file: " + filePath, e);

        }
    }
    private Order parseLine(String line) {
        if (line == null || line.trim().isEmpty()) return null;

        String[] parts = line.split("\\|");
        if (parts.length < 3) {
            System.out.println("Error: no data in line " + line);
            return null;
        }

        try {
            LocalDateTime date = LocalDateTime.parse(parts[0].trim());
            String company = parts[1].trim();
            long count = Long.parseLong(parts[2].trim());
            return new Order(date, company, count);
        } catch (DateTimeParseException | NumberFormatException e) {
            System.out.println("Error: while parsing line " + line + "Reason: " + e.getMessage());
            return null;
        }

    }
}