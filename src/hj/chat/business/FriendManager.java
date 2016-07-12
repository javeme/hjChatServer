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
 *  ������Ϣ������
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
	 * @summary ��������
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
		
		//�������ݿ�,��ʱд��
		FriendInfo f=new FriendInfo();
		UserInfo userInfo=new UserInfo();
		userInfo.setId(379743616);
		userInfo.setUserName("bluemei");
		userInfo.setHeadPicture(0);
		userInfo.setSignText("��̸���������Ż��н���Ϣ��İ���绰����ʹ��������");
		f.setUserInfo(userInfo);
		f.setRemarkName("����÷");
		list.add(f);
		
		return list;		
	}
}
