package com.peanutwolf;


import java.util.*;


class MailService implements Runnable{
	private List<MyMailListener> _listeners = new ArrayList<MyMailListener>();
	MailProtoPort mailProtoPort;
	int unreadMsgNumber = 0;
	
	public synchronized void addEventListener(MyMailListener listener){
		_listeners.add(listener);
	}
	
	public synchronized void removeEventListener(MyMailListener listener){
		_listeners.remove(listener);
	}
	
	private void fireEvent(){
		MyMailEvent event = new MyMailEvent(this);
		Iterator<MyMailListener> iterator = _listeners.iterator();
		while(iterator.hasNext()){
			iterator.next().mailReceived(event);
		}
	}

	public MailService(String host, String email, String pass, String port) throws Error{
		this.mailProtoPort = new ImapMailProtoPort(host, email, pass, port);
		try {
			this.mailProtoPort.connect();
		}catch (Error err){
			throw err;
		}
		this.mailProtoPort.initFolder("Inbox");
	}

	public String getEmail(){
		return mailProtoPort.getEmail();
	}

	@Override
	public void run() {
		int unreadMsgNumber_tmp;
		while(true){
			unreadMsgNumber_tmp = mailProtoPort.getUnreadMsgCount();
			if(unreadMsgNumber_tmp != unreadMsgNumber){
				unreadMsgNumber = unreadMsgNumber_tmp;
				this.fireEvent();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}










