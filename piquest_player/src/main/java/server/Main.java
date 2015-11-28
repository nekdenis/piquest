package server;

import server.frames.ConnectionBoard;
import server.frames.PreferencesBoard;
import server.frames.VideoBoard;
import server.sockets.SocketMainServer;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import javax.swing.*;

public class Main {
    private static String filePath;

    public static void main(String[] args) {
        startPreferencesBoard();
    }

    public static void startVideoBoard(SocketMainServer server) {
        VideoBoard frame = new VideoBoard(server, filePath);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // *** center the app ***
        frame.pack();
        frame.setVisible(true);
    }

    public static void startPreferencesBoard() {
        new NativeDiscovery().discover();
        PreferencesBoard frame = new PreferencesBoard();
        frame.setLocationRelativeTo(null); // *** center the app ***
        frame.pack();
        frame.setVisible(true);
    }

    public static void startConnectionBoard(String path, int port) {
        filePath = path;
        ConnectionBoard frame = new ConnectionBoard(port);
        frame.setLocationRelativeTo(null); // *** center the app ***
        frame.pack();
        frame.setVisible(true);
    }
}
