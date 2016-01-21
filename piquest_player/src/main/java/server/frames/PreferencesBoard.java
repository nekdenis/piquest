package server.frames;

import server.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * App preferences board
 */
public class PreferencesBoard extends JFrame {

    private JLabel pathLabel;
    private JLabel portLabel;
    private JTextField pathField;
    private JTextField portField;
    private JButton chooseFile;
    private JButton startServer;

    public PreferencesBoard() {

        super("PreferencesBoard");

        pathLabel = new JLabel();
        pathLabel.setText("Enter video folder path");

        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new BoxLayout(pathPanel, BoxLayout.X_AXIS));

        pathField = new JTextField();
        pathField.setText("/Users/macbookpro/Movies/");

        pathPanel.add(pathField);

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setApproveButtonText("Choose folder");

        chooseFile = new JButton("Browse");
        chooseFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onFileSelected(fileChooser);
            }
        });

        pathPanel.add(chooseFile);

        portLabel = new JLabel();
        portLabel.setText("Enter port");

        portField = new JTextField();
        portField.setText("5660");

        startServer = new JButton("Start");
        startServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startConnection();
            }
        });

        getContentPane().add(pathLabel);
        getContentPane().add(pathPanel);
        getContentPane().add(portLabel);
        getContentPane().add(portField);
        getContentPane().add(startServer);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        setVisible(true);
    }

    private void onFileSelected(JFileChooser fileChooser) {
        int returnVal = fileChooser.showDialog(PreferencesBoard.this, null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            pathField.setText(fileChooser.getSelectedFile().getAbsolutePath()+File.separator);
        }
    }

    private void startConnection() {
        String path = pathField.getText();
        int port = Integer.parseInt(portField.getText());
        Main.startConnectionBoard(path, port);
        setVisible(false);
    }

}
