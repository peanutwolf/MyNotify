package com.peanutwolf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by vigursky on 14.08.2015.
 */


public class RoundedPanel extends JPanel
{
    protected int strokeSize = 1;
    protected Color _shadowColor = Color.BLACK;
    protected boolean shadowed = false;
    protected boolean _highQuality = true;
    protected Dimension _arcs = new Dimension(30, 30);
    protected int _shadowGap = 0;
    protected int _shadowOffset = 0;
    protected int _shadowAlpha = 0;
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
            this.image = img;
        }
    }

    public RoundedPanel()
    {
        super();
        setOpaque(false);
    }

    public void setBackgroundImage(BufferedImage img){
        this.image = img;
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

        graphics.setColor(new Color(0,0,0,0));

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

//        graphics.setColor(getForeground());
//        graphics.setStroke(new BasicStroke(strokeSize));
        graphics.draw(rr);
//        graphics.setStroke(new BasicStroke());
    }
}

class MyRoundedPanelMouseListener implements MouseListener {
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