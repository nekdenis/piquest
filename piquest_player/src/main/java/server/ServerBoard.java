package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.IOException;

/**
 * @author Prashant Adesara
 *         Handle UI part with sending messages to clients
 */
public class ServerBoard extends JFrame {

    private static final float MAX_X_VALUE = 7f;
    private static final float MAX_Y_VALUE = 5f;

    private static final long serialVersionUID = 1L;
    private JTextArea messagesArea;
    private JTextField message;
    private JButton startServer;
    private SocketMainServer mServer;
    private SocketBroadcastServer mBroadcastServer;
    Robot r;
    double width;
    double height;

    public ServerBoard() {

        super("ServerBoard - Prashant Adesara");

        JPanel panelFields = new JPanel();
        panelFields.setLayout(new BoxLayout(panelFields, BoxLayout.X_AXIS));

        JPanel panelFields2 = new JPanel();
        panelFields2.setLayout(new BoxLayout(panelFields2, BoxLayout.X_AXIS));

        // here we will have the text messages screen
        messagesArea = new JTextArea();
        messagesArea.setColumns(30);
        messagesArea.setRows(10);
        messagesArea.setEditable(false);

        try {
            r = new Robot();
            initScreen();
        } catch (AWTException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        startServer = new JButton("Start");
        startServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // disable the start button
                startServer.setEnabled(false);


                messagesArea.append("Server Started, now start Android Client");
                // creates the object OnMessageReceived asked by the DispatcherServer
                // constructor

                try {
                    mBroadcastServer = new SocketBroadcastServer("SocketBroadcastServer");

                    mBroadcastServer.start();
                    mServer = new SocketMainServer(new SocketMainServer.OnMessageReceived() {


                        @Override
                        // this method declared in the interface from DispatcherServer
                        // class is implemented here
                        // this method is actually a callback method, because it
                        // will run every time when it will be called from
                        // DispatcherServer class (at while)
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
        });

        // the box where the user enters the text (EditText is called in
        // Android)
        message = new JTextField();
        message.setSize(200, 20);
        message.setScrollOffset(1);

        // add the buttons and the text fields to the panel
        JScrollPane sp = new JScrollPane(messagesArea);
        panelFields.add(sp);
        panelFields.add(startServer);

        panelFields2.add(message);

        getContentPane().add(panelFields);
        getContentPane().add(panelFields2);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        setSize(300, 170);
        setVisible(true);
    }

    private void processMessage(String message) {
        System.out.println("Msg Recieved");
        messagesArea.append("\n" + message);
        if (message.contains("move_")) {
            String subSX = message.substring(message.indexOf("x") + 1, message.indexOf("y"));
            double moveX = Double.parseDouble(subSX);
            String subSY = message.substring(message.indexOf("y") + 1, message.length());
            double moveY = Double.parseDouble(subSY);
            r.mouseMove(getXOffset(moveX), getYOffset(moveY));
        } else if (message.contains("clck_d_")) {
            if (message.contains("lc")) {
                r.mousePress(InputEvent.BUTTON1_MASK);
            } else {
                r.mousePress(InputEvent.BUTTON2_MASK);
            }
        } else if (message.contains("clck_u_")) {
            if (message.contains("lc")) {
                r.mouseRelease(InputEvent.BUTTON1_MASK);
            } else {
                r.mouseRelease(InputEvent.BUTTON2_MASK);
            }
        }
    }

    private void initScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = screenSize.getWidth() / 2d;
        height = screenSize.getHeight() / 2d;
    }

    private int getXOffset(double value) {
        int result = (int) (width - width * value / MAX_X_VALUE);
        //System.out.println("x = "+result);
        return result;
    }

    private int getYOffset(double value) {
        return (int) (height - height * value / MAX_Y_VALUE);
    }
}
