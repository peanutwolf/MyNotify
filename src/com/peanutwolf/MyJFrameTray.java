package com.peanutwolf;

import javax.swing.*;
import javax.swing.text.html.ObjectView;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by vigursky on 23.07.2015.
 */

enum MessageID{
    MAIL_ID,
    VK_ID,
}

public class MyJFrameTray extends JFrame implements Runnable, MyMailListener, MyVKListener {
    private JFrame mainFrame;
    private Rectangle maxBounds;
    private Map<MessageID, MyJWindowNode> unreadMsgNumberContainer;

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
        mainFrame.setBackground(new Color(0, 0, 0, 0));
        mainFrame.setVisible(true);
        mainFrame.getContentPane().addMouseListener(new MyJFrameTrayMouseListener(mainFrame));

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

        System.out.println("DEBUG: event.getUnreadMsgCount() = " + newMsgCount);
        MyJWindowNode mailNode = unreadMsgNumberContainer.get(MessageID.MAIL_ID);

        if(mailNode == null){
            mailNode = new MailJWindowNode();
            unreadMsgNumberContainer.put(mailNode.getMessageID(), mailNode);
        }
        isNewMsg = ((MailJWindowNode)mailNode).setUnreadMsgNumber(newMsgCount);
        panel = ((MailJWindowNode)mailNode).getTrayPanel();
        if(isNewMsg == true){
            //TODO: Add animation
            mainFrame.getContentPane().add(panel);
        }
        else{
            mainFrame.getContentPane().remove(panel);
        }
        mainFrame.pack();
        mainFrame.revalidate();
    }

    @Override
    public void vkMessageReceived(MyVKEvent event) {

    }
}


class MyJFrameTrayMouseListener implements MouseListener{

    private JFrame mainFrame;

    MyJFrameTrayMouseListener(JFrame frame){
        this.mainFrame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point containerPoint = SwingUtilities.convertPoint(mainFrame, e.getPoint(), mainFrame.getContentPane());

        Component component = SwingUtilities.getDeepestComponentAt(
                mainFrame.getContentPane(),
                containerPoint.x,
                containerPoint.y);
        mainFrame.remove(component);
        mainFrame.pack();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
