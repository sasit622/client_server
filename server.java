package chat.server;

import chat.database.DBConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dad extends Frame implements Runnable, ActionListener {
    TextField textField;
    TextArea textArea;
    Button send;
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Thread chat;

    public Dad() {
        // Create components
        textField = new TextField(30);  // Fixed size for the input field
        textArea = new TextArea(20, 50);  // Set size for the chat area
        textArea.setEditable(false);  // Make the chat area read-only
        send = new Button("Send");

        // Add action listener to send button
        send.addActionListener(this);

        // Add "Enter" key listener to textField
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        // Set up server socket
        try {
            serverSocket = new ServerSocket(12000);
            socket = serverSocket.accept();  // Accept connection

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Layout and component arrangement
        setLayout(new BorderLayout());

        // Panel for text field and button at the bottom
        Panel inputPanel = new Panel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(textField);
        inputPanel.add(send);

        // Add components to frame
        add(textArea, BorderLayout.CENTER);  // Chat area in the center
        add(inputPanel, BorderLayout.SOUTH);  // Input panel at the bottom

        // Start chat thread
        chat = new Thread(this);
        chat.setDaemon(true);
        chat.start();

        // Frame settings
        setSize(600, 400);  // Adjust window size
        setTitle("DAD Chat");
        setVisible(true);

        // Window close behavior
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        // Load previous chat messages from DB
        loadPreviousMessages();
    }

    // Handle send button click
    public void actionPerformed(ActionEvent e) {
        sendMessage();
    }

    // Method to send the message
    private void sendMessage() {
        String msg = textField.getText();
        if (msg.trim().isEmpty()) {
            return; // Don't send empty messages
        }
        textArea.append("DAD:" + msg + "\n");
        textField.setText("");  // Clear the text field after sending
        
        // Debugging print
        System.out.println("Sending message: " + msg);

        try {
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();

            // Save message to DB
            saveMessageToDB("DAD", msg);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Thread for receiving messages
    public void run() {
        while (true) {
            try {
                String msg = dataInputStream.readUTF();
                textArea.append("SON:" + msg + "\n");

                // Save received message to DB
                saveMessageToDB("SON", msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Save the message to the database
    public void saveMessageToDB(String sender, String message) {
        if (message == null || message.trim().isEmpty()) {
            System.out.println("Empty message, nothing to save.");
            return;
        }

        String query = "INSERT INTO messages (sender, message) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sender);
            stmt.setString(2, message);
            int rowsInserted = stmt.executeUpdate();
            
            if (rowsInserted > 0) {
                System.out.println("Message saved to DB: " + sender + ": " + message);
            } else {
                System.out.println("Failed to insert message into DB.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load previous messages from the database
    public void loadPreviousMessages() {
        String query = "SELECT sender, message FROM messages ORDER BY id ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String sender = rs.getString("sender");
                String message = rs.getString("message");
                textArea.append(sender + ": " + message + "\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Dad();
    }
}
