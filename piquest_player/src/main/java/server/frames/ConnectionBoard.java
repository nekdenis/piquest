package server.frames;

import server.Main;
import server.sockets.SocketBroadcastServer;
import server.sockets.SocketMainServer;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
        String ipAddr = getIp();
        message = new JLabel("Waiting for connection at ip: " + ipAddr);
        message.setSize(200, 150);

        panelFields.add(message);

        getContentPane().add(panelFields);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                server.stopServer();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private String getIp() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return "";
        }
        return addr.getHostAddress();
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
        dispose();
    }
}
