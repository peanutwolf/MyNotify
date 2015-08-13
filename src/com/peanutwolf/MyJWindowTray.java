package com.peanutwolf;

import com.sun.awt.AWTUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
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
        try {
            BufferedImage img = ImageIO.read(new URL("http://pre02.deviantart.net/98b8/th/pre/i/2012/242/1/c/sunny_leaves_background_texture_by_donnamarie113-d5d0txr.jpg"));
            trayPanel = new RoundedPanel(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
       // trayPanel = new RoundedPanel();
        trayLabel = new JLabel();

        trayPanel.setName(MessageID.MAIL_ID.toString());

        trayPanel.add(trayLabel);

        trayPanel.setBackground(Color.YELLOW);

        trayPanel.setMinimumSize(new Dimension(320, 80));
        trayPanel.setPreferredSize(new Dimension(320, 80));
        trayPanel.setMaximumSize(new Dimension(320, 80));

        trayPanel.addMouseListener(new MyRoundedPanelMouseListener());

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

class VKJWindowNode extends MyJWindowNode{
    private int unreadMsgNumber = 0;
    private JPanel trayPanel;
    private JLabel trayLabel;

    VKJWindowNode() {
        super(MessageID.VK_ID);
        try {
            BufferedImage img = ImageIO.read(new URL("http://konstantinmartynov.ru/sites/default/files/images/vk.jpg"));
            trayPanel = new RoundedPanel(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        trayLabel = new JLabel();

        trayPanel.setName(MessageID.VK_ID.toString());

        trayPanel.add(trayLabel);

        trayPanel.setBackground(Color.YELLOW);

        trayPanel.setMinimumSize(new Dimension(320, 80));
        trayPanel.setPreferredSize(new Dimension(320, 80));
        trayPanel.setMaximumSize(new Dimension(320, 80));

        trayPanel.addMouseListener(new MyRoundedPanelMouseListener());

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
        trayLabel.setText("You have " + unreadMsgNumber + " messages in vk");
        trayPanel.revalidate();
        trayPanel.setMinimumSize(new Dimension(320, 80));
        trayPanel.setPreferredSize(new Dimension(320, 80));
        trayPanel.setMaximumSize(new Dimension(320, 80));
        trayPanel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255));

        return trayPanel;
    }

}

class RoundedPanel extends JPanel
{
    protected int strokeSize = 1;
    protected Color _shadowColor = Color.BLACK;
    protected boolean shadowed = false;
    protected boolean _highQuality = true;
    protected Dimension _arcs = new Dimension(30, 30);
    protected int _shadowGap = 5;
    protected int _shadowOffset = 4;
    protected int _shadowAlpha = 150;
    protected float composite = 1.0f;

    protected Color _backgroundColor = Color.LIGHT_GRAY;
    protected BufferedImage image = null;
    private Color c;

    public RoundedPanel(BufferedImage img)
    {
        super();
        setOpaque(false);

        if(img != null)
        {
            image = img;
        }
    }

    @Override
    public void setBackground(Color c)
    {
        float c1 = (float) c.getAlpha();
        float c2 = 1.0f/255;
        float c3 = c1 * c2;
        if(_backgroundColor != null &&(_backgroundColor.getAlpha() != c.getAlpha())){
            composite = (1.0f/255) * (float)c.getAlpha();
        }
        _backgroundColor = c;
    }

    @Override
    public Color getBackground(){
        return _backgroundColor;
    }

    private void setAlphaComposite(float composite){
        this.composite = composite;
    }

    private float getAlphaComposite(){
        return composite;
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int shadowGap = this._shadowGap;
        Color shadowColorA = new Color(_shadowColor.getRed(), _shadowColor.getGreen(), _shadowColor.getBlue(), _shadowAlpha);
        Graphics2D graphics = (Graphics2D) g;

        if(_highQuality)
        {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        if(shadowed)
        {
            graphics.setColor(shadowColorA);
            graphics.fillRoundRect(_shadowOffset, _shadowOffset, width - strokeSize - _shadowOffset,
                    height - strokeSize - _shadowOffset, _arcs.width, _arcs.height);
        }
        else
        {
            _shadowGap = 1;
        }

        RoundRectangle2D.Float rr = new RoundRectangle2D.Float(0, 0, (width - shadowGap), (height - shadowGap), _arcs.width, _arcs.height);

        Shape clipShape = graphics.getClip();

        if (image == null)
        {
            graphics.setColor(_backgroundColor);
            graphics.fill(rr);
        }
        else
        {
            RoundRectangle2D.Float rr2 =  new RoundRectangle2D.Float(0, 0, (width - strokeSize - shadowGap), (height - strokeSize - shadowGap), _arcs.width, _arcs.height);

            graphics.setClip(rr2);
            graphics.setComposite(AlphaComposite.SrcOver.derive(composite));
            graphics.drawImage(this.image, 0, 0, null);
            graphics.setClip(clipShape);
        }

        graphics.setColor(getForeground());
        graphics.setStroke(new BasicStroke(strokeSize));
        graphics.draw(rr);
        graphics.setStroke(new BasicStroke());
    }
}

class MyRoundedPanelMouseListener implements MouseListener{
    Timer timer;

    @Override
    public void mouseClicked(MouseEvent e) {
        MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(e.getComponent(), e, e.getComponent().getParent());
        e.getComponent().getParent().dispatchEvent(convertMouseEvent);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Component comp = (Component)e.getSource();

        if(timer == null)
            timer = new Timer(50, new MyJFrameTrayActionListener(comp));

        if (timer.isRunning() && !timer.getActionCommand().equals("ComponentFadeout"))
            return;
        else if (timer.isRunning() && timer.getActionCommand().equals("ComponentFadeout"))
            timer.stop();
        timer.setInitialDelay(0);
        timer.setDelay(50);
        timer.setActionCommand("ComponentFadein");
        timer.start();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(timer == null)
            return;
        if(timer.isRunning() && !timer.getActionCommand().equals("ComponentFadein"))
            return;
        else if(timer.isRunning() && timer.getActionCommand().equals("ComponentFadein"))
            timer.stop();
        timer.setInitialDelay(0);
        timer.setDelay(50);
        timer.setActionCommand("ComponentFadeout");
        timer.start();
    }
}