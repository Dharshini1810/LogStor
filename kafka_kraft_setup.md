Topics in Kafka:
	Event-driven Architecture
	Clusters
	Kafka Brokers
	Topics
	Partitioning
	Replication
	Producers
	Consumers
	Zookeeper
	KRaft

Creating Topics with partitioning and replication
Producers producing message to Kafka
Consumers consuming messages from Kafka


# Apache Kafka (KRaft Mode) Setup and Concepts

## 🔹 Kafka Installation on Windows (KRaft Mode)

### 1. Extract Kafka

-   Download Kafka from the [official
    website](https://kafka.apache.org/downloads).

-   Extract it. You'll see folders like:

        bin/
        config/
        libs/
        logs/

-   `bin\windows\` contains `.bat` scripts for Windows.

------------------------------------------------------------------------

### 2. Generate a Cluster ID

Kafka clusters need a unique identifier.

``` bat
bin\windows\kafka-storage.bat random-uuid
```

-   This generates a **UUID** (example: `xC2s4X1ET8u2xM0rXb2bFg`).
-   Copy it for the next step.

📌 **Purpose**: Cluster ID is the identity of your Kafka cluster.

------------------------------------------------------------------------

### 3. Format Storage Directory

Format Kafka's log directory with your Cluster ID:

``` bat
bin\windows\kafka-storage.bat format -t <UUID> -c config\kraft\server.properties
```

📌 **Purpose**:\
- Writes the Cluster ID into the log directories.\
- Sets up Kafka's **metadata log** (like formatting a hard disk).

------------------------------------------------------------------------

### 4. Start the Kafka Broker

Start the Kafka server in **KRaft mode**:

``` bat
bin\windows\kafka-server-start.bat config\kraft\server.properties
```

📌 **What Happens**:\
- Kafka starts a **single-node cluster**.\
- Acts as both **controller** (metadata manager) and **broker** (message
storage).

------------------------------------------------------------------------

### 5. Create a Topic

``` bat
bin\windows\kafka-topics.bat --create --topic test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

📌 **Purpose**: Creates a topic named `test-topic` with 1 partition and
replication factor 1.

------------------------------------------------------------------------

### 6. Produce Messages

``` bat
bin\windows\kafka-console-producer.bat --topic test-topic --bootstrap-server localhost:9092
```

Type:

    Hello Kafka
    This is my first message

📌 **Purpose**: Producers send messages into the topic.

------------------------------------------------------------------------

### 7. Consume Messages

``` bat
bin\windows\kafka-console-consumer.bat --topic test-topic --from-beginning --bootstrap-server localhost:9092
```

📌 **Purpose**: Consumers read messages from the topic.

------------------------------------------------------------------------

## 🔹 What is KRaft Mode?

**KRaft** = **Kafka Raft Metadata mode**.\
It's the new architecture in Kafka (introduced in 2.8, production-ready
from 3.3) that removes ZooKeeper.

### Before KRaft

-   Kafka used **ZooKeeper** for:
    -   Cluster metadata storage
    -   Controller election
    -   Coordination between brokers

### Problems with ZooKeeper

-   Added complexity (extra service to run)\
-   Harder to scale\
-   Kafka + ZooKeeper sync issues

### With KRaft

-   Kafka brokers manage metadata themselves.\
-   Uses **Raft consensus algorithm** for agreement on metadata.\
-   One broker acts as **controller leader**, others as **followers**.\
-   Metadata is replicated for fault tolerance.

📌 **In short**: Kafka is now **self-contained**, no external ZooKeeper
needed.

------------------------------------------------------------------------

## 🔹 Purpose of Formatting in KRaft

When you run `kafka-storage format`: 1. Creates a **meta.properties**
file in `log.dirs`.\
- Contains `cluster.id`, `node.id`.\
2. Initializes the **@metadata log**.\
- Special Raft log dedicated only for cluster metadata.\
- Stores topic configs, ACLs, partition counts, etc.

📌 **Think of it like formatting a new ledger for the Kafka cluster.**

------------------------------------------------------------------------

## 🔹 Multiple Clusters on Same Machine

You can run multiple Kafka clusters on the same machine.

### Steps

1.  Duplicate `server.properties` → `server1.properties`,
    `server2.properties`.\
2.  Change:
    -   `node.id` (unique per broker)
    -   `listeners` (different ports, e.g., 9092, 9093)
    -   `log.dirs` (separate directories)\
