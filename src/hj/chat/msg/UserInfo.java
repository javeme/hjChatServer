package hj.chat.msg;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import bluemei.java.net.INetMessage;



public class UserInfo implements INetMessage{
	public static final long NET_ID = 6;
	
	private long id;
	private int headPicture;
	private String userName;
	private String password;
	private String email;
	
	private boolean sex;
	private int age;
	private String homeAddress;

	private String signText;
	private String interestTotal;

	public UserInfo()
	{
		id=0;
		
		userName="";
		password="";
		email="";
		
		sex=true;
		age=0;
		homeAddress="";

		signText="";
		interestTotal="";
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean getSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	public String getInterestTotal() {
		return interestTotal;
	}
	public void setInterestTotal(String interestTotal) {
		this.interestTotal = interestTotal;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getHeadPicture() {
		return headPicture;
	}
	public void setHeadPicture(int headPicture) {
		this.headPicture = headPicture;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getSignText() {
		return signText;
	}
	public void setSignText(String signText) {
		this.signText = signText;
	}
	@Override
	public long getNetId() {
		// TODO Auto-generated method stub
		return NET_ID;
	}
	@Override
	public void readExternal(ObjectInput input) throws IOException,
			ClassNotFoundException {
		id=input.readLong();
		userName=input.readUTF();
		password=input.readUTF();
		headPicture=input.readInt();
		sex=input.readBoolean();
		age=input.readInt();
		signText=input.readUTF();
		homeAddress=input.readUTF();
		interestTotal=input.readUTF();
	}
	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeLong(id);
		output.writeUTF(userName);
		output.writeUTF(password);
		output.writeInt(headPicture);		
		output.writeBoolean(sex);
		output.writeInt(age);
		output.writeUTF(signText);		
		output.writeUTF(homeAddress);
		output.writeUTF(interestTotal);
	
	}

}
