package com.peanutwolf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by vigursky on 14.08.2015.
 */
class MyJFrameTrayActionListener implements ActionListener {
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
        JPanel upperBorderPanel = MyJFrameTray.upperBorderPanel;
        mainFrame = (JFrame) SwingUtilities.getWindowAncestor(component);

        color = component.getBackground();
        compAlpha = color.getAlpha();
        compHeight = (int)component.getPreferredSize().getHeight();

        if(compHeight - 1 >= minHeight){
            compHeight -= 1;
            component.setMinimumSize(new Dimension(((int)component.getPreferredSize().getWidth()), compHeight));
            component.setPreferredSize(new Dimension(((int)component.getPreferredSize().getWidth()), compHeight));
            component.setMaximumSize(new Dimension(((int) component.getPreferredSize().getWidth()), compHeight));

            upperBorderPanel.setMinimumSize(new Dimension(((int)upperBorderPanel.getPreferredSize().getWidth()), maxHeight-compHeight));
            upperBorderPanel.setPreferredSize(new Dimension(((int)upperBorderPanel.getPreferredSize().getWidth()), maxHeight-compHeight));
            upperBorderPanel.setMaximumSize(new Dimension(((int) upperBorderPanel.getPreferredSize().getWidth()), maxHeight-compHeight));
        }

        if(compAlpha - 4 >= minAlpha){
            component.setBackground(new Color(color.getRed(),color.getGreen(), color.getBlue(), compAlpha-4));
        }else if(compAlpha != minAlpha){
            component.setBackground(new Color(color.getRed(),color.getGreen(), color.getBlue(), minAlpha));
        }

        if(compAlpha == minAlpha && compHeight == minHeight){
            mainFrame.remove(component);
            upperBorderPanel.setMinimumSize(new Dimension(((int)upperBorderPanel.getPreferredSize().getWidth()), 0));
            upperBorderPanel.setPreferredSize(new Dimension(((int)upperBorderPanel.getPreferredSize().getWidth()), 0));
            upperBorderPanel.setMaximumSize(new Dimension(((int) upperBorderPanel.getPreferredSize().getWidth()), 0));
            ((Timer)e.getSource()).stop();
        }

        component.revalidate();
        mainFrame.pack();
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
