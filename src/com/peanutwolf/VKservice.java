package com.peanutwolf;

import java.io.IOError;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vigursky on 27.07.2015.
 */
public class VKservice implements Runnable {
    private List<MyVKListener> _listeners = new ArrayList<MyVKListener>();
    VKProtoPort vkProtoPort;
    int unreadMsgNumber;

    public synchronized void addEventListener(MyVKListener listener){
        _listeners.add(listener);
    }

    public synchronized void removeEventListener(MyVKListener listener){
        _listeners.remove(listener);
    }

    private void fireEvent(){
        MyVKEvent event = new MyVKEvent(this);
        Iterator<MyVKListener> iterator = _listeners.iterator();
        while(iterator.hasNext()){
            iterator.next().vkMessageReceived(event);
        }
    }

    VKservice(String email, String pass) throws Error {
        this.vkProtoPort = new VKProtoPort(email, pass);
        try{
            this.vkProtoPort.connect();
        }catch (Error err){
            throw err;
        }
    }

    @Override
    public void run() {
        int unreadMsgNumber_tmp;
        while(true){
            unreadMsgNumber_tmp = vkProtoPort.getUnreadMessageCount();
            if(unreadMsgNumber_tmp != unreadMsgNumber){
                unreadMsgNumber = unreadMsgNumber_tmp;
                this.fireEvent();
                System.out.println("You have " + unreadMsgNumber + " unread messages in VK");
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
