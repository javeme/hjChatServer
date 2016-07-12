package hj.chat.msg;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import bluemei.java.net.INetMessage;

public abstract class IdMessage implements INetMessage  {
	private long id;
	
	public IdMessage()
	{
		this(0);
	}
	
	public IdMessage(long id) {
		this.id=id;
		
	}
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		id=in.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(id);
	}
}
