package org.client;

import org.server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class ClientGUI extends JFrame {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    private final  JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2,3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JTextField tfLogin = new JTextField("Login");
    private final JPasswordField tfPassword = new JPasswordField("Password");
    private final JButton btnLogin = new JButton("Login");
    private final JButton button = new JButton();
    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");
    private boolean inServer = false;

    private String text;

    public JTextArea getLog() {
        return log;
    }

    public JPanel getPanelTop() {
        return panelTop;
    }

    public void setInServer(boolean inServer) {
        this.inServer = inServer;
    }

    public ClientGUI(ServerWindow serverWindow){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH,HEIGHT);
        setTitle("Chat Client");
        setAlwaysOnTop(true);
        setVisible(true);

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        button.setVisible(false);
        panelTop.add(button);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        add(panelTop,BorderLayout.NORTH);


        panelBottom.add(tfMessage,BorderLayout.CENTER);
        panelBottom.add(btnSend,BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!serverWindow.isServerWorking()){
                    log.append("Подключение не удалось. Сервер не активен!\n");
                } else {
                    log.append("Вы успешно подключились!\n");
                    inServer = true;
                    addGUI(serverWindow.getUsers());
                    serverWindow.writeLogToWindow(log);
                    serverWindow.getLog().append(tfLogin.getText() + " - подключился к беседе\n");
                    panelTop.setVisible(false);
                }
            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!serverWindow.isServerWorking()){
                    log.append("Сервер не активен!\n");}
                if(tfMessage.getText().length() > 0 && inServer){
                    String message = tfLogin.getText() + ": " + tfMessage.getText() + "\n";
                    serverWindow.sendMessage(message);
                    tfMessage.setText("");
                }
            }
        });

        tfMessage.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(!serverWindow.isServerWorking() && e.getKeyCode() == KeyEvent.VK_ENTER &&
                        tfMessage.getText().length() > 0){
                    log.append("Сервер не активен!\n");}
                if(e.getKeyCode() == KeyEvent.VK_ENTER && tfMessage.getText().length() > 0 && inServer) {
                    String message = tfLogin.getText() + ": " + tfMessage.getText() + "\n";
                    serverWindow.sendMessage(message);
                    tfMessage.setText("");
                }
            }
        });


    }

    private boolean addGUI(List<ClientGUI> list) {
        if(list.contains(this)){
            return false;
        }
        else return list.add(this);
    }

    public static void main(String[] args) {

    }
}
