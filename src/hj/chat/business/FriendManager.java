/**
 * 
 */
package hj.chat.business;

import hj.chat.common.C;
import hj.chat.msg.ChatMessage;
import hj.chat.msg.FriendInfo;
import hj.chat.msg.GetFriendInfoList;
import hj.chat.msg.ResponseMessage;
import hj.chat.msg.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 *  朋友消息管理器
 * @author Administrator
 * @date 2013-6-2
 */
public class FriendManager extends ChatManager {

	/* (non-Javadoc)
	 * @see hj.chat.business.ChatManager#doMessage(java.util.Observable, hj.chat.msg.ChatMessage)
	 */
	@Override
	public ResponseMessage doMessage(Observable o, ChatMessage arg) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @summary 方法描述
	 * @author Administrator
	 * @date 2013-6-2
	 * @param msg
	 * @return ResponseMessage
	 */
	public ResponseMessage getFriendInfoList(GetFriendInfoList msg) {
		ResponseMessage response=new ResponseMessage();
		
		List<FriendInfo> list=getFriendInfoListOfUser(msg.getId());
		if(list!=null)
		{
			response.setStatus(C.Integers.STATUS_SUCCESS);
			response.setResult(list);
		}
		else
			response.setStatus(C.Integers.STATUS_FAILD);
		return response;
	}


	private List<FriendInfo> getFriendInfoListOfUser(long id) {
		List<FriendInfo> list=new ArrayList<FriendInfo>();
		
		//查找数据库,暂时写死
		FriendInfo f=new FriendInfo();
		UserInfo userInfo=new UserInfo();
		userInfo.setId(379743616);
		userInfo.setUserName("bluemei");
		userInfo.setHeadPicture(0);
		userInfo.setSignText("交谈中请勿轻信汇款、中奖信息、陌生电话，勿使用外挂软件");
		f.setUserInfo(userInfo);
		f.setRemarkName("李章梅");
		list.add(f);
		
		return list;		
	}
}
