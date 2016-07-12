package bluemei.java.util.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import bluemei.java.util.Log;
import bluemei.java.util.SynchronizedLinkedList;


public class DatabasePool {
	private SynchronizedLinkedList<Connection> conList=null;
	private int nMaxConCount;
	private int nMinConCount;
	private boolean runnable;
	private Log log=null;
	private int nCurrentConCount;
	//private String dbName=null;
	private DataAccess da=null;
	DatabasePool()
	{
		this(10);
	}
	DatabasePool(int minConCount)
	{
		this(minConCount,20);
	}
	DatabasePool(int minConCount,int maxConCount)
	{
		nMinConCount=minConCount;
		nMaxConCount=maxConCount;
		nCurrentConCount=0;	
		//log=new Log("DatabasePool");//DatabasePool.class.getName()
	}
	public void initConnections(String dbName) throws ClassNotFoundException, SQLException, IOException {
		da=new DataAccess(dbName);
		log=new Log("DatabasePool-"+dbName);
		getConnectionsFromDb();
	}
	public void initConnections(String driver,String url,String user,String psw) throws ClassNotFoundException, SQLException, IOException {
		da=new DataAccess(driver,url,user,psw);
		log=new Log("DatabasePool-"+da.getDbName());
		getConnectionsFromDb();
	}
	private void getConnectionsFromDb() throws ClassNotFoundException, SQLException
	{
		if(conList==null)
			conList=new SynchronizedLinkedList<Connection>();
		for(int i=0;i<nMinConCount;i++)
		{			
			conList.addToLast(da.getConnection());
			nCurrentConCount++;
		}
		Log.d("DatabasePool.getConnections","init connections success.  database message: "+da.getDbMsg());
		log.print("init connections success:");
		log.print("  database message: "+da.getDbMsg());
		log.print("  min connection count: "+this.nMinConCount);
		log.print("  max connection count: "+this.nMaxConCount);
		startCheckConStateService();
	}
	public final Connection getOneConnection() throws Exception
	{
		if(conList.size()==0)
		{
			if(nCurrentConCount>=nMaxConCount)
			{
				log.print("can't provide any more connection," +
						"connection count reach the max range:"+nMaxConCount);
				throw new Exception("connect refuse:connection count reach the max range:"+nMaxConCount);
			}
			try {
				conList.addToLast(da.getConnection());
				nCurrentConCount++;
			} catch (Exception e) {
				e.printStackTrace(log.getWriter());
				throw new Exception("original connect refuse");
			}
		}
		Log.d("DatabasePool.getOneConnection","get one connection,current con="+nCurrentConCount);		
		return conList.removeLastObject();
	}
	public void returnOneConnection(Connection con)
	{
		if(con==null)
		{
			Log.e("DatabasePool.returnOneConnection","***********connection is null.");
			return;
		}
		conList.addToLast(con);
		
		Log.d("DatabasePool.returnOneConnection","return one connection ,current con:"+(nCurrentConCount-conList.size())+".\n");		
	}
	public void closeAllConnection()
	{
		Connection con;
		int size=conList.size();
		for(int i=0;i<size;i++)
		{
			con=conList.removeLastObject();
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {	
					Log.e("DatabasePool.closeAllConnection",e.toString());			
					e.printStackTrace();
				}
			}
		}
	}
	public int getCurrentConNum()
	{
		return conList.size();
	}
	public void setRunnable(boolean runnable)
	{
		this.runnable=runnable;
	}
	private void startCheckConStateService()
	{
		runnable=true;
		new CheckConStateThread().start();
	}
	class CheckConStateThread extends Thread
	{
		public void run()
		{
			int i,size;
			Log.i("DatabasePool.CheckConStateThread.run","start...");
			log.print("thread of checking connection state start success.");
			while(runnable)
			{
				log.print("");
				log.print("total connections is: "+nCurrentConCount);
				log.print("current connections is: "+(nCurrentConCount-conList.size()));
				try {				
					Thread.sleep(1000*60);
				} catch (InterruptedException e) {
					e.printStackTrace(log.getWriter());
				}
				conList.lock();//是否只要调用synchronized get(i)就可以让其它方法等待???
				size=conList.size();
				for(i=0;i<size;i++)
				{
					try {
						if(conList.get(i).isClosed())
						{
							conList.remove(i);
							nCurrentConCount--;
							try {
								conList.addToLast(da.getConnection());
								nCurrentConCount++;
							} catch (ClassNotFoundException e) {
								e.printStackTrace(log.getWriter());
							}
						}
					} catch (SQLException e) {
						log.print(e.toString());
					}
				}
				conList.unLock();
			}
			Log.i("DatabasePool.CheckConStateThread.run","destroy");
			log.print("thread of checking connection state destroy.");
		}
	}
}
