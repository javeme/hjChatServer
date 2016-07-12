package bluemei.java.net;

import java.net.Socket;


public interface INetStateListener {
	public boolean onReceived(INetMessage msg, SocketHandler socketThread);
	public boolean onError(Exception e,Socket socket);
	public void onClose(Socket socket);
}
