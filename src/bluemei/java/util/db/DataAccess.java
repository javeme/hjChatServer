package bluemei.java.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class DataAccess {
	private String driver=null,url=null,user=null,psw=null;
	private String dbName;
	/*
	static{
		//DatabaseConfigParser  dbconfig=new DatabaseConfigParser();//DatabaseConfigParser???
		try{
			dbconfig.parse("database.conf.xml");
			Properties dbProps=dbconfig.getProps();
			driver=dbProps.getProperty("driver");
			url=dbProps.getProperty("url");
			user=dbProps.getProperty("user");
			psw=dbProps.getProperty("psw");
			
		}catch(Exception e){}		
	}
	*/
	public DataAccess(String dbName){
		this("com.mysql.jdbc.Driver",//com.mysql.jdbc.Driver	org.git.mm.mysql.Driver
			 "jdbc:mysql://localhost:3306/"+dbName+"?useUnicode=true&characterEncoding=utf8",
			 "root",
			 "20101001");
		this.dbName=dbName;
	}
	public DataAccess(String driver,String url,String user,String psw){
		this.driver=driver;
		this.url=url;
		this.user=user;
		this.psw=psw;
		if(dbName==null)
		{
			if(url.indexOf("sqlserver")>0)
			{
				dbName=url.substring(url.indexOf("="));
			}
			else{
				String strs[]=url.split("/");
				dbName=strs[strs.length-1];
			}
		}
	}
	public Connection getConnection() throws ClassNotFoundException, SQLException
	{
		Connection con=null;	
		Class.forName(driver);
		con=DriverManager.getConnection(url,user,psw);		
		return con;
	}
	public static DataAccess getSqlServerEntry(String dbName)
		throws ClassNotFoundException, SQLException
	{
		Connection con=null;
		String url="localhost:1433";
		String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";//2005
		url="jdbc:sqlserver://"+url+";DatabaseName="+dbName;		
		String userName="";
		String userPassword="";
		
		return new DataAccess(driver, url, userName, userPassword);
	}
	public static DataAccess getAccessEntry(String dbName)
		throws ClassNotFoundException, SQLException
	{
		Connection con=null;
		String url="jdbc:odbc:"+dbName;
		String driver="sun.jdbc.odbc.JdbcOdbcDriver";	
		String userName="";
		String userPassword="";
		
		return new DataAccess(driver, url, userName, userPassword);
	}
	public static Connection getAccessConnection(String dbName)
		throws ClassNotFoundException, SQLException
	{
		Connection con=null;		
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		con=DriverManager.getConnection("jdbc:odbc:"+dbName);		
		//stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		return con;
	}
	//??DataSourcel??
	public static Connection getConnectionByDS(String arg) throws NamingException, SQLException
	{
		Connection con=null;		
		Context ctx=new InitialContext();
		DataSource ds=(DataSource) ctx.lookup(arg);//"ebsDS"	
		con=ds.getConnection();
		return con;
	}
	/*
	public static Connection getMysqlConnectionByDs(){//??DataSourcel??
		Connection con=null;
		try{
			DataSource source =new MysqlDataSource();
			
			source.setServerName("localhost");
			source.setDatabaseName("db");
			source.setPort(3306);
			
			source.setUser("bluemei");
			source.setPsw("password");				
			con=source.getConnection();
		}catch(Exception e){}	
	
		return con;
	}*/

	public String getDbMsg() {		
		return "\n    driver("+driver+")\n    url("+url+")\n    user("+user+")";
	}
	public String getDbName() {		
		return this.dbName;
	}
}