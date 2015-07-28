package com.peanutwolf;


import java.util.*;


class MailService implements Runnable{
	private List<MyMailListener> _listeners = new ArrayList<MyMailListener>();
	MailProtoPort proto;
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

	public MailService(MailProtoPort proto) {
		this.proto = proto;
		this.proto.initFolder("Inbox");
	}

	public String getEmail(){
		return proto.getEmail();
	}

	@Override
	public void run() {
		int unreadMsgNumber_tmp;
		while(true){
			unreadMsgNumber_tmp = proto.getUnreadMsgCount();
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










