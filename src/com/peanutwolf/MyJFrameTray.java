package com.peanutwolf;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

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
        MyJWindowNode mailNode = unreadMsgNumberContainer.get(MessageID.MAIL_ID);

        if(mailNode == null){
            mailNode = new MailJWindowNode();
            unreadMsgNumberContainer.put(mailNode.getMessageID(), mailNode);
        }
        isNewMsg = ((MailJWindowNode)mailNode).setUnreadMsgNumber(newMsgCount);
        panel = ((MailJWindowNode)mailNode).getTrayPanel();
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
        MyJWindowNode vkNode = unreadMsgNumberContainer.get(MessageID.VK_ID);

        if(vkNode == null){
            vkNode = new VKJWindowNode();
            unreadMsgNumberContainer.put(vkNode.getMessageID(), vkNode);
        }
        isNewMsg = ((VKJWindowNode)vkNode).setUnreadMsgNumber(newMsgCount);
        panel = ((VKJWindowNode)vkNode).getTrayPanel();
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


class MyJFrameTrayMouseListener implements MouseListener{
    private final static int clickedDelay = 5;
    private Component compMouseOn;
    private Timer compClickedTimer;
    private ArrayList<TimedComponent> timedCompArray;

    MyJFrameTrayMouseListener(){
        super();
        timedCompArray = new ArrayList<>();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Component component;
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(e.getComponent());
        Point containerPoint = SwingUtilities.convertPoint(mainFrame, e.getPoint(), mainFrame.getContentPane());

        component = SwingUtilities.getDeepestComponentAt( mainFrame.getContentPane(),containerPoint.x,containerPoint.y);
        if(!JPanel.class.isInstance(component))
            return;
        // TODO : add correct action listener
        compClickedTimer = new Timer(clickedDelay, new MyJFrameTrayActionListener(component));
        compClickedTimer.setActionCommand("ComponentClicked");
        compClickedTimer.start();
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

    private TimedComponent getTimedComp(Component comp){
        TimedComponent element;
        Iterator<TimedComponent> it = timedCompArray.iterator();
        while (it.hasNext()){
            element = it.next();
            if(element.compMatches(comp))
                return element;
        }

        element = new TimedComponent(comp);
        timedCompArray.add(element);

        return element;
    }
}

class MyJFrameTrayActionListener implements ActionListener{
    Component component;
    JFrame mainFrame;
    int maxHeight;
    int maxAlpha;
    int minHeight = 2;
    int minAlpha = 2;

    MyJFrameTrayActionListener(Component comp){
        this.component = comp;
        this.maxHeight = (int)comp.getPreferredSize().getHeight();
        this.maxAlpha = comp.getBackground().getAlpha();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "ComponentAdded": this.componentAdded(e); break;
            case "ComponentClicked": this.componentClicked(e); break;
            case "ComponentFadeout": this.componentFadeout(e); break;
            case "ComponentFadein": this.componentFadein(e); break;
            default: break;
        }
    }

