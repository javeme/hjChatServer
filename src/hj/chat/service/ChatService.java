/**
 * 
 */
package hj.chat.service;

import hj.chat.business.TextMessageManager;
import hj.chat.common.C;
import hj.chat.msg.ChatMessage;
import hj.chat.msg.TextMessage;
import hj.chat.net.NetManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import bluemei.java.util.Log;

/**
 * 聊天服务端入口
 * @author bluemei
 * @date 2013-5-22
 */
public class ChatService {
	
	private NetManager netManager;
	private TextMessageManager messageManager;
	
	public ChatService(){
		netManager=new NetManager();
		messageManager=new TextMessageManager();
		
		netManager.registerObserver(TextMessage.NET_ID, messageManager);
		messageManager.setSender(netManager);
	}

	public void start() throws IOException{
		netManager.start();
	}
	
	/**
	 * @summary 主方法
	 * @author Administrator
	 * @date 2013-5-22
	 * @param args void
	 */
	public static void main(String[] args) {
		
		try {
			new ChatService().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		ChatMessage m=new TextMessage();
		try {
			FileOutputStream fos=new FileOutputStream("obj");
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			oos.writeObject(m);
			
			FileInputStream fis=new FileInputStream("obj");
			ObjectInputStream ois=new ObjectInputStream(fis);
			Object o=ois.readObject();
			Log.i("read object",o.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}	
}
