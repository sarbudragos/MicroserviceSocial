# Microservice Social - A basic social media application using microservices

## 1. Introduction and prerequisites

This is a social media application which is built using microservices. Users can log in, see the posts of other users 
and create their own posts. This application uses a Java Spring backend and a React frontend using a microfrontend architecture.

## 2. Installation

### 1. Clone this repository
```bash
git clone *repo name*
```
### 2. Go into the project repository
```bash
cd MicroserviceSocial
```
### 3. Go into the docker compose file and fill in any missing environment variables
### 4. Potential db initialization issue
if the db does not initialize correctly, run the V1 migration from the user service manually.
### 5. Run docker compose
```bash
docker compose up -d 
```


### 4. Connect on localhost:3000 or localhost:8000 for the api

## 3. Diagrams

### 1. C4 Architecture diagram

```mermaid
C4Container
title Architecture diagram

    Person(user, "User", "Web Browser")

    Container_Boundary(frontend, "Micro-Frontend Layer") {
        Container(host, "Host", "React", "Main layout, Authentication.")
        Container(dashboard, "Dashboard", "React", "Loaded via Module Federation.")
        Container(user-profile, "User profile", "React", "Loaded via Module Federation.")
    }

    Container_Boundary(gateway_layer, "Edge Layer") {
        Container(gateway, "API Gateway", "Spring", "Routing, Load Balancing (Client-Side), Security.")
    }

    Container_Boundary(services, "Microservices Layer") {
        Container(user, "User Service", "Spring Boot", "Manages users.")
        Container(post, "Post Service", "Spring Boot", "Manages posts.")
        Container(notification, "Notification", "Spring Boot", "Manages notifications.")
    }

    Container_Boundary(infra, "Infrastructure Layer") {
        ContainerDb(db, "PostgreSQL", "SQL Database", "Stores User and Post data.")
        ContainerDb(broker, "RabbitMQ", "Message Broker", "Handles RPC between gateway and services.")
        ContainerDb(kafka, "Kafka", "Event Streaming", "Handles streams to notification service.")
        ContainerDb(nodered, "NodeRed", "Low Code FAAS service", "Handles random post generation.")
    }

    Rel(user, host, "Loads App", "HTTP")
    Rel(host, dashboard, "Lazily Loads", "Module Federation")
    Rel(host, user-profile, "Lazily Loads", "Module Federation")
    Rel(host, gateway, "API Calls", "HTTPS / JSON")

    Rel(gateway, broker, "Performs RPC", "AMQP")
    Rel(broker, user, "Routes", "Handles users and authentication")
    Rel(broker, post, "Routes", "Handles posts")
    
    Rel(user, kafka, "Publishes notification data")
    Rel(post, kafka, "Publishes notification data")
    Rel(kafka, notification, "Consumes notification data")

    Rel(user, db, "Reads/Writes users", "JDBC")
    Rel(post, db, "Reads/Writes posts", "JDBC")
    
    Rel(post, nodered,"Get random post content")

```

### 2. UML activity diagram

```mermaid
sequenceDiagram
    autonumber
    actor User

    participant Dashboard as Dashboard (Remote)
    participant Gateway as API Gateway

    participant UserService as UserService
    participant Post as Post Service
    participant DB as PostgreSQL
    participant Rabbit as RabbitMQ

    Note over User, Dashboard: User adds a post
    User->>Dashboard: Click Add
    Dashboard->>Gateway: POST /posts (Bearer Token)
    Gateway->>UserService: Validate JWT
    UserService->>Gateway: OK
    Gateway->>Rabbit: RPC call add-post
    Rabbit->>Post: Consume call

    activate Post

    Post->>Post: Generate Post
    Post->>DB: INSERT INTO Posts (Status: CONFIRMED)
    Post->>Rabbit: Reply

    deactivate Post

    Rabbit->>Gateway: Receive reply

    Gateway-->>Dashboard: 200 OK
    
    Dashboard->>Gateway: GET /posts (Bearer Token)
    Gateway->>UserService: Validate JWT
    UserService->>Gateway: OK
    Gateway->>Rabbit: RPC call add-post
    Rabbit->>Post: Consume call

    activate Post

    Post->>DB: SELECT FROM Posts
    Post->>Rabbit: Reply

    deactivate Post

    Rabbit->>Gateway: Receive reply

    Gateway-->>Dashboard: 200 OK

    Dashboard-->>User: Show posts with new post
```

