package server.frames;

import server.player.MediaPlayerController;
import server.sockets.SocketMainServer;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VideoBoard extends JFrame {

    private static final boolean IS_FULLSCREEN = false;

    public VideoBoard(SocketMainServer server, String path) {

        super("ConnectionBoard");

        JPanel panelFields = new JPanel();
        panelFields.setLayout(new BoxLayout(panelFields, BoxLayout.X_AXIS));

        JPanel panelFields2 = new JPanel();
        panelFields2.setLayout(new BoxLayout(panelFields2, BoxLayout.X_AXIS));


        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        setLayout(new BorderLayout());

        final EmbeddedMediaPlayerComponent mediaPlayerComponent = initMediaPlayer();
        MediaPlayerController mediaPlayerController = new MediaPlayerController(mediaPlayerComponent, server, path);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });

        setContentPane(mediaPlayerComponent);

        if (IS_FULLSCREEN) { //start fullscreen
            GraphicsDevice gd =
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            if (gd.isFullScreenSupported()) {
                setUndecorated(true);
                gd.setFullScreenWindow(this);
            } else {
                System.err.println("Full screen not supported");
            }
        }
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        mediaPlayerController.configureServer();
    }

    private EmbeddedMediaPlayerComponent initMediaPlayer() {
        return new EmbeddedMediaPlayerComponent();
    }
}
