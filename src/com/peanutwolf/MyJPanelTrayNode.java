package com.peanutwolf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by vigursky on 14.08.2015.
 */
abstract class MyJPanelTrayNode {
    private MessageID messageID;
    protected JPanel trayPanel;
    protected JLabel trayLabel;

    MyJPanelTrayNode(MessageID messageID){
        this.messageID = messageID;

        trayPanel = new RoundedPanel();

        trayLabel = new JLabel();

        trayPanel.setName(MessageID.VK_ID.toString());

        trayPanel.add(trayLabel);

        trayPanel.setBackground(Color.lightGray);

        trayPanel.setMinimumSize(new Dimension(320, 80));
        trayPanel.setPreferredSize(new Dimension(320, 80));
        trayPanel.setMaximumSize(new Dimension(320, 80));

        trayPanel.addMouseListener(new MyRoundedPanelMouseListener());
        trayPanel.setDoubleBuffered(true);
    }

    public MessageID getMessageID(){
        return messageID;
    }

    protected JPanel getTrayPanel(){
        return trayPanel;
    }

}
