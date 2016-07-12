/**
 * 
 */
package hj.chat.net;


import hj.chat.common.C;
import hj.chat.msg.ChatMessage;
import hj.chat.msg.ResponseMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import bluemei.java.net.INetMessage;
import bluemei.java.net.INetStateListener;
import bluemei.java.net.SocketHandler;
import bluemei.java.util.Log;

/**
 * 网络管理类
 * @author Administrator
 * @date 2013-5-22
 */
public class NetManager extends ChatMessageHandler implements INetStateListener,IChatMessageSender {

	private Map<Socket,SocketHandler> socketHandlerMap;	
	private ServerMessageHandler serverMessageHandler;
	
	
	public NetManager(){
		socketHandlerMap=new HashMap<Socket,SocketHandler>();	
		serverMessageHandler=new ServerMessageHandler();
	}
	
	public void start() throws IOException{
		ServerSocket serverSocket=new ServerSocket(C.Integers.DEFAULT_SERVER_PORT);
		//serverSocket.setSocketFactory(new NetServiceFactory());
		
		while(true){
			Log.i(C.TAG,"wait connect at port "+serverSocket.getLocalPort()+"...");
			Socket socket=serverSocket.accept();
			Log.i(C.TAG,"socket connected:"+socket.getRemoteSocketAddress());
			add(socket);
		}
	}
	/**
	 * @summary 方法描述
	 * @author Administrator
	 * @date 2013-5-22
	 * @param socket void
	 * @throws IOException 
	 */
	public void add(Socket socket) throws IOException {
		SocketHandler thread=new SocketHandler(socket);
		thread.setNetListener(this);
		socketHandlerMap.put(socket,thread);
		thread.start();		
	}
	
	/* (non-Javadoc)
	 * @see bluemei.java.net.INetStateListener#onClose(java.net.Socket)
	 */
	@Override
	public void onClose(Socket socket) {
		// TODO Auto-generated method stub
		socketHandlerMap.remove(socket);
	}
	/* (non-Javadoc)
	 * @see bluemei.java.net.INetStateListener#onError(java.lang.Exception, java.net.Socket)
	 */
	@Override
	public boolean onError(Exception e, Socket socket) {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see bluemei.java.net.INetStateListener#onReceived(bluemei.java.net.INetMessage)
	 */
	@Override
	public boolean onReceived(INetMessage msg,SocketHandler socketHandler) {
		if(msg instanceof ChatMessage)
		{
			ChatMessage chatMsg=(ChatMessage) msg;
			if(verify(chatMsg,socketHandler)){
				//分发消息: 文本聊天,图片聊,语音聊,群聊...
				super.notifyChatMessage(chatMsg);
			}
			else
				return false;
		}
		else
		{
			ResponseMessage response=serverMessageHandler.doMessage(msg, socketHandler);
			try {
				if(response==null)
					response=new ResponseMessage(C.Integers.STATUS_UNSUPPORT_REQUEST,"unsupport");
			
				socketHandler.send(response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * @summary 验证登录
	 * @author Administrator
	 * @date 2013-5-23
	 * @param msg
	 * @param socketThread
	 * @return boolean
	 */
	private boolean verify(ChatMessage msg, SocketHandler socketHandler) {
		return serverMessageHandler.findLoginedUser(msg.getFromUserId())==socketHandler;
	}


	/* (non-Javadoc)
	 * @see hj.chat.net.IChatMessageSender#send(hj.chat.data.ChatMessage)
	 */
	@Override
	public boolean send(ChatMessage msg) throws IOException {
		long toId = msg.getToUserId();
		SocketHandler socket = serverMessageHandler.findLoginedUser(toId);
		if(socket!=null)
		{
			socket.send(msg);

			return true;
		}
		else
		{
			//对方不在线
			return false;
		}
	}

	

	/* (non-Javadoc)
	 * @see hj.chat.net.NetMessageMap#onResponse(hj.chat.data.ResponseMessage)
	 */
	@Override
	public void onResponse(ResponseMessage response) {
		try {
			send(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			Log.e(C.TAG, e.toString());		
		}
	}
}
