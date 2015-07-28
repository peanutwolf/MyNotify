package com.peanutwolf;

import java.util.Properties;

import javax.mail.*;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

import com.sun.mail.imap.*;


public abstract class MailProtoPort {
	protected Store store;
	protected Folder folder;
	
	abstract protected void initConnectService();
	abstract public void initFolderWithListener(String folderName);
	abstract public void initFolder(String folderName);
	abstract public int getUnreadMsgCount();
	abstract public String getEmail();
}

class ImapMailProtoPort extends MailProtoPort{
	private String host,email, pass, port;
	
	public ImapMailProtoPort(String host, String email, String pass, String port){
		this.host = host;
		this.email = email;
		this.pass = pass;
		this.port = port;
		this.initConnectService();
	}

	@Override
	protected void initConnectService() {
		Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.port", this.port);
        if(this.port.equals("993"))
        	props.setProperty("mail.imap.ssl.enable", "true");        
        Session session = Session.getInstance(props, null);
        try {
			System.out.println("Connecting to mail server");
			super.store = session.getStore();
			this.store.connect(this.host, this.email, this.pass);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} 
		
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

}