package bluemei.java.util.db;

import java.sql.Connection;

import bluemei.java.util.Log;

public class DbManager implements ConnectDbAble
{

	private DatabasesPool dbsPool=null;
	private Log log=null;
	private String currentDb;
	
	public DbManager(){
		
	}
	public DbManager(Log log){
		setLog(log);
	}

	@Override
	public void setDbName(String name)
	{
		this.currentDb=name;
	}
	@Override
	public Connection getConnection() throws Exception
	{		
		return dbsPool.getPool(currentDb).getOneConnection();
	}

	@Override
	public void returnConnection(Connection con)
	{
		dbsPool.getPool(currentDb).returnOneConnection(con);
	}
	public DatabasesPool getDbsPool()
	{
		return dbsPool;
	}
	public void setLog(Log log)
	{
		this.log=log;
	}
	
	public void initDbPool(ConDbMsg[] conDbMsgs) throws Exception {
		dbsPool=DatabasesPool.getDatabasesPool();	
		for(int i=0;i<conDbMsgs.length;i++)
		{
			String msg="init connections of '"+conDbMsgs[i].dbName+"'";
			try {		
				dbsPool.addPool(conDbMsgs[i]);
				Log.i("DbManager.initDbPool",msg+" success.");
				log.print(msg+" success.");
			} catch (Exception e) {
				Log.e("DbManager.initDbPool",msg+" failed.");
				log.print(msg+" failed:\n  "+e.toString());
				e.printStackTrace(log.getWriter());
				throw e;
			}
		}
	}
	public void destroy(){
		String[] dbNames = dbsPool.getConnectedDbNames().toArray(new String[0]);
		for(int i=0;i<dbNames.length;i++)
		{
			dbsPool.closeAllConnection(dbNames[i]);
		}
	}
}
