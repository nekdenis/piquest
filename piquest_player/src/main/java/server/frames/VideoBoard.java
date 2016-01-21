package server.frames;

import server.player.MediaPlayerController;
import server.sockets.SocketMainServer;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VideoBoard extends JFrame {

    private final MediaPlayerController mediaPlayerController;

    public VideoBoard(final SocketMainServer server, String path) {

        super("VideoBoard");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);

        setLocationRelativeTo(null);

        final EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        mediaPlayerController = new MediaPlayerController(mediaPlayerComponent, this, server, path);

//        GraphicsDevice gd =
//                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//        if (gd.isFullScreenSupported()) {
//            setUndecorated(true);
//            gd.setFullScreenWindow(this);
//        } else {
//            System.err.println("Full screen not supported");
//        }
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerController.getPlayer().release();
                server.stopServer();
                System.exit(0);
            }
        });

        mediaPlayerController.configureServer();
    }
}
