package com.peanutwolf;


import java.io.IOException;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main{

    public static void main(String... args){
        Thread VKServiceThread;
        Thread MailServiceThread;
        Thread NotifyServiceThread;
        Thread HardwareDevThread;


        MyJFrameTray tray = MyJFrameTray.getInstance();

        try {
            MailService mailService = new MailService("imap.gmail.com", "peanutwolf@gmail.com", "204655jekA204655jekA", "993");
            mailService.addEventListener(tray);
            MailServiceThread = new Thread(mailService);
            MailServiceThread.start();
        }catch (Error err){
            err.printStackTrace();
        }

        while (true){}

    }

}