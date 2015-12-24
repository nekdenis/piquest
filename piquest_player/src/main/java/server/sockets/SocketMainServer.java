package server.sockets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Prashant Adesara
 *         The class extends the Thread class so we can receive and send messages at the
 *         same time
 */
public class SocketMainServer extends Thread {
    public static final String MESSAGE_START = "Server_hi!";
    public static final String MESSAGE_END = "Server_bye!";
    private int SERVERPORT;
    private ServerSocket serverSocket;
    private Socket client = null;
    private boolean running = false;
    private PrintWriter mOut;
    private OnMessageReceived messageListener;
    private OnConnectionChangeListener connectionListener;

    /**
     * Constructor of the class
     *
     * @param messageListener listens for the messages
     * @author Prashant Adesara
     */
    public SocketMainServer(OnMessageReceived messageListener, OnConnectionChangeListener connectionListener, int serverPort) {
        this.SERVERPORT = serverPort;
        this.messageListener = messageListener;
        this.connectionListener = connectionListener;
    }

    /**
     * Method to send the messages from server to client
     *
     * @param message the message sent by the server
     * @author Prashant Adesara
     */
    public void sendMessage(String message) {
        try {
            if (mOut != null && !mOut.checkError()) {
                System.out.println(message);
                // Here you can connect with database or else you can do what you want with static message
                mOut.println(message);
                mOut.flush();
            }
        } catch (Exception e) {
        }
    }

    /**
     * @author Prashant Adesara
     */
    @Override
    public void run() {
        super.run();
        running = true;
        try {
            System.out.println("PA: Connecting...");

            // create a server socket. A server socket waits for requests to
            // come in over the network.
            serverSocket = new ServerSocket(SERVERPORT);

            // create client socket... the method accept() listens for a
            // connection to be made to this socket and accepts it.
            try {
                client = serverSocket.accept();
                System.out.println("S: Receiving...");
                // sends the message to the client
                mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                System.out.println("PA: Sent");
                System.out.println("PA: Connecting Done.");
                // read the message received from client
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                // in this while we wait to receive messages from client (it's an infinite loop)
                // this while it's like a listener for messages
                while (running) {
                    String message = in.readLine();
                    if (message != null) {
                        processReceivedMessage(message);
                    }
                }
            } catch (Exception e) {
                System.out.println("PA: Error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                client.close();
                System.out.println("PA: Done.");
            }
        } catch (Exception e) {
            System.out.println("PA: Error");
            e.printStackTrace();
        }

    }

    public void setMessageListener(OnMessageReceived messageListener) {
        this.messageListener = messageListener;
    }

    public void setConnectionListener(OnConnectionChangeListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    private void processReceivedMessage(String message) {
        if (message.contains(MESSAGE_START)) {
            sendMessage("Server connected with Android Client now you can chat with socket server.");
            System.out.println("Received start message");
            if (connectionListener != null) {
                connectionListener.connected();
            }
        } else if (message.contains(MESSAGE_END)) {
            System.out.println("Received end message");
            if (connectionListener != null) {
                connectionListener.disconnected();
            }
        } else if (messageListener != null) {
            messageListener.messageReceived(message);
        }
    }

    /**
     * Declare the interface. The method messageReceived(String message) will
     *
     * @author Prashant Adesara
     *         must be implemented in the ConnectionBoard
     *         class at on configureServer button click
     */
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

    public interface OnConnectionChangeListener {
        public void connected();

        public void disconnected();
    }

}
