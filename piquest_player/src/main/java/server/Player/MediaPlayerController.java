package server.player;

import server.sockets.SocketMainServer;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static server.protocol.Command.*;

public class MediaPlayerController {

    final private EmbeddedMediaPlayer player;
    private String filePrefix;
    private SocketMainServer server;

    public MediaPlayerController(EmbeddedMediaPlayerComponent mediaPlayerComponent, JFrame frame, SocketMainServer server, String path) {
        frame.setUndecorated(true);
        DefaultFullScreenStrategy fsStrat = new DefaultFullScreenStrategy(frame);
        player = mediaPlayerComponent.getMediaPlayer();
        player.setFullScreenStrategy(fsStrat);

        frame.setContentPane(mediaPlayerComponent);

        this.filePrefix = path;
        this.server = server;

        player.setEnableKeyInputHandling(false);
        player.setEnableMouseInputHandling(false);
        player.addMediaPlayerEventListener(new SimpleMediaPlayerListener() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.out.println("Player finished");
                MediaPlayerController.this.server.sendMessage(COMMAND_FINISHED);
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.out.println("Player error");
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
                System.out.println("Player stopped");
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
                System.out.println("Player paused");
            }
        });

        player.setFullScreen(true);
        initListeners(mediaPlayerComponent);
        frame.setVisible(true);
//        playVideo(0);
    }

    private void initListeners(final Panel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (!player.isFullScreen()) {
                    System.out.println("Enabling fullscreen.");
                    player.setFullScreen(true);
                }
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE && player.isFullScreen()) {
                    System.out.println("Disabling fullscreen.");
                    player.setFullScreen(false);
                }
            }
        });
    }


    public void configureServer() {
        server.setMessageListener(new SocketMainServer.OnMessageReceived() {
            public void messageReceived(String message) {
                processMessage(message);
            }
        });
        server.setConnectionListener(new SocketMainServer.OnConnectionChangeListener() {
            public void connected() {

            }

            public void disconnected() {
                //TODO: show alert
            }
        });
    }

    public EmbeddedMediaPlayer getPlayer() {
        return player;
    }

    private void processMessage(String message) {
        if (message.contains(COMMAND_PLAY)) {
            Integer num = Integer.parseInt(message.substring(COMMAND_PLAY.length() + 1));
            playVideo(num);
        }
    }

    private void playVideo(int num) {
        String filePath = getFilePathByNum(num);
        player.playMedia(filePath);
    }

    private String getFilePathByNum(int num) {
        switch (num) {
            case VALUE_PLAY_INTRO_VIDEO:
                return filePrefix + "intro.mp4";
            case VALUE_PLAY_FIRST_END_VIDEO:
                return filePrefix + "first_end.mp4";
            case VALUE_PLAY_SECOND_END_VIDEO:
                return filePrefix + "second_end.mp4";
            default:
                return filePrefix + "cat.mp4";
        }
    }
}