3.  Generate separate cluster IDs.\
4.  Format each `log.dirs` with its respective cluster ID.\
5.  Start brokers separately.

📌 Result: Two isolated clusters running independently.

------------------------------------------------------------------------

## 🔹 Logs in KRaft Mode

### Example Directory Structure

    C:/tmp/kraft-cluster1-logs/
    │
    ├── meta.properties
    │   └── contains cluster.id, node.id, version info
    │
    ├── @metadata-0
    │   ├── 00000000000000000000.log
    │   └── ...
    │   (special Raft log storing cluster metadata)
    │
    ├── test-topic-0
    │   ├── 00000000000000000000.log
    │   └── ...
    │   (partition 0 for topic "test-topic")
    │
    ├── orders-0
    │   └── ...
    │   (partition 0 for topic "orders")
    │
    └── __consumer_offsets-23
        └── ...
        (internal topic for tracking consumer group offsets)

### Breakdown

-   **meta.properties** → Ties log directory to a specific cluster ID.\
-   **@metadata** → Dedicated Raft log for metadata.\
-   **Topic partitions** → Store actual messages.\
-   **Internal topics** → Kafka's internal bookkeeping.

------------------------------------------------------------------------

## 🔹 ZooKeeper vs KRaft

  ------------------------------------------------------------------------
  Feature            ZooKeeper Mode (Legacy)  KRaft Mode (New)
  ------------------ ------------------------ ----------------------------
  Metadata storage   ZooKeeper external       Kafka's internal @metadata
                     service                  log

  Consensus          Zab (ZooKeeper Atomic    Raft algorithm
  algorithm          Broadcast)               

  Deployment         Needs ZooKeeper + Kafka  Only Kafka

  Complexity         Higher (two systems)     Lower (self-contained)

  Scalability        Limited by ZooKeeper     Better with Raft

  Future Support     Deprecated (till Kafka   Default moving forward
                     4.0)                     
  ------------------------------------------------------------------------

------------------------------------------------------------------------

# ✅ Summary

-   **KRaft mode** replaces ZooKeeper with Kafka's own Raft-based
    metadata log.\
-   **Formatting** prepares the log directories with cluster ID and
    metadata log.\
-   **Cluster ID + log.dirs** ensure clusters are isolated.\
-   Logs contain both **metadata (@metadata)** and **data (topic
    partitions)**.\
-   KRaft makes Kafka simpler, faster, and self-contained.


Controller Quorum = the group of brokers that agree on the cluster metadata using Raft.




# Start a Kafka server/broker

-	generate a unique id representing the kafka broker to be created

	bin\kafka-storage.sh random-uuid
	
-	format the broker meta-data as now kafka is maintained by Kraft, so it stores all the metadata in the controller Quorum[The main server in which broker is started(multiple brokers can also be started in same machine too forming a cluster)]
		
		bin\windows\kafka-storage.bat format -t 7aACc2NgT2OBXC9xwHn7Nw -c config\server.properties

Error msg: 2025-09-20T16:42:40.794099400Z main ERROR Reconfiguration failed: No configuration found for '2c7b84de' at 'null' in 'null'
Because controller.quorum.voters is not set on this controller, you must specify one of the following: --standalone, --initial-controllers, or --no-initial-controllers.

The above error specifies the type of broker it is running, it it standalone, like no other controller server, so this broker now which will be running will be controller

-	Start the Server
	
	bin\windows\kafka-server-start.bat config\server.properties
	
- Check whether kafka is running
	
	jps

op:	
9652 Eclipse
78356 XMLServerLauncher
808
79992 Jps
82892 Kafka

- 	Create a topic with partition and replication factor
	
	.\bin\windows\kafka-topics.bat --create --topic logstor --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
	
2025-09-20T17:38:50.919427200Z main ERROR Reconfiguration failed: No configuration found for '2c7b84de' at 'null' in 'null'
Created topic logstor.

- 	List topics


-	Produce message to broker
	
	.\bin\windows\kafka-console-producer.bat --bootstrap-server localhost:9092 --topic logstor

2025-09-21T06:18:52.748004Z main ERROR Reconfiguration failed: No configuration found for '2c7b84de' at 'null' in 'null'
>Pushing log into logstor topic
>Message2 push from logstor

-	Consume message from broker	
	
	 .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic logstor --from-beginning
	 
2025-09-21T06:24:34.128431200Z main ERROR Reconfiguration failed: No configuration found for '2c7b84de' at 'null' in 'null'
Pushing log into logstor topic
Message2 push from logstor

