package com.peanutwolf;

import javax.swing.*;
import java.util.*;

/**
 * Created by vigursky on 31.07.2015.
 */
public class MyJWindowTray implements MyMailListener, MyVKListener{
    private JWindow window;
    private static MyJWindowTray tray = new MyJWindowTray();
    public static MyJWindowTray getInstance(){return tray;}

    private Map<MessageID, MyJWindowNode> unreadMsgNumberContainer;

    private MyJWindowTray(){
        window = new JWindow();
        unreadMsgNumberContainer = new TreeMap<>();
        window.setAlwaysOnTop(true);
        window.getContentPane().setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
    }

    @Override
    public void mailReceived(MyMailEvent event) {
        MyJWindowNode mailNode = unreadMsgNumberContainer.get(MessageID.MAIL_ID);
        if(mailNode == null){
            mailNode = new MailJWindowNode(MessageID.MAIL_ID);
            unreadMsgNumberContainer.put(mailNode.getMessageID(), mailNode);
        }
    }

    @Override
    public void vkMessageReceived(MyVKEvent event) {

    }
}

abstract class MyJWindowNode{
    private MessageID messageID;

    MyJWindowNode(MessageID messageID){
        this.messageID = messageID;
    }

    public MessageID getMessageID(){
        return messageID;
    }
}

class MailJWindowNode extends MyJWindowNode{
    private int unreadMsgNumber = 0;

    MailJWindowNode(MessageID messageID) {
        super(messageID);
    }

    public int getUnreadMsgNumber(){
        return unreadMsgNumber;
    }

    public boolean setUnreadMsgNumber(int unreadMsgNumber){
        boolean result = false;
        if(this.unreadMsgNumber < unreadMsgNumber)
            result = true;
        this.unreadMsgNumber = unreadMsgNumber;

        return  result;
    }


}
