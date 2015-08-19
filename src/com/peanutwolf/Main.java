package com.peanutwolf;


public class Main{

    public static void main(String... args){
        Thread VKServiceThread;
        Thread MailServiceThread;
        Thread NotifyServiceThread;
        Thread HardwareDevThread;


        MyJFrameTray tray = MyJFrameTray.getInstance();

        try {
            MailService mailService = new MailService("imap.gmail.com", "peanutwolf@gmail.com", "", "993");
            mailService.addEventListener(tray);
            MailServiceThread = new Thread(mailService);
            MailServiceThread.start();
        }catch (Error err){
            err.printStackTrace();
        }

        try {
            VKservice vkService = new VKservice("peanutwolf@bk.ru", "");
            vkService.addEventListener(tray);
            VKServiceThread = new Thread(vkService);
            VKServiceThread.start();
        }catch (Error err){
            err.printStackTrace();
        }


        while (true){}

    }

}