    private void componentAdded(ActionEvent e){
        int compAlpha;
        int compHeight;
        Color color;

        mainFrame = (JFrame) SwingUtilities.getWindowAncestor(component);

        color = component.getBackground();
        compAlpha = color.getAlpha();
        compHeight = (int)component.getPreferredSize().getHeight();

        if(compHeight + 1 <= maxHeight){
            compHeight++;
            component.setMinimumSize(new Dimension(((int)component.getPreferredSize().getWidth()), compHeight));
            component.setPreferredSize(new Dimension(((int)component.getPreferredSize().getWidth()), compHeight));
            component.setMaximumSize(new Dimension(((int)component.getPreferredSize().getWidth()), compHeight));
        }

        if(compAlpha + 4 <= maxAlpha){
            component.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), compAlpha + 4));
        }else if(compAlpha != maxAlpha){
            component.setBackground(new Color(color.getRed(),color.getGreen(), color.getBlue(), maxAlpha));
        }


        if(compAlpha == maxAlpha && compHeight == maxHeight){
            ((Timer)e.getSource()).stop();
            ((Timer)e.getSource()).setActionCommand("ComponentFadeout");
            ((Timer)e.getSource()).setInitialDelay(5000);
            ((Timer)e.getSource()).setDelay(50);
            ((Timer)e.getSource()).start();
        }

        component.revalidate();
        mainFrame.pack();
        mainFrame.revalidate();
        mainFrame.setLocation((int) (MyJFrameTray.getMaxBounds().getWidth() - mainFrame.getWidth()), (int) MyJFrameTray.getMaxBounds().getHeight() - mainFrame.getHeight());
    }

    private void componentClicked(ActionEvent e){
        int compAlpha;
        int compHeight;
        Color color;

        mainFrame = (JFrame) SwingUtilities.getWindowAncestor(component);

        color = component.getBackground();
        compAlpha = color.getAlpha();
        compHeight = (int)component.getPreferredSize().getHeight();

        if(compHeight - 1 >= minHeight){
            compHeight--;
            component.setMinimumSize(new Dimension(((int)component.getPreferredSize().getWidth()), compHeight));
            component.setPreferredSize(new Dimension(((int)component.getPreferredSize().getWidth()), compHeight));
            component.setMaximumSize(new Dimension(((int) component.getPreferredSize().getWidth()), compHeight));
        }

        if(compAlpha - 4 >= minAlpha){
            component.setBackground(new Color(color.getRed(),color.getGreen(), color.getBlue(), compAlpha-4));
        }else if(compAlpha != minAlpha){
            component.setBackground(new Color(color.getRed(),color.getGreen(), color.getBlue(), minAlpha));
        }

        if(compAlpha == minAlpha && compHeight == minHeight){
            mainFrame.remove(component);
            ((Timer)e.getSource()).stop();
        }

        component.revalidate();
        mainFrame.pack();
        mainFrame.revalidate();
        mainFrame.setLocation((int) (MyJFrameTray.getMaxBounds().getWidth() - mainFrame.getWidth()), (int) MyJFrameTray.getMaxBounds().getHeight() - mainFrame.getHeight());
    }

    private void componentFadeout(ActionEvent e){
        int maxFadeout = 150;
        int fadeoutDelta = 5;

        Color c = component.getBackground();

        if(c.getAlpha() > maxFadeout){
            component.setBackground(new Color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha() - fadeoutDelta));
            component.repaint();
            component.revalidate();
        }else{
            ((Timer)e.getSource()).stop();
        }
    }

    private void componentFadein(ActionEvent e){
        int maxFadein = 255;
        int fadeinDelta = 5;

        Color c = component.getBackground();
        //TODO: Make fadein more accurate, check for bound conditions
        if(c.getAlpha() < maxFadein){
            component.setBackground(new Color(c.getRed(),c.getGreen(),c.getBlue(),c.getAlpha() + fadeinDelta));
            component.repaint();
            component.revalidate();
        }else{
            ((Timer)e.getSource()).stop();
        }
    }
}

class MyJFrameTrayContainerListener implements ContainerListener{
    private final static int delay = 5;
    private Component comp;
    private Color compBackground;
    private Dimension componentDimension;
    private Timer timer;

    @Override
    public void componentAdded(ContainerEvent e) {

        comp = e.getChild();
        compBackground = comp.getBackground();
        componentDimension = comp.getPreferredSize();
        timer = new Timer(delay, new MyJFrameTrayActionListener(comp));
        comp.setBackground(new Color(compBackground.getRed(), compBackground.getGreen(), compBackground.getBlue(), 0));
        comp.setMinimumSize(new Dimension(((int) componentDimension.getWidth()), 2));
        comp.setPreferredSize(new Dimension(((int) componentDimension.getWidth()), 2));
        comp.setMaximumSize(new Dimension(((int) componentDimension.getWidth()), 2));
        timer.setActionCommand("ComponentAdded");
        timer.start();
    }

    @Override
    public void componentRemoved(ContainerEvent e) {

    }
}


class TimedComponent{
    private Component comp;
    private Timer timer;

    TimedComponent(Component comp){
        this.comp = comp;
        this.timer = new Timer(50, new MyJFrameTrayActionListener(this.comp));
    }

    public boolean compMatches(Component comp){
        return comp.equals(this.comp);
    }

    public Timer getTimer(){
        return timer;
    }
}