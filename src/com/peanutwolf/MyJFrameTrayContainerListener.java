package com.peanutwolf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

/**
 * Created by vigursky on 14.08.2015.
 */
class MyJFrameTrayContainerListener implements ContainerListener {
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

