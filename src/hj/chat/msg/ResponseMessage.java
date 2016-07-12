/**
 * 
 */
package hj.chat.msg;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 类描述
 * @author Administrator
 * @date 2013-5-22
 */
public class ResponseMessage extends ChatMessage {

	public static final long NET_ID = 2;//消息标识,不可重复
	
	private int status;
	private Object result;
	
	public ResponseMessage()
	{
		status=-1;
		result="";
	}
	
	public ResponseMessage(int status,String msg)
	{
		this.status=status;
		this.result=msg;
	}
	
	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public Object getResult() {
		return result;
	}


	public void setResult(Object result) {
		this.result = result;
	}


	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		super.readExternal(in);
		status=in.readInt();
		result=in.readObject();
	}


	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeInt(status);
		out.writeObject(result);
	}
	
	/* (non-Javadoc)
	 * @see bluemei.java.net.INetMessage#getNetId()
	 */
	@Override
	public long getNetId() {
		return NET_ID;
	}

}
