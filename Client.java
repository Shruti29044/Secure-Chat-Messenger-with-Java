
// Client.java
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Base64;

public class Client {
    private static final String SERVER_IP = "127.0.0.1"; // Replace with server IP if needed
    private static final int SERVER_PORT = 12345;
    private static final String SECRET_KEY = "1234567812345678"; // 16-byte AES key

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    public Client() {
        setupGUI();
        connectToServer();
        startListening();
    }

    private void setupGUI() {
        frame = new JFrame("Secure Chat Client");
        chatArea = new JTextArea(20, 40);
        chatArea.setEditable(false);
        inputField = new JTextField(30);
        sendButton = new JButton("Send");

        JPanel panel = new JPanel();
        panel.add(inputField);
        panel.add(sendButton);

        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            chatArea.append("Connected to server\n");
        } catch (IOException e) {
            chatArea.append("Unable to connect to server\n");
        }
    }

    private void startListening() {
        Thread listener = new Thread(() -> {
            try {
                while (true) {
                    String encryptedMsg = in.readUTF();
                    String message = decrypt(encryptedMsg);
                    chatArea.append("[Friend]: " + message + "\n");
                }
            } catch (IOException e) {
                chatArea.append("Disconnected from server\n");
            }
        });
        listener.start();
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            try {
                String encrypted = encrypt(message);
                out.writeUTF(encrypted);
                out.flush();
                chatArea.append("[You]: " + message + "\n");
                inputField.setText("");
            } catch (IOException e) {
                chatArea.append("Failed to send message\n");
            }
        }
    }

    private String encrypt(String str) {
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

    private String decrypt(String str) {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}
