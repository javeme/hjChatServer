/**
 * 
 */
package hj.chat.msg;

/**
 * ע����¼
 * @author Administrator
 * @date 2013-5-23
 */
public class UnloginMessage extends LoginMessage {

	public static final long NET_ID = 4;//��Ϣ��ʶ,�����ظ�
		
	@Override
	public long getNetId() {
		return NET_ID;
	}
}
