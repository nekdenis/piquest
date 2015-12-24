package server.player;

import server.sockets.SocketMainServer;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static server.protocol.Command.*;

public class MediaPlayerController {

    private String filePrefix;
    private SocketMainServer server;
    final private EmbeddedMediaPlayer player;

    public MediaPlayerController(Canvas canvas, JFrame frame, SocketMainServer server, String path) {
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        DefaultFullScreenStrategy fsStrat = new DefaultFullScreenStrategy(frame);

        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
        player = mediaPlayerFactory.newEmbeddedMediaPlayer(fsStrat);

        player.setVideoSurface(videoSurface);

        this.filePrefix = path;
        this.server = server;

        player.setEnableKeyInputHandling(false);
        player.setEnableMouseInputHandling(false);
        player.addMediaPlayerEventListener(new SimpleMediaPlayerListener() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                MediaPlayerController.this.server.sendMessage(COMMAND_FINISHED);
            }
        });

        player.setFullScreen(true);
        initListeners(canvas);
    }

    private void initListeners(Canvas canvas) {
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (!player.isFullScreen()) {
                    System.out.println("Enabling fullscreen.");
                    player.setFullScreen(true);
                }
            }
        });
        canvas.addKeyListener(new KeyAdapter() {
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
