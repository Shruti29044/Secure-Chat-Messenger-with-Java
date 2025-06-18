# README for Secure Chat Messenger (Java Secure Client-Server)

## 📝 Project Overview
This project implements a simple secure chat application using Java. It includes:

- ✅ A multithreaded **Server** to handle multiple clients.
- ✅ A Swing-based **Client GUI**.
- ✅ Full end-to-end AES encryption for secure communication.

---

## 🖥️ Technologies Used
- Java (SE 8+)
- Swing (for GUI)
- Java Sockets (for TCP network communication)
- Java Cryptography API (AES encryption)

---

## 📂 Project Structure
```
SecureChatMessenger/
 ├── Server.java   # Server-side code
 └── Client.java   # Client-side code
```

---

## 🔧 Prerequisites
- Java JDK 8 or higher installed on your system.
- Verify installation:
```bash
java -version
```

---

## 🚀 How to Compile and Run

### 1️⃣ Extract the ZIP file
Unzip `SecureChatMessenger.zip` and navigate into the folder:
```bash
cd path/to/SecureChatMessenger
```

### 2️⃣ Compile both server and client:
```bash
javac Server.java Client.java
```

### 3️⃣ Start the server (run this first):
```bash
java Server
```
Output:
```
Server started on port 12345
```

### 4️⃣ Start the client (in another terminal or machine):
```bash
java Client
```
The Secure Chat Client GUI will open.

---

## 🔑 Default IP Configuration
- By default, the client connects to `127.0.0.1` (localhost).
- If you're running both server and client on the **same machine**, no changes are needed.
- If you're running client and server on **different machines**, modify this line in `Client.java`:
```java
private static final String SERVER_IP = "127.0.0.1";
```
Change `127.0.0.1` to your server's real IP address on the network.

---

## 🔐 Security Details
- **Encryption:** AES (symmetric encryption, 128-bit key)
- **Key:** `1234567812345678` (for demo purpose — you should generate a strong random key in production)
- **Data flow:**
  - Client encrypts messages → sends over TCP socket
  - Server decrypts, rebroadcasts encrypted messages to all clients
  - Clients decrypt and display

---

## 🌐 Running on a LAN (Multi-device setup)
1️⃣ Ensure all devices are on same LAN network.

2️⃣ Open firewall on server machine to allow incoming connections on port `12345`.

3️⃣ Get server IP using:
```bash
ipconfig  (on Windows)
ifconfig  (on Linux/Mac)
```

4️⃣ Update `SERVER_IP` in Client.java to this IP.

---

## 🚧 Known Limitations
- AES key is hardcoded (improve for production).
- No authentication or user management.
- Server must be started manually.
- No offline message storage.

---

## 🔮 Possible Extensions
- Add user login / authentication system
- Use RSA key exchange + AES for stronger encryption
- Add message history / offline support
- Package into runnable JAR (automatic startup)
- Add Docker deployment
- Make fully internet-accessible (with NAT/port forwarding)

---

## ⚠ Challenges

### 1️⃣ Encryption Key Management
- Hardcoded symmetric keys are not secure for production systems.
- Key exchange mechanisms (e.g. Diffie-Hellman, RSA) should be integrated.

### 2️⃣ Network Connectivity
- NAT, firewalls, and network configurations can block client-server communication across different networks.

### 3️⃣ Lack of Authentication
- No user authentication means anyone can connect if they know the server IP.
- Secure authentication methods should be added.

### 4️⃣ No Persistent Chat History
- Current system does not store chat history. Messages are lost after disconnect.

### 5️⃣ GUI Limitations
- Basic Swing interface without rich user experience.
- Limited error handling for UI disconnections.

### 6️⃣ Scalability
- This server can handle a small number of clients but would need optimization for large-scale deployment.

### 7️⃣ Production Security
- TLS should be added to secure data-in-transit.
- Stronger encryption standards and proper secret storage required for production-grade applications.

---

## 📄 License
This project is provided for educational purposes only.

---

