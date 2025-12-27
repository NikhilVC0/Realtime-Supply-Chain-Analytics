package com.logistics;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ShipmentConsumer {
    public static void main(String[] args) {
        String topicName = "shipment_events";
        String groupId = "supply-chain-group";

        // 1. Kafka Properties
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topicName));
        ObjectMapper mapper = new ObjectMapper();

        // 2. Database Connection
        String url = "jdbc:postgresql://localhost:5432/supply_chain_analytics";
        String user = "postgres"; 
        String password = "Pass@123"; // TODO: CHANGE THIS

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to Database...");
            
            String sql = "INSERT INTO shipments (shipment_id, event_time, origin_city, destination_city, " +
                         "vehicle_id, route_id, shipment_status, delay_minutes, distance_km, eta_minutes) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                         "ON CONFLICT (shipment_id) DO NOTHING"; // Prevent duplicates

            PreparedStatement pstmt = conn.prepareStatement(sql);

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(java.time.Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    JsonNode node = mapper.readTree(record.value());

                    // 3. Parse and Set SQL Parameters
                    pstmt.setString(1, node.get("shipment_id").asText());
                    
                    // Parse ISO Timestamp
                    String timeStr = node.get("event_time").asText();
                    LocalDateTime ldt = LocalDateTime.parse(timeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    pstmt.setTimestamp(2, Timestamp.valueOf(ldt));

                    pstmt.setString(3, node.get("origin_city").asText());
                    pstmt.setString(4, node.get("destination_city").asText());
                    pstmt.setString(5, node.get("vehicle_id").asText());
                    pstmt.setString(6, node.get("route_id").asText());
                    pstmt.setString(7, node.get("shipment_status").asText());
                    pstmt.setInt(8, node.get("delay_minutes").asInt());
                    pstmt.setDouble(9, node.get("distance_km").asDouble());
                    pstmt.setInt(10, node.get("eta_minutes").asInt());

                    pstmt.executeUpdate();
                    System.out.println("Inserted: " + node.get("shipment_id").asText());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}