package hj.chat.msg;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import bluemei.java.net.INetMessage;

public class FriendInfo implements INetMessage{	
	
	private UserInfo userInfo;
	private String remarkName;//±¸×¢ÐÕÃû
	
	public FriendInfo()
	{
		userInfo=null;
	}
	
	public String getUserName() {
		return userInfo.getUserName();
	}

	public int getFriendPicture() {
		return userInfo.getHeadPicture();
	}


	public boolean getSex() {
		return userInfo.getSex();
	}
	
	public String getInterestTotal() {
		return userInfo.getInterestTotal();
	}
	
	public long getId() {
		return userInfo.getId();
	}
	
	
	public String getEmail() {
		return userInfo.getEmail();
	}
	
	public int getAge() {
		return userInfo.getAge();
	}
	
	public String getHomeAddress() {
		return userInfo.getHomeAddress();
	}
	
	public String getSignText() {
		return userInfo.getSignText();
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}
	
	@Override
	public void readExternal(ObjectInput input) throws IOException,
			ClassNotFoundException {
		userInfo=(UserInfo) input.readObject();
		remarkName=input.readUTF();
	}
	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeObject(userInfo);
		output.writeUTF(remarkName);	
	}

	/* (non-Javadoc)
	 * @see bluemei.java.net.INetMessage#getNetId()
	 */
	@Override
	public long getNetId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
