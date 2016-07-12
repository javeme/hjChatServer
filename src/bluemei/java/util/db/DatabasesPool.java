package bluemei.java.util.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import bluemei.java.util.Log;


public class DatabasesPool {
	Map<String,DatabasePool> dbConnections=null;
	private static boolean isInit=false;
	private DatabasesPool()
	{
		dbConnections=new HashMap<String,DatabasePool>();
	}
	public void addPool(String dbName) throws ClassNotFoundException, SQLException, IOException
	{
		if(dbConnections.containsKey(dbName))
			return;
		DatabasePool dbPool=new DatabasePool();
		dbPool.initConnections(dbName);
		dbConnections.put(dbName,dbPool);
	}
	public void addPool(String dbName,int minConCount,int maxConCount) throws ClassNotFoundException, SQLException, IOException
	{
		if(dbConnections.containsKey(dbName))
			return;
		DatabasePool dbPool=new DatabasePool(minConCount,maxConCount);
		dbPool.initConnections(dbName);
		dbConnections.put(dbName,dbPool);
	}
	public void addPool(ConDbMsg conDbMsg) throws ClassNotFoundException, SQLException, IOException {
		this.addPool(conDbMsg.dbName, conDbMsg.minConCount, conDbMsg.maxConCount);
	}
	public void initConnections(ConDbMsg conDbMsg) throws SQLException, ClassNotFoundException, IOException
	{
		DatabasePool dbPool=new DatabasePool(conDbMsg.minConCount, conDbMsg.maxConCount);
		dbPool.initConnections(conDbMsg.driver, conDbMsg.url, conDbMsg.user, conDbMsg.passward);
		dbConnections.put(conDbMsg.dbName,dbPool);
	}
	public DatabasePool getPool(String dbName)
	{
		if(dbConnections.containsKey(dbName))
		{		
			return dbConnections.get(dbName);
		}
		else
		{
			//addPool(dbName);
			//return dbConnections.get(dbName);
			Log.e("DatabasesPool.getPool","there is no database named "+dbName);
			return null;
		}
	}
	public void returnOneConnection(String dbName,Connection con)
	{
		DatabasePool dbPool=getPool(dbName);
		dbPool.returnOneConnection(con);
	}
	public void closeAllConnection(String dbName)
	{
		DatabasePool dbPool=getPool(dbName);
		dbPool.closeAllConnection();
	}
	public Set<String> getConnectedDbNames(){
		return dbConnections.keySet();
	}
	public static DatabasesPool getDatabasesPool()
	{
		if(isInit)
			return null;
		isInit=true;
		return new DatabasesPool();
	}
}
