/**
 * 
 */
package hj.chat.business;

import hj.chat.common.C;
import hj.chat.msg.ChatMessage;
import hj.chat.msg.ResponseMessage;
import hj.chat.msg.TextMessage;

import java.io.IOException;
import java.util.Observable;

import bluemei.java.util.Log;

/**
 *  ��Ϣ������
 * @author Administrator
 * @date 2013-5-22
 */
public class TextMessageManager extends ChatManager{

	/* ��Ϣ��������
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public ResponseMessage doMessage(Observable o, ChatMessage arg) {
		ResponseMessage response=null;
		if(arg instanceof TextMessage)
		{
			//����������Ϣ
			TextMessage message=(TextMessage) arg;
			Log.i(C.TAG, "�յ��ı���Ϣ:"+message);
			
			try {
				if(sender.send(message))//ֱ��ת��
					response=doResponse(C.Integers.STATUS_SEND_SUCCESS,C.Strings.SEND_SUCCESS);
				else//������(������,�����ݴ浽������)
				{
					// TODO Auto-generated method stub
					;
					response=doResponse(C.Integers.STATUS_SEND_WAIT,C.Strings.SEND_NOT_ONLINE);
				}
			} catch (IOException e) {
				e.printStackTrace();
				
				Log.e(C.TAG, e.toString());			
				response=doResponse(C.Integers.STATUS_SEND_FAILD,C.Strings.SEND_FAILD);
			}
		}
		else
		{
			Log.e(C.TAG, arg+" is not a instance of TextMessage.");
			response=doResponse(C.Integers.STATUS_UNSUPPORT_REQUEST,C.Strings.UNSUPPORT_REQUEST);
		}
		return response;
	}


	/**
	 * @summary ��Ӧ
	 * @author Administrator
	 * @date 2013-5-22
	 * @param sendSuccess
	 * @param sendSuccess2 void
	 */
	private ResponseMessage doResponse(int status, String result) {
		ResponseMessage response=new ResponseMessage(status,result);
		return response;		
	}
}
