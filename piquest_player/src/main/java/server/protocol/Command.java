package server.protocol;

public class Command {
    public static final String COMMAND_PLAY = "PLAY";
    public static final String COMMAND_FINISHED_INTRO = "FINISHED_INTRO";
    public static final String COMMAND_FINISHED_GAME = "FINISHED_GAME";

    public static final int VALUE_PLAY_INTRO_VIDEO = 0;
    public static final int VALUE_PLAY_FIRST_END_VIDEO = 1;
    public static final int VALUE_PLAY_SECOND_END_VIDEO = 2;
}