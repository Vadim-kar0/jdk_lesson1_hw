package org.server;

import org.client.ClientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame {
    public static final int POS_X = 500;
    public static final int POS_Y = 558;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;

    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JTextArea log = new JTextArea();
    private boolean isServerWorking;
    private final String backUpFileName = "backUp.txt";
    private List<ClientGUI> users = new ArrayList<>();



    public List<ClientGUI> getUsers() {
        return users;
    }

    public JTextArea getLog() {
        return log;
    }

    public boolean isServerWorking() {
        return isServerWorking;
    }

    public void setServerWorking(boolean serverWorking) {
        isServerWorking = serverWorking;
    }

    public static void main(String[] args) {
        new ServerWindow();
    }

    public ServerWindow(){
        isServerWorking = false;
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = false;
                log.append("Server stopped " + "\n");
                for (ClientGUI user : users) {
                    user.getPanelTop().setVisible(true);
                    user.setInServer(false);
                    user.getLog().append("Вы были отключены от сервера\n");
                }
                users.clear();

            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isServerWorking = true;
                log.append("Server running " + "\n");
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(POS_X,POS_Y,WIDTH,HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);

        JPanel panelBottom = new JPanel(new GridLayout(1,2));
        panelBottom.add(btnStart);
        panelBottom.add(btnStop);

        JScrollPane scrollLog = new JScrollPane(log);

        add(panelBottom,BorderLayout.SOUTH);
        add(scrollLog);

        setVisible(true);


    }


    public void writeLogToFile(String text)  {
        try(FileOutputStream fileOutputStream = new FileOutputStream(backUpFileName,true)){
            fileOutputStream.write(text.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeLogToWindow(JTextArea area){
        try (FileReader in = new FileReader(backUpFileName);
             BufferedReader reader = new BufferedReader(in)){
            while (reader.ready()){
                String line = reader.readLine();
                area.append(line + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message){
        log.append(message);
        writeLogToFile(message);
        for (ClientGUI user : users) {
            user.getLog().append(message);
        }
    }




}
