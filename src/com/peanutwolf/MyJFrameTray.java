package com.peanutwolf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by vigursky on 23.07.2015.
 */

enum FrameState{
    VISIBLE,
    SHOWING,
    INVISIBLE
}

enum MessageID{
    MAIL_ID,
    VK_ID,
}

public class MyJFrameTray extends JFrame implements Runnable {
    private JFrame dialog;
    private Rectangle maxBounds;

    private FrameState state = FrameState.INVISIBLE;

    private static MyJFrameTray ourInstance = new MyJFrameTray();

    public static MyJFrameTray getInstance() {
        return ourInstance;
    }

    private MyJFrameTray() {

        dialog = new JFrame();

        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        dialog.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                state = FrameState.INVISIBLE;
                dialog.setVisible(false);

                for(MessageID id : MessageID.values())
                    dialog.getContentPane().remove( id.ordinal());
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
        });

        dialog.setUndecorated(true);

        dialog.setAlwaysOnTop(true);

        dialog.setType(javax.swing.JFrame.Type.UTILITY);

        dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        maxBounds = ge.getMaximumWindowBounds();
    }

    public synchronized void displayMsg(int id, String msg) {

        removeCompFromFrame(id);

        dialog.getContentPane().add(createTrayMsgLabel(id, msg));
        dialog.pack();
        dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 10, 10));
        dialog.setLocation((int) (maxBounds.getWidth() - dialog.getWidth()), (int) maxBounds.getHeight() - dialog.getHeight());
        state = FrameState.VISIBLE;
    }

    private JLabel createTrayMsgLabel(int id, String msg){
        JLabel label;

        System.out.println(MessageID.MAIL_ID.ordinal());

        if(id == MessageID.MAIL_ID.ordinal()){
            label = new JLabel(msg);
            label.setName(MessageID.MAIL_ID.toString());
        }else{
            label = new JLabel("New VK message @for future use@");
            label.setName(MessageID.VK_ID.toString());
        }

        label.setMinimumSize(new Dimension(320, 80));
        label.setPreferredSize(new Dimension(320, 80));
        label.setMaximumSize(new Dimension(Short.MAX_VALUE,
                Short.MAX_VALUE));

        label.setOpaque(true);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }

    private void removeCompFromFrame(int id){
        String id_str = MessageID.values()[id].toString();

        Component [] comp = dialog.getContentPane().getComponents();

        for(int i = 0; i < comp.length; i++){
            if(comp[i].getName().equals(id_str)){
                dialog.getContentPane().remove(i);
            }
        }

    }

    @Override
    public void run() {
        float opacity = 0.0f;

        while(true){
            if(state == FrameState.INVISIBLE){
                opacity = 0.0f;
                dialog.setOpacity(opacity);
            }else if(state == FrameState.VISIBLE){
                dialog.setVisible(true);
                for(opacity = 0; opacity < 1.0; opacity += 0.1){
                    dialog.setOpacity(opacity);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                state = FrameState.SHOWING;
            }else{

            }


        }
    }
}
