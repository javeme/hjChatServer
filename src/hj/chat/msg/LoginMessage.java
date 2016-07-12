/**
 * 
 */
package hj.chat.msg;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import bluemei.java.net.INetMessage;

/**
 * 登录描述
 * @author Administrator
 * @date 2013-5-22
 */
public class LoginMessage  implements INetMessage {

	public static final long NET_ID = 3;//消息标识,不可重复
	
	private long id;
	private String username;
	private String password;
	
	public LoginMessage()
	{
		this(0,"");
	}
	
	public LoginMessage(long id,String password)
	{
		this.id=id;
		this.username="";
		this.password=password;
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		//super.readExternal(in);
		id=in.readLong();
		username=in.readUTF();
		password=in.readUTF();
	}


	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		//super.writeExternal(out);
		out.writeLong(id);
		out.writeUTF(username);
		out.writeUTF(password);
	}
	
	/* (non-Javadoc)
	 * @see bluemei.java.net.INetMessage#getNetId()
	 */
	@Override
	public long getNetId() {
		// TODO Auto-generated method stub
		return NET_ID;
	}

}
