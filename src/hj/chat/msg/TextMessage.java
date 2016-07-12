/**
 * 
 */
package hj.chat.msg;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * �ı���Ϣ
 * @author Administrator
 * @date 2013-5-22
 */
public class TextMessage extends ChatMessage {

	public static final long NET_ID = 1;//��Ϣ��ʶ,�����ظ�
	
	private String content;
	
	public TextMessage()
	{
		content="";
	}
		
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		
		sb.append("TextMessage:").append(super.toString()).append(",");
		sb.append("content:").append(content);
		
		return sb.toString();
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		super.readExternal(in);
		content=in.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeUTF(content);
	}

	/* (non-Javadoc)
	 * @see bluemei.java.net.INetMessage#getNetId()
	 */
	@Override
	public long getNetId() {
		return NET_ID;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}
	
}
