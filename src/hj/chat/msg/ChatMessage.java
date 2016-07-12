/**
 * 
 */
package hj.chat.msg;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import bluemei.java.net.INetMessage;

/**
 * ¿‡√Ë ˆ
 * @author Administrator
 * @date 2013-5-22
 */

public abstract class ChatMessage implements INetMessage
{	
	private long toUserId;
	private long fromUserId;
	private Date time;
	private int type;
	private int reserve;	
	
	public ChatMessage()
	{
		time=new Date();
	}
	
	
	public long getToUserId() {
		return toUserId;
	}


	public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}


	public long getFromUserId() {
		return fromUserId;
	}


	public void setFromUserId(long fromUserId) {
		this.fromUserId = fromUserId;
	}


	public Date getTime() {
		return time;
	}


	public void setTime(Date time) {
		this.time = time;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public int getReserve() {
		return reserve;
	}


	public void setReserve(int reserve) {
		this.reserve = reserve;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		
		sb.append("to:").append(toUserId).append(",");
		sb.append("from:").append(fromUserId).append(",");
		sb.append("time:").append(time).append(",");
		sb.append("type:").append(type).append(",");
		sb.append("reserve:").append(reserve);
		
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */	
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		//super.readExternal(in);
		toUserId=in.readLong();
		fromUserId=in.readLong();
		time=new Date(in.readLong());
		type=in.readInt();
		reserve=in.readInt();
	}

	/* (non-Javadoc)
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		//super.writeExternal(out);
		out.writeLong(toUserId);
		out.writeLong(fromUserId);
		out.writeLong(time.getTime());
		out.writeInt(type);
		out.writeInt(reserve);
	}

}