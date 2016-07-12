/**
 * 
 */
package hj.chat.business;

import hj.chat.msg.ChatMessage;
import hj.chat.msg.ResponseMessage;
import hj.chat.net.IChatMessageSender;
import hj.chat.net.NetManager;

import java.util.Observable;
import java.util.Observer;

/**
 * ÀàÃèÊö
 * @author Administrator
 * @date 2013-5-22
 */
public abstract class ChatManager implements Observer {
	
	protected IChatMessageSender sender=null;
	public void setSender(IChatMessageSender netManager) {
		sender=netManager;
	}
	
	public abstract ResponseMessage doMessage(Observable o, ChatMessage arg);
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
