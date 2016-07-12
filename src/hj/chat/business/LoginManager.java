/**
 * 
 */
package hj.chat.business;

import hj.chat.common.C;
import hj.chat.msg.ChatMessage;
import hj.chat.msg.LoginMessage;
import hj.chat.msg.ResponseMessage;
import hj.chat.msg.UnloginMessage;

import java.util.Observable;

import bluemei.java.util.Log;

/**
 * 登录管理器
 * @author Administrator
 * @date 2013-5-22
 */
public class LoginManager extends ChatManager{

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @summary 登录
	 * @author Administrator
	 * @date 2013-5-22
	 * @param netData void
	 */
	public long doLogin(LoginMessage message) {
		Log.i(C.TAG,"user login:"+message.getUsername());
		//数据库操作
		// TODO Auto-generated method stub
		
		if(message.getUsername().equals("hj") && message.getPassword().equals("123"))
			return 365098257;
		if(message.getUsername().equals("bluemei") && message.getPassword().equals("123"))
			return 379743616;
		
		return 0;
	}

	/**
	 * @summary 注销
	 * @author Administrator
	 * @date 2013-5-23
	 * @param chatMessage void
	 */
	public boolean doUnlogin(UnloginMessage message) {
		// TODO Auto-generated method stub
		;
		return false;
	}

	/* (non-Javadoc)
	 * @see hj.chat.business.ChatManager#doMessage(java.util.Observable, hj.chat.data.ChatMessage)
	 */
	@Override
	public ResponseMessage doMessage(Observable o, ChatMessage arg) {
		// TODO Auto-generated method stub
		return null;
	}

}
