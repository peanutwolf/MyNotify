package com.peanutwolf;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.Properties;

/**
 * Created by vigursky on 29.07.2015.
 */

class ImapMailProtoPort extends MailProtoPort{
    private String host,email, pass, port;

    public ImapMailProtoPort(String host, String email, String pass, String port){
        this.host = host;
        this.email = email;
        this.pass = pass;
        this.port = port;
    }

    @Override
    public void initFolderWithListener(String folderName) {
        if(!store.isConnected())
            return;
        try {
            this.folder =((IMAPStore)store).getFolder(folderName);
            this.folder.open(Folder.READ_ONLY);
            System.out.println("UnreadMsgs = " + ((IMAPFolder)this.folder).getUnreadMessageCount());
            folder.addMessageCountListener(new MessageCountListener() {

                @Override
                public void messagesRemoved(MessageCountEvent evt) {
                    try {
                        System.out.println(((IMAPFolder)folder).getNewMessageCount() + " " + ((IMAPFolder)folder).getUnreadMessageCount());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void messagesAdded(MessageCountEvent evt) {
                    try {
                        System.out.println(((IMAPFolder)folder).getNewMessageCount() + " " + ((IMAPFolder)folder).getUnreadMessageCount());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public void initFolder(String folderName){
        if(!store.isConnected())
            return;
        try {
            this.folder =((IMAPStore)store).getFolder(folderName);
            this.folder.open(Folder.READ_ONLY);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public int getUnreadMsgCount(){
        if(!store.isConnected()){
            System.out.println("store is not connected");
            return -1;
        }
        try {
            ((IMAPFolder)this.folder).getMessageCount();
            return ((IMAPFolder)this.folder).getUnreadMessageCount();
        } catch (MessagingException e) {
            e.printStackTrace();
            return -1;
        }

    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void connect() throws Error{
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.port", this.port);
        if(this.port.equals("993"))
            props.setProperty("mail.imap.ssl.enable", "true");
        Session session = Session.getInstance(props, null);
        try {
            System.out.println("Connecting to mail server");
            this.store = session.getStore();
            this.store.connect(this.host, this.email, this.pass);
            return;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        throw new Error("Cannot connect to " + this.host);
    }
}