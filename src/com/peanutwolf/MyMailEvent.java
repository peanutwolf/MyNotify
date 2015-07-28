package com.peanutwolf;

/**
 * Created by vigursky on 23.07.2015.
 */

class MyMailEvent extends java.util.EventObject {
    private Object source;

    public MyMailEvent(Object source) {
        super(source);
        this.source = source;
    }

    public int getUnreadMsgCount(){
        if(MailService.class.isInstance(source))
            return ((MailService)source).unreadMsgNumber;
        else
            return -1;
    }

    public String getMailAddress(){
        if(MailService.class.isInstance(source))
            return ((MailService)source).getEmail();
        else
            return null;
    }


}