/**
 * 
 */
package hj.chat.net;

import hj.chat.business.ChatManager;
import hj.chat.common.C;
import hj.chat.msg.ChatMessage;
import hj.chat.msg.ResponseMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


/**
 * 网络消息映射
 * @author bluemei
 * @date 2013-5-22
 */

public abstract class ChatMessageHandler extends Observable{

	protected Map<Long,ChatManager> observerMap;
	
	public ChatMessageHandler(){
		observerMap=new HashMap<Long,ChatManager>();
	}
	
	public Observer registerObserver(long netId, ChatManager obs){
		return observerMap.put(netId, obs);
	}
	
	public Observer unregisterObserver(Long netId){
		return observerMap.remove(netId);
	}

	public abstract void onResponse(ResponseMessage response);
	
	/**
	 * @summary 通知收到消息
	 * @author Administrator
	 * @date 2013-5-22
	 * @param netData void
	 */
	public void notifyChatMessage(ChatMessage message) {
		super.setChanged();
		super.notifyObservers(message);//通知所有观察者(仅用于监听网络消息)
		
		ResponseMessage response=null;
		long key = message.getNetId();
		if(observerMap.containsKey(key))//消息处理器
			response=observerMap.get(key).doMessage(this, message);	//通知消息处理者	
		else
			response=new ResponseMessage(C.Integers.STATUS_UNSUPPORT_REQUEST ,
					C.Strings.UNSUPPORT_REQUEST);
		response.setToUserId(message.getFromUserId());
		
		onResponse(response);
	}

}