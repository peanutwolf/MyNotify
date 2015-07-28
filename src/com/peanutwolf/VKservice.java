package com.peanutwolf;

/**
 * Created by vigursky on 27.07.2015.
 */
public class VKservice implements Runnable {
    VKProtoPort vkProtoPort;
    int unreadMsgNumber;

    VKservice(VKProtoPort vkProtoPort){
        this.vkProtoPort = vkProtoPort;
    }
    @Override
    public void run() {
        int unreadMsgNumber_tmp;
        while(true){
            unreadMsgNumber_tmp = vkProtoPort.getUnreadMessageCount(null);
            if(unreadMsgNumber_tmp != unreadMsgNumber){
                unreadMsgNumber = unreadMsgNumber_tmp;
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
