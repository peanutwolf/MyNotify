package com.peanutwolf;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by vigursky on 23.07.2015.
 */

enum MessageID{
    MAIL_ID,
    VK_ID,
}

public class MyJFrameTray extends JFrame implements Runnable, MyMailListener, MyVKListener {
    private JFrame mainFrame;
    private static Rectangle maxBounds;
    private Map<MessageID, MyJPanelTrayNode> unreadMsgNumberContainer;

    final static JPanel upperBorderPanel = new JPanel();

    private static MyJFrameTray ourInstance = new MyJFrameTray();

    public static MyJFrameTray getInstance() {
        return ourInstance;
    }

    private MyJFrameTray() {
        GraphicsEnvironment ge;

        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setUndecorated(true);
        mainFrame.setAlwaysOnTop(true);
        mainFrame.setType(javax.swing.JFrame.Type.UTILITY);
        mainFrame.getContentPane().setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS));
        mainFrame.setBackground(new Color(128, 128, 0, 0));
        mainFrame.setVisible(true);

        upperBorderPanel.setMinimumSize(new Dimension(320, 0));
        upperBorderPanel.setPreferredSize(new Dimension(320, 0));
        upperBorderPanel.setMaximumSize(new Dimension(320, 0));
        upperBorderPanel.setBackground(new Color(0, 0, 0, 0));
        mainFrame.getContentPane().add(upperBorderPanel);

        mainFrame.getContentPane().addMouseListener(new MyJFrameTrayMouseListener());
        mainFrame.getContentPane().addContainerListener(new MyJFrameTrayContainerListener());

        unreadMsgNumberContainer = new TreeMap<>();

        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        maxBounds = ge.getMaximumWindowBounds();
    }

    @Override
    public void run() {

    }

    @Override
    public void mailReceived(MyMailEvent event) {
        JPanel panel;
        boolean isNewMsg;
        int newMsgCount = event.getUnreadMsgCount();

        System.out.println("DEBUG: Mail event.getUnreadMsgCount() = " + newMsgCount);
        MyJPanelTrayNode mailNode = unreadMsgNumberContainer.get(MessageID.MAIL_ID);

        if(mailNode == null){
            mailNode = new MailJPanelTrayNode();
            unreadMsgNumberContainer.put(mailNode.getMessageID(), mailNode);
        }
        isNewMsg = ((MailJPanelTrayNode)mailNode).setUnreadMsgNumber(newMsgCount);
        panel = ((MailJPanelTrayNode)mailNode).getTrayPanel();
        if(isNewMsg == true){
            mainFrame.getContentPane().add(panel);
        }
        else{
            mainFrame.getContentPane().remove(panel);
        }
        mainFrame.pack();
        mainFrame.revalidate();
        mainFrame.setLocation((int) (maxBounds.getWidth() - mainFrame.getWidth()), (int) maxBounds.getHeight() - mainFrame.getHeight());
    }

    @Override
    public void vkMessageReceived(MyVKEvent event) {
        //TODO: Handle event
        int newMsgCount = event.getUnreadMsgCount();
        JPanel panel;
        boolean isNewMsg;

        System.out.println("DEBUG: VK event.getUnreadMsgCount() = " + newMsgCount);
        MyJPanelTrayNode vkNode = unreadMsgNumberContainer.get(MessageID.VK_ID);

        if(vkNode == null){
            vkNode = new VKJPanelTrayNode();
            unreadMsgNumberContainer.put(vkNode.getMessageID(), vkNode);
        }
        isNewMsg = ((VKJPanelTrayNode)vkNode).setUnreadMsgNumber(newMsgCount);
        panel = ((VKJPanelTrayNode)vkNode).getTrayPanel();
        if(isNewMsg == true){
            mainFrame.getContentPane().add(panel);

        }
        else{
            mainFrame.getContentPane().remove(panel);
        }
        mainFrame.pack();
        mainFrame.revalidate();
        mainFrame.setLocation((int) (maxBounds.getWidth() - mainFrame.getWidth()), (int) maxBounds.getHeight() - mainFrame.getHeight());
    }

    public static Rectangle getMaxBounds(){
        return maxBounds;
    }
}



