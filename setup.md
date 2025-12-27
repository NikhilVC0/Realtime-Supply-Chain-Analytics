# ⚙️ Setup & Installation Guide

This document provides a comprehensive, step-by-step guide to setting up the **Real-Time Supply Chain Analytics** pipeline on a local machine (Windows).

---

## 📑 Table of Contents

- [Prerequisites](#-prerequisites)
- [Step 1: Database Setup](#-step-1-database-setup-postgresql)
- [Step 2: Apache Kafka Configuration](#-step-2-apache-kafka-configuration)
- [Step 3: Project Setup (Java)](#-step-3-project-setup-java)
- [Step 4: Running the Pipeline](#️-step-4-running-the-pipeline)
- [Step 5: Apache Superset Visualization](#-step-5-apache-superset-visualization)
- [Troubleshooting](#️-troubleshooting)

---

## 📋 Prerequisites

Ensure you have the following installed and configured before starting:

- [x] Java JDK 11 or 17  
- [x] Apache Kafka (Binary version, extracted to `C:\kafka`)  
- [x] PostgreSQL (v14 or higher)  
- [x] Apache Superset (via Python/Pip or Docker)  
- [x] Eclipse IDE (or IntelliJ) with Maven support  

---

## 🚀 Step 1: Database Setup (PostgreSQL)

Before running the code, we need a persistent store for the shipment data.

1. Open **pgAdmin** or your SQL terminal.
2. Create a new database named:

```sql
supply_chain_analytics
```

3. Run the following SQL script:

```sql
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
```

> ℹ️ **Note:** PostgreSQL must be running on port **5432**.

---

## 📡 Step 2: Apache Kafka Configuration

Open **3 separate Command Prompt terminals**.

### Terminal 1 – Zookeeper

```bat
cd C:\kafka
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```

### Terminal 2 – Kafka Broker

```bat
cd C:\kafka
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

### Terminal 3 – Create Topic

```bat
cd C:\kafka\bin\windows
kafka-topics.bat --create --topic shipment_events --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

---

## 💻 Step 3: Project Setup (Java)

### 1. Clone Repository

```bash
git clone https://github.com/NikhilVC0/Realtime-Supply-Chain-Analytics.git
```

### 2. Import into Eclipse

- File → Import  
- Maven → Existing Maven Projects  
- Select cloned folder

### 3. Fix Folder Structure

```text
src/main/java/com/logistics
```

Move inside above path:
- `ShipmentProducer.java`
- `ShipmentConsumer.java`

### 4. Update DB Password

```java
String password = "YOUR_PASSWORD";
```

### 5. Update Maven

- Right-click project → Maven → Update Project

---

## ▶️ Step 4: Running the Pipeline

### 1. Start Consumer

```text
Connected to Database...
Inserted: SHP001
Inserted: SHP002
Inserted: SHP003
```

### 2. Start Producer

```text
Sent: SHP001 | Partition: 0
Sent: SHP002 | Partition: 1
Sent: SHP003 | Partition: 2
```

### 3. Verify Data

```sql
SELECT * FROM shipments;
```

---

## 📊 Step 5: Apache Superset Visualization

### Start Superset

```bash
python -m venv venv
venv\Scripts\activate
pip -m pip install -upgrade pip
pip install apache-superset
set FLASK_APP=superset.app:create_app
superset db upgrade
superset init
superset run -p 8088 --with-threads --reload --debugger
```

### Database URI

```text
Make sure your Postgres connection and apache superset data connect fields are same.
```
```text
OR for sqlite way : postgresql://User_name:YOUR_PASSWORD@localhost:5432/supply_chain_analytics
```
```text
Once DB Connection Successful, create suitable charts and dashboard as per your choice!
```

### For Example : Charts

| Chart Type | Metric               | Group By             |
|-----------|----------------------|----------------------|
| Line      | Average Delay        | event_time, route_id |
| Bar       | Count (*)            | origin_city          |
| Table     | Avg / Max Delay      | route_id             |

---

## 🛠️ Troubleshooting

| Issue | Solution |
|------|----------|
| Kafka connection refused | Start Zookeeper first |
| Database connection | Make sure Db is public and all config matchs with database properties in PgAdmn and Apache Superset 
| Postgres role missing | Verify DB username |
| Serializer error | Add `jackson-databind` |
| Superset DB error | Use `127.0.0.1` or install `psycopg2-binary` |

---

✅ **Setup Complete**
