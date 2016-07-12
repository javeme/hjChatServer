package bluemei.java.util;

import bluemei.java.util.db.DbManager;

public class AppContext
{
	private static DirUtil dirUtil;
	private static DbManager dbManager=null;
	//private static ServletContext application;
	
	static{
		dirUtil=new DirUtil();
		dbManager=new DbManager();
	}
	public static DbManager getDbManager(){
		return dbManager;		
	}
	

	public static void initDir(String path)
	{
		dirUtil.ROOT_PATH=path;
	}
	public static DirUtil getDir()
	{
		return dirUtil;
	}
}
