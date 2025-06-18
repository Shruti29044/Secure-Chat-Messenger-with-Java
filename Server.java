
// Server.java
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private static final String SECRET_KEY = "1234567812345678"; // 16-byte AES key

    private ServerSocket serverSocket;
    private ExecutorService pool;
    private List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
        pool = Executors.newCachedThreadPool();
        System.out.println("Server started on port " + PORT);
        listen();
    }

    private void listen() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket);
            clients.add(handler);
            pool.execute(handler);
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }

        public void run() {
            try {
                while (true) {
                    String encryptedMsg = in.readUTF();
                    String message = decrypt(encryptedMsg);
                    System.out.println("Received: " + message);
                    broadcast(message);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected");
            } finally {
                try { socket.close(); } catch (IOException e) { }
                clients.remove(this);
            }
        }

        private void broadcast(String message) {
            for (ClientHandler client : clients) {
                try {
                    String encrypted = encrypt(message);
                    client.out.writeUTF(encrypted);
                    client.out.flush();
                } catch (IOException e) { }
            }
        }
    }

    private static String encrypt(String str) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(str.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            return null;
        }
    }

    private static String decrypt(String str) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(str));
            return new String(decrypted);
        } catch (Exception e) {
            return "[Decryption error]";
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }
}
