# client_server

![Client-Server Architecture](assets/architecture.png)

📌 Overview
This project demonstrates a Client-Server architecture using Java with a connected database (MySQL/PostgreSQL). The server handles client requests, processes data, and communicates with the database, while the client interacts with the server for data operations.

🏗️ Architecture
[Client] ⇄ [Server] ⇄ [Database]

Client: Java application (GUI/CLI) that sends requests to the server.
Server: Java-based backend handling client requests and communicating with the database.
Database: Stores data and processes SQL queries from the server.

⚙️ Technologies Used
Java (Socket Programming, JDBC)
MySQL (Database)

🚀 Getting Started
1️⃣ Setup Database
Install MySQL/PostgreSQL
Create a database and table
Update connection details in config.properties

2️⃣ Run the Server
javac Server.java  
java Server  

3️⃣ Run the Client
javac Client.java  
java Client  

📁 Project Structure
📦 Client-Server-Java  
 ┣ 📂 client  
 ┃ ┣ 📜 Client.java  
 ┃ ┣ 📜 DatabaseConnection.java  
 ┣ 📂 server  
 ┃ ┣ 📜 Server.java  
 ┃ ┣ 📜 DatabaseConnection.java  
 ┣ 📂 database  
 ┃ ┗ 📜 schema.sql  
 ┗ 📜 README.md  

🔗 Database Configuration (config.properties)
DB_URL=jdbc:mysql://localhost:3306/mydb  
DB_USER=root  
DB_PASSWORD=yourpassword  

📝 Features
✔️ Multi-client support
✔️ Secure database transactions
✔️ Real-time communication via sockets
✔️ CRUD operations on the database

🎯 Future Enhancements
Implement authentication
Improve error handling
Add a REST API version
📌 Author
👨‍💻 [SASI] | 🚀 Java Developer
