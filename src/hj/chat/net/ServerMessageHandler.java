/**
 * 
 */
package hj.chat.net;

import hj.chat.business.FriendManager;
import hj.chat.business.LoginManager;
import hj.chat.common.C;
import hj.chat.msg.GetFriendInfoList;
import hj.chat.msg.IdMessage;
import hj.chat.msg.LoginMessage;
import hj.chat.msg.ResponseMessage;
import hj.chat.msg.UnloginMessage;

import java.util.HashMap;
import java.util.Map;

import bluemei.java.net.INetMessage;
import bluemei.java.net.SocketHandler;

/**
 * ÀàÃèÊö
 * @author Administrator
 * @date 2013-6-2
 */
public class ServerMessageHandler {

	protected LoginManager loginManager;
	protected FriendManager friendManager;
	protected Map<Long,SocketHandler> loginUserMap;
	
	ServerMessageHandler()
	{		
		loginUserMap=new HashMap<Long, SocketHandler>();
		
		loginManager=new LoginManager();
		friendManager=new FriendManager();
	}
	
	public ResponseMessage doMessage(INetMessage msg, SocketHandler socketHandler)
	{
		ResponseMessage response=null;
		int id=(int) msg.getNetId();
		if(id==LoginMessage.NET_ID)//µÇÂ¼
		{
			response=doLogin((LoginMessage)msg,socketHandler);
		}		
		else if(id==UnloginMessage.NET_ID)//×¢Ïú
		{
			UnloginMessage unloginMessage=(UnloginMessage)msg;
			response=doUnLogin(unloginMessage,socketHandler);			
		}
		/*else if(id==RegisterMessage.NET_ID)//×¢²á
		{
			;
		}*/
		else if(msg instanceof IdMessage)
		{
			IdMessage idMessage=(IdMessage) msg;
			if(!isLogined(idMessage.getId()))//ÑéÖ¤µÇÂ¼
				response=new ResponseMessage(C.Integers.STATUS_NOTLOGIN,C.Strings.NOTLOGIN);
			else{
				if(id==GetFriendInfoList.NET_ID)
				{	
					GetFriendInfoList fmsg = (GetFriendInfoList)msg;
					response=friendManager.getFriendInfoList(fmsg);					
				}
			}
		}
		
		return response;
	}
	
	/**
	 * @summary µÇÂ¼
	 * @author Administrator
	 * @param socketHandler 
	 * @date 2013-6-2 void
	 */
	private ResponseMessage doLogin(LoginMessage loginMessage,SocketHandler socketHandler) {
		ResponseMessage response=null;
		long userId=loginManager.doLogin(loginMessage);
		if(userId>0)
		{
			addLoginedUser(userId,socketHandler);
			response=new ResponseMessage(C.Integers.STATUS_SUCCESS,C.Strings.LOGIN_SUCCESS);
			response.setToUserId(userId);
		}
		else{
			response=new ResponseMessage(C.Integers.STATUS_FAILD,"");
		}
		return response;
	}

	/**
	 * @summary ×¢Ïú
	 * @author Administrator
	 * @date 2013-6-2
	 * @param unloginMessage
	 * @param socketHandler
	 * @return ResponseMessage
	 */
	private ResponseMessage doUnLogin(UnloginMessage unloginMessage,
			SocketHandler socketHandler) {

		ResponseMessage response=null;
		SocketHandler handler=findLoginedUser(unloginMessage.getId());
		if(handler==socketHandler)
		{
			if(unloginMessage.getNetId()==UnloginMessage.NET_ID)
			{
				if(loginManager.doUnlogin(unloginMessage))
				{
					removeLoginedUser(unloginMessage.getId());

					response=new ResponseMessage(C.Integers.STATUS_SUCCESS,"");
				}
				else{
					response=new ResponseMessage(C.Integers.STATUS_FAILD,"");
				}
			}
		}
		return response;
	}

	/**
	 * @summary Ìí¼ÓµÇÂ¼ÓÃ»§
	 * @author Administrator
	 * @date 2013-5-23
	 * @param fromUserId
	 * @param socketThread void
	 */
	public void addLoginedUser(long userId, SocketHandler socketThread) {
		this.loginUserMap.put(userId, socketThread);
	}

	/**
	 * @summary ÒÆ³ý
	 * @author Administrator
	 * @date 2013-5-25
	 * @param fromUserId void
	 */
	private SocketHandler removeLoginedUser(long userId) {
		return loginUserMap.remove(userId);
	}
	
	public SocketHandler findLoginedUser(long userId)
	{
		return loginUserMap.get(userId);		
	}
	
	public boolean isLogined(long userId)
	{
		return findLoginedUser(userId)!=null;		
	}
}
