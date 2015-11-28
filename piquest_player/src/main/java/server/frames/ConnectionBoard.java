package server.frames;

import server.Main;
import server.sockets.SocketBroadcastServer;
import server.sockets.SocketMainServer;

import javax.swing.*;
import javax.swing.plaf.FileChooserUI;
import java.io.IOException;

/**
 * Send broadcast and wait for connections
 */
public class ConnectionBoard extends JFrame {

    private JLabel message;
    private SocketMainServer server;
    private SocketBroadcastServer broadcastServer;

    public ConnectionBoard(int port) {

        super("ConnectionBoard");

        JPanel panelFields = new JPanel();
        panelFields.setLayout(new BoxLayout(panelFields, BoxLayout.X_AXIS));

        initSockets(port);

        message = new JLabel("Waiting for connection");
        message.setSize(200, 150);

        panelFields.add(message);

        getContentPane().add(panelFields);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        setVisible(true);
    }

    private void initSockets(int port) {
        try {
            broadcastServer = new SocketBroadcastServer("SocketBroadcastServer");

            broadcastServer.start();
            server = new SocketMainServer(new SocketMainServer.OnMessageReceived() {
                public void messageReceived(String message) {
                }
            }, new SocketMainServer.OnConnectionChangeListener() {
                public void connected() {
                    broadcastServer.stopServer();
                    startMainFrame();
                }

                public void disconnected() {
                    broadcastServer.start();
                }
            }, port);
            server.start();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void startMainFrame() {
        Main.startVideoBoard(server);
        setVisible(false);
    }
}
