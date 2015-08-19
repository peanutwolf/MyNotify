package com.peanutwolf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by vigursky on 14.08.2015.
 */
class MyJFrameTrayMouseListener implements MouseListener {
    private final static int clickedDelay = 5;
    private Timer compClickedTimer;


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

}