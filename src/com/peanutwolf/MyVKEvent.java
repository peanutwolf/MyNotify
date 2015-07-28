package com.peanutwolf;

/**
 * Created by vigursky on 27.07.2015.
 */
public class MyVKEvent extends java.util.EventObject {
    private Object source;

    public MyVKEvent(Object source) {
        super(source);
        this.source = source;
    }

    public int getUnreadMsgCount(){
        if(VKservice.class.isInstance(source))
            return ((VKservice)source).unreadMsgNumber;
        else
            return -1;
    }

}
