package bluemei.java.util.db;

import java.sql.Connection;

public interface ConnectDbAble {	
	public void setDbName(String name);
	public Connection getConnection() throws Exception;
	public void returnConnection(Connection con);
}
