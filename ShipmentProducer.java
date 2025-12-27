package com.logistics;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class ShipmentProducer {
    public static void main(String[] args) {
        String topicName = "shipment_events";
        String csvFile = "shipment_input.csv";

        // 1. Kafka Properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        ObjectMapper mapper = new ObjectMapper();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            String header = br.readLine(); // Skip header
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                
                // 2. Create Map for JSON conversion
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("shipment_id", data[0]);
                map.put("event_time", data[1]);
                map.put("origin_city", data[2]);
                map.put("destination_city", data[3]);
                map.put("vehicle_id", data[4]);
                map.put("route_id", data[5]);
                map.put("shipment_status", data[6]);
                map.put("delay_minutes", Integer.parseInt(data[7]));
                map.put("distance_km", Double.parseDouble(data[8]));
                map.put("eta_minutes", Integer.parseInt(data[9]));

                String jsonValue = mapper.writeValueAsString(map);
                String key = data[0]; // Shipment ID as Key [cite: 64]

                // 3. Publish to Kafka
                ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, jsonValue);
                producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        System.out.println("Sent: " + key + " | Partition: " + metadata.partition());
                    } else {
                        exception.printStackTrace();
                    }
                });

                // 4. Simulate Real-time delay (1 second) 
                Thread.sleep(1000); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}