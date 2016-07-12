package hj.chat.msg;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class GetFriendInfoList extends IdMessage  {
	
	public static final long NET_ID = 5;
	
	public GetFriendInfoList()
	{
		super(0);
	}
	
	public GetFriendInfoList(long id)
	{
		super(id);
	}
	
	@Override
	public long getNetId() {
		
		return NET_ID;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		super.readExternal(in);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
	}

}
