package com.peanutwolf;

import javax.comm.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * Created by vigursky on 23.07.2015.
 */
class MyMailDevice implements MyMailListener{
    CommPort myCom;
    InputStream inp;
    OutputStream out;
    MyTrayIcon tray;
    MyJFrameTray frame;

    public MyMailDevice() {
        this.openCommPort("COM2");
        this.InitComIOStreams();
        tray = MyTrayIcon.getInstance();
        frame = MyJFrameTray.getInstance();
    }

    void openCommPort(String com){
        CommPortIdentifier commId;
        Enumeration commList = CommPortIdentifier.getPortIdentifiers();

        while(commList.hasMoreElements()){
            commId = (CommPortIdentifier)commList.nextElement();
            if(commId.getName().equals(com)){
                try {
                    myCom = commId.open("MailDevice", 1000);
                    ((SerialPort)myCom).setSerialPortParams(115200,  SerialPort.DATABITS_8 , SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                } catch (PortInUseException e) {
                    e.printStackTrace();
                } catch (UnsupportedCommOperationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void InitComIOStreams(){
        if(myCom == null)
            return;
        try {
            inp = myCom.getInputStream();
            out = myCom.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mailReceived(MyMailEvent event) {

        int mailCount = event.getUnreadMsgCount();

        System.out.println("Number of mails " + mailCount);
//        tray.displayTrayMessage("Number of mails "+ mailCount);

        try {
            if(out != null)
                out.write(("new_mail:" + mailCount + ":").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}