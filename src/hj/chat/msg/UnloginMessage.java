/**
 * 
 */
package hj.chat.msg;

/**
 * 注销登录
 * @author Administrator
 * @date 2013-5-23
 */
public class UnloginMessage extends LoginMessage {

	public static final long NET_ID = 4;//消息标识,不可重复
		
	@Override
	public long getNetId() {
		return NET_ID;
	}
}
