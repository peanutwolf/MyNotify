package com.peanutwolf;

import java.util.Properties;

import javax.mail.*;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

import com.sun.mail.imap.*;


public abstract class MailProtoPort implements ProtoPort{
	protected Store store;
	protected Folder folder;

	abstract public void initFolderWithListener(String folderName);
	abstract public void initFolder(String folderName);
	abstract public int getUnreadMsgCount();
	abstract public String getEmail();

}

