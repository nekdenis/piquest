package server;

import server.frames.ConnectionBoard;
import server.frames.PreferencesBoard;
import server.frames.VideoBoard;
import server.sockets.SocketMainServer;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class Main {
    private static String filePath = "/Users/macbookpro/Movies/aaa";

    public static void main(String[] args) {
        new NativeDiscovery().discover();
        startPreferencesBoard();
    }

    public static void startVideoBoard(SocketMainServer server) {
        new VideoBoard(server, filePath);
    }

    public static void startPreferencesBoard() {
        PreferencesBoard frame = new PreferencesBoard();
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public static void startConnectionBoard(String path, int port) {
        filePath = path;
        ConnectionBoard frame = new ConnectionBoard(port);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
