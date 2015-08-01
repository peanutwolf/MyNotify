package com.peanutwolf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.SortedMap;

/**
 * Created by vigursky on 31.07.2015.
 */
public class MyJWindowTray implements MyMailListener, MyVKListener{
    private JWindow window;
    private static MyJWindowTray tray = new MyJWindowTray();
    public static MyJWindowTray getInstance(){return tray;}

//    private SortedMap<Integer, >

    private MyJWindowTray(){
        window = new JWindow();
        window.setAlwaysOnTop(true);
        window.getContentPane().setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
    }

    @Override
    public void mailReceived(MyMailEvent event) {

    }

    @Override
    public void vkMessageReceived(MyVKEvent event) {

    }
}
