package com.peanutwolf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by vigursky on 23.07.2015.
 */
public class MyTrayIcon {
    private TrayIcon trayIcon;
    private static MyTrayIcon ourInstance = new MyTrayIcon();

    public static MyTrayIcon getInstance() {
        return ourInstance;
    }

    private MyTrayIcon() {
        PopupMenu popup = new PopupMenu();
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        popup.add(exitItem);
        SystemTray systemTray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage("vk_icon.png");
        trayIcon = new TrayIcon(image,"MailNotifier",popup);
        trayIcon.setImageAutoSize(true);
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    public void displayTrayMessage(String msg){
        trayIcon.displayMessage("MailNotifier", msg, TrayIcon.MessageType.INFO);
    }
}
