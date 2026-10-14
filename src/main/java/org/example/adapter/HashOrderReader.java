package org.example.adapter;

import org.example.model.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class HashOrderReader implements OrderReader {

    @Override
    public List<Order> readOrders(String filePath) {

        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.map(line -> {
                        String[] parts = line.split("#");

                        LocalDateTime date = LocalDateTime.parse(parts[0]);
                        String company = parts[1];
                        long count = Long.parseLong(parts[2]);

                        return new Order(date, company, count);
                    })
                    .toList();

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return List.of();
        }
    }

}