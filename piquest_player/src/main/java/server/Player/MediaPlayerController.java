package server.player;

import server.sockets.SocketMainServer;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;

import static server.protocol.Command.*;

public class MediaPlayerController {

    private String filePrefix;
    private SocketMainServer server;
    private EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public MediaPlayerController(EmbeddedMediaPlayerComponent mediaPlayerComponent, SocketMainServer server, String path) {
        this.filePrefix = path;
        this.server = server;
        this.mediaPlayerComponent = mediaPlayerComponent;
        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new SimpleMediaPlayerListener() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                MediaPlayerController.this.server.sendMessage(COMMAND_FINISHED);
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

    private void processMessage(String message) {
        if (message.contains(COMMAND_PLAY)) {
            Integer num = Integer.parseInt(message.substring(COMMAND_PLAY.length() + 1));
            playVideo(num);
        }
    }

    private void playVideo(int num) {
        String filePath = getFilePathByNum(num);
        mediaPlayerComponent.getMediaPlayer().playMedia(filePath);
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
