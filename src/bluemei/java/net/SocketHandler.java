 package bluemei.java.net;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import bluemei.java.net.INetMessage;
import bluemei.java.net.INetStateListener;
import bluemei.java.util.Log;

/**
 * Socket处理器
 * @author bluemei
 * @date 2013-5-22
 */
public class SocketHandler extends Thread{

	private Socket socket=null;
	private INetStateListener netListener;
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private boolean isRunning;
	
	private Object synchLock;

	public SocketHandler(Socket socket) throws IOException {
		this.socket=socket;
		
		oos=new ObjectOutputStream(socket.getOutputStream());
		ois=new ObjectInputStream(socket.getInputStream());
		
		isRunning=false;
		synchLock=new Object();
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void setNetListener(INetStateListener netListener) {
		this.netListener = netListener;
	}
	
	public void run(){
		//DataInputStream dataInput=new DataInputStream(socket.getInputStream());
		//String line="this is a line reply from server\n";
		//os.writeUTF(line);
		isRunning=true;
		while(isRunning){
			Log.i("SocketThread","wait data...");
			
			try {
				//接收消息
				INetMessage message = (INetMessage) ois.readObject();
				Log.i("SocketThread","received data:"+message.toString());
				
				//通知收到消息
				if(netListener!=null)
				{
					isRunning=netListener.onReceived(message,this);
					if(!isRunning){
						socket.close();			
						netListener.onClose(socket);
					}
				}
			} catch (Exception e){
				e.printStackTrace();	
				Log.e("SocketThread", e.toString());		
				
				if(netListener!=null){
					isRunning=netListener.onError(e, socket);
					if(socket.isClosed()){					
						netListener.onClose(socket);
					}
				}
			}			
		}		
	}

	/**
	 * @summary 异步发送消息
	 * @author Administrator
	 * @date 2013-5-22
	 * @param msg
	 * @return boolean
	 * @throws IOException 
	 */
	public void send(INetMessage msg) throws IOException {
		oos.writeObject(msg);
	}
	
	/**
	 * @summary 同步发送消息
	 * @author Administrator
	 * @date 2013-5-22
	 * @param msg
	 * @return boolean
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public INetMessage sendSync(INetMessage msg) throws IOException, InterruptedException {
		synchronized(synchLock) {
			INetStateListener old=netListener;
			SynchNetStateListener l=new SynchNetStateListener();		
			netListener=l;
			send(msg);
			synchLock.wait();
			netListener=old;
			return l.result;
		}
	}
	
	class SynchNetStateListener implements INetStateListener{
		INetMessage result=null;
		@Override
		public void onClose(Socket socket) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean onError(Exception e, Socket socket) {
			synchronized (synchLock) {
				synchLock.notify();				
			}
			return true;
		}
		@Override
		public boolean onReceived(INetMessage msg,SocketHandler socketThread) {
			synchronized (synchLock) {
				result=msg;
				synchLock.notify();				
			}
			return true;
		}			
	}
}
