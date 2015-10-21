package server;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class VideoBoard extends JFrame {

    private static final float MAX_X_VALUE = 7f;
    private static final float MAX_Y_VALUE = 5f;
    public static final String COMMAND_PLAY = "PLAY";

    private SocketMainServer mServer;
    private SocketBroadcastServer mBroadcastServer;
    private JViewport videoScreen;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public VideoBoard() {

        super("ServerBoard");

        JPanel panelFields = new JPanel();
        panelFields.setLayout(new BoxLayout(panelFields, BoxLayout.X_AXIS));

        JPanel panelFields2 = new JPanel();
        panelFields2.setLayout(new BoxLayout(panelFields2, BoxLayout.X_AXIS));

        // here we will have the text messages screen
        videoScreen = new JViewport();

        startServer();

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        setLayout(new BorderLayout());

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });

        setContentPane(mediaPlayerComponent);

        //start fullscreen
        GraphicsDevice gd =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

//        if (gd.isFullScreenSupported()) {
//            setUndecorated(true);
//            gd.setFullScreenWindow(this);
//        } else {
//            System.err.println("Full screen not supported");
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
//        }
    }

    private void startServer() {

        try {
            mBroadcastServer = new SocketBroadcastServer("SocketBroadcastServer");

            mBroadcastServer.start();
            mServer = new SocketMainServer(new SocketMainServer.OnMessageReceived() {


                @Override
                public void messageReceived(String message) {
                    processMessage(message);
                }
            }, new SocketMainServer.OnConnectionChangeListener() {
                @Override
                public void connected() {
                    mBroadcastServer.stopServer();
                }

                @Override
                public void disconnected() {
                    mBroadcastServer.start();
                }
            }
            );
            mServer.start();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private void processMessage(String message) {
        if (message.contains(COMMAND_PLAY)) {
            Integer num = Integer.parseInt(message.substring(COMMAND_PLAY.length()+1));
            playVideo(num);
        }
    }

    private void playVideo(Integer num) {
        String filePath;
        switch (num) {
            case 0:
            default:
                filePath = "file:///Volumes/Macintosh%20HD/Users/nekdenis/Movies/park_me_right_commercial_1280x720.mp4";
        }
        mediaPlayerComponent.getMediaPlayer().playMedia(filePath);
    }
}
