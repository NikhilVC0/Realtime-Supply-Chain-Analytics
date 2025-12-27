# Real-Time Logistics & Supply Chain Tracking Analytics Pipeline 🚛📊

**Subject:** Real Time Analytics  
**Course:** MCA (Data Science)  

## 📖 Project Overview
This project implements a **Real-Time Logistics & Supply Chain Tracking Analytics System**. It streams shipment event data, processes it in real-time, stores it for historical analysis, and visualizes key performance metrics like delivery delays, route bottlenecks, and vehicle utilization.

The pipeline demonstrates the integration of **Big Data streaming** (Kafka) with **Relational Databases** (PostgreSQL) and **Business Intelligence** (Superset).

## 🏗️ Architecture Pipeline
1.  **Data Source:** `shipment_input.csv` (Simulates raw event logs).
2.  **Ingestion Layer (Producer):** Java Application reads CSV and publishes JSON events to Apache Kafka.
3.  **Streaming Layer (Kafka):** Topics partitioned for parallel processing.
4.  **Processing & Storage (Consumer):** Java Application consumes events, parses JSON, and writes to PostgreSQL.
5.  **Analytics Layer (Superset):** Dashboards connected to PostgreSQL for real-time visualization.

---

## 🛠️ Tech Stack
* **Language:** Java 11 / 17
* **Streaming:** Apache Kafka & Zookeeper
* **Database:** PostgreSQL 16
* **Visualization:** Apache Superset
* **Build Tool:** Maven
* **IDE:** Eclipse / IntelliJ IDEA

---

## ⚙️ Setup & Installation

### 1. Prerequisites
* Java JDK 11+
* Apache Kafka (Binary)
* PostgreSQL
* Maven

### 2. Database Setup (PostgreSQL)
Create the database and table using the SQL script:
```sql
CREATE DATABASE supply_chain_analytics;

CREATE TABLE shipments (
    shipment_id VARCHAR(50) PRIMARY KEY,
    event_time TIMESTAMP,
    origin_city VARCHAR(100),
    destination_city VARCHAR(100),
    vehicle_id VARCHAR(50),
    route_id VARCHAR(50),
    shipment_status VARCHAR(50),
    delay_minutes INTEGER,
    distance_km DOUBLE PRECISION,
    eta_minutes INTEGER
);
