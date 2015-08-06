package com.peanutwolf;

import com.sun.awt.AWTUtilities;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.*;

/**
 * Created by vigursky on 31.07.2015.
 */

public class MyJWindowTray implements MyMailListener, MyVKListener{
    private JWindow window;
    private static MyJWindowTray tray = new MyJWindowTray();
    public static MyJWindowTray getInstance(){return tray;}

    private Map<MessageID, MyJWindowNode> unreadMsgNumberContainer;
    private Rectangle maxBounds;

    private MyJWindowTray(){
        GraphicsEnvironment ge;

        window = new JWindow();
        unreadMsgNumberContainer = new TreeMap<>();
        window.setAlwaysOnTop(true);
        window.getContentPane().setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        AWTUtilities.setWindowOpacity(window, 0.3f);
        window.setVisible(true);

        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        maxBounds = ge.getMaximumWindowBounds();
    }

    @Override
    public void mailReceived(MyMailEvent event) {
        int newMsgCount = event.getUnreadMsgCount();
        MyJWindowNode mailNode = unreadMsgNumberContainer.get(MessageID.MAIL_ID);

        if(mailNode == null){
            mailNode = new MailJWindowNode();
            unreadMsgNumberContainer.put(mailNode.getMessageID(), mailNode);
        }
        if(((MailJWindowNode)mailNode).setUnreadMsgNumber(newMsgCount) == true){
            JPanel panel = new JPanel();

            panel.setMinimumSize(new Dimension(320, 80));
            panel.setPreferredSize(new Dimension(320, 80));
            panel.setMaximumSize(new Dimension(Short.MAX_VALUE,
                    Short.MAX_VALUE));

            panel.setOpaque(false);

            window.add(panel);

            window.revalidate();
            window.pack();

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
    private JPanel trayPanel;
    private JLabel trayLabel;

    MailJWindowNode() {
        super(MessageID.MAIL_ID);
        trayPanel = new RoundedPanel();
        trayLabel = new JLabel();

        trayPanel.setName(MessageID.MAIL_ID.toString());

        trayPanel.add(trayLabel);

        trayPanel.setMinimumSize(new Dimension(320, 80));
        trayPanel.setPreferredSize(new Dimension(320, 80));
        trayPanel.setMaximumSize(new Dimension(320, 80));

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
        trayLabel.setText("You have " + unreadMsgNumber + "messages");
        trayPanel.revalidate();

        return trayPanel;
    }

}

class RoundedPanel extends JPanel {

    /** Stroke size. it is recommended to set it to 1 for better view */
    protected int strokeSize = 1;
    /** Color of shadow */
    protected Color shadowColor = Color.black;
    /** Sets if it drops shadow */
    protected boolean shady = false;
    /** Sets if it has an High Quality view */
    protected boolean highQuality = true;
    /** Double values for Horizontal and Vertical radius of corner arcs */
    protected Dimension arcs = new Dimension(20, 20);
    /** Distance between shadow border and opaque panel border */
    protected int shadowGap = 5;
    /** The offset of shadow.  */
    protected int shadowOffset = 4;
    /** The transparency value of shadow. ( 0 - 255) */
    protected int shadowAlpha = 150;

    public RoundedPanel() {
        super();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int shadowGap = this.shadowGap;
        Color shadowColorA = new Color(shadowColor.getRed(),
                shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);
        Graphics2D graphics = (Graphics2D) g;

        //Sets antialiasing if HQ.
        if (highQuality) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

        //Draws shadow borders if any.
        if (shady) {
            graphics.setColor(shadowColorA);
            graphics.fillRoundRect(
                    shadowOffset,// X position
                    shadowOffset,// Y position
                    width - strokeSize - shadowOffset, // width
                    height - strokeSize - shadowOffset, // height
                    arcs.width, arcs.height);// arc Dimension
        } else {
            shadowGap = 1;
        }

        //Draws the rounded opaque panel with borders.
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width - shadowGap,
                height - shadowGap, arcs.width, arcs.height);
        graphics.setColor(getForeground());
        graphics.setStroke(new BasicStroke(strokeSize));
        graphics.drawRoundRect(0, 0, width - shadowGap,
                height - shadowGap, arcs.width, arcs.height);

        //Sets strokes to default, is better.
        graphics.setStroke(new BasicStroke());
    }
}