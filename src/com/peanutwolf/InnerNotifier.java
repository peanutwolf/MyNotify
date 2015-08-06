package com.peanutwolf;

/**
 * Created by vigursky on 23.07.2015.
 */
public class InnerNotifier implements MyMailListener{

    MyTrayIcon tray;
    MyJFrameTray frame;

    InnerNotifier(){
        tray = MyTrayIcon.getInstance();
        frame = MyJFrameTray.getInstance();
    }

    @Override
    public void mailReceived(MyMailEvent event) {
        int mailCount = event.getUnreadMsgCount();
        String email = event.getMailAddress();
        //frame.displayMsg(0, "<html>"+"You have " + mailCount + " new messages <b>" + "in " + email+"</html>");
    }
}
