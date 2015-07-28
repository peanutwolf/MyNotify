package com.peanutwolf;


import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class Main{


    public static void main(String... args){
//        MyMailDevice dev = new MyMailDevice();
//        InnerNotifier innerNotifier = new InnerNotifier();
//        MailService mailService = new MailService(new ImapMailProtoPort("imap.gmail.com", "peanutwolf@gmail.com", "204655jekA204655jekA", "993"));
//        mailService.addEventListener(dev);
//        mailService.addEventListener(innerNotifier);
//        Thread newThread = new Thread(mailService);
//        newThread.start();
//        MyJFrameTray frame = MyJFrameTray.getInstance();
//        Thread frameThread = new Thread(frame);
//        frameThread.start();

        VKProtoPort vkPort = new VKProtoPort();
        String access_token = vkPort.setConnection("**********", "***********");

        VKservice vkService = new VKservice(vkPort);
        Thread newThread = new Thread(vkService);
        newThread.start();

    }

}
