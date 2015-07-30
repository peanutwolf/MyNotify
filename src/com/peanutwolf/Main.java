package com.peanutwolf;


import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class Main{

    public static void main(String... args){
        Thread VKServiceThread;
        Thread MailServiceThread;
        Thread NotifyServiceThread;
        Thread HardwareDevThread;

        Thread newThread;
        VKservice vkService;
        try {
            vkService= new VKservice("","");
             newThread= new Thread(vkService);
            newThread.start();
        }catch (Error err){
            System.err.println(err.getMessage());
        }

        while (true){

        }

    }

}