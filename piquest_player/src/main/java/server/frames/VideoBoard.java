package server.frames;

import server.player.MediaPlayerController;
import server.sockets.SocketMainServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VideoBoard extends JFrame {

    private final MediaPlayerController mediaPlayerController;

    public VideoBoard(SocketMainServer server, String path) {

        super("VideoBoard");

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        frame.setContentPane(panel);
        Canvas canvas = new Canvas();
        canvas.setSize(400, 400);
        canvas.setBackground(Color.white);
        panel.add(canvas);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        mediaPlayerController = new MediaPlayerController(canvas, frame, server, path);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerController.getPlayer().release();
                System.exit(0);
            }
        });

        mediaPlayerController.configureServer();
    }
}
