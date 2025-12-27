# Real-Time Logistics & Supply Chain Tracking Analytics Pipeline 🚛📊

**Subject:** Real Time Analytics   

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

###  Prerequisites
* Java JDK 11+
* Apache Kafka-3.9 (Binary)
* PostgreSQL
* Eclipse & Maven
* Python 3.11+
* gcc (use Visual Studio Setup or MSYS2 for installation)

### Read [setup.md](#-/setup.md) for Installation and setup.

 
---
* Apache Dashboard
![Apache Superset Dashboard](/RTA_apache_Dashboard.jpg)



