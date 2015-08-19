package com.peanutwolf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by vigursky on 14.08.2015.
 */
class MailJPanelTrayNode extends MyJPanelTrayNode {
    private int unreadMsgNumber = 0;
    private BufferedImage img = null;

    MailJPanelTrayNode() {
        super(MessageID.MAIL_ID);
        if(RoundedPanel.class.isInstance(super.trayPanel)){
            try {
                img = ImageIO.read(new File("mail_small.jpg"));
                ((RoundedPanel) super.trayPanel).setBackgroundImage(img);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public JPanel getTrayPanel(){
        Color color = trayPanel.getBackground();
        trayLabel.setText("You have " + unreadMsgNumber + " messages in mail box");
        trayPanel.revalidate();
        trayPanel.setMinimumSize(new Dimension(320, 80));
        trayPanel.setPreferredSize(new Dimension(320, 80));
        trayPanel.setMaximumSize(new Dimension(320, 80));
        trayPanel.setBackground(new Color(color.getRed(),color.getGreen(),color.getBlue(), 255));

        return trayPanel;
    }

}