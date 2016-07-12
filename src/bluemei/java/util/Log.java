package bluemei.java.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Log {
	private PrintWriter log=null;
	private SaveThread saveThread=null;
	//private boolean isOpen=false;
	private int dayOfMonth=0;
	private String prefix=null;
	private String dir=null;
	/*****************************文件日志***********************************/
	public Log(String prefix) throws IOException
	{
		this.prefix=prefix+"-";
		this.dir=DirUtil.ROOT_PATH;
		newLogFile();
	}
	private synchronized void newLogFile()throws IOException {
		String strTime=prefix+(Calendar.getInstance().get(Calendar.YEAR))+"-"
			+(Calendar.getInstance().get(Calendar.MONTH)+1)+"-"
			+Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if(log!=null)
		{
			log.close();
		}
		File file=new File(dir+"/log/"+strTime+".log");
		if(!file.getParentFile().exists()){
			Log.i("log", "create dirs: "+file.getParent());
			file.getParentFile().mkdirs();
			//file.createNewFile();
		}
		log=new PrintWriter(new FileWriter(file,true));
		
		dayOfMonth=Calendar.getInstance().get(Calendar.MONTH);
		if(saveThread==null)
		{
			saveThread=new SaveThread();
			saveThread.start();
		}
	}
	public synchronized void print(String str)
	{
		int currentTime=Calendar.getInstance().get(Calendar.MONTH);
		if(currentTime!=dayOfMonth)
		{			
			try {
				newLogFile();
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
		//log.append(str);
		String time=sf.format(new Date());
		str=String.format("[%s] %s", time,str);
		log.println(str);
	}
	public void println(String str) {
		print(str+System.getProperty("line.separator"));
	}

	public void printSuccess(String success) {
		println("success:"+System.getProperty("line.separator")+" "+success);
	}
	
	public void printWarm(String warm) {
		println("warm:"+System.getProperty("line.separator")+" "+warm);
	}
	public void printError(String error) {
		println("error:"+System.getProperty("line.separator")+" "+error);
	}
	public synchronized PrintWriter getWriter()
	{
		return this.log;
	}
	public synchronized void flush()
	{
		log.flush();
	}
	public void close() {
		log.close();
		log=null;
	}
	class SaveThread extends Thread
	{
		public void run()
		{
			while(true)//是否会在对象废弃之后还不停止,从而无法释放?
			{
				try {
					Thread.sleep(1000*1);
					if(log==null)
						break;
				} catch (InterruptedException e) {
					e.printStackTrace(log);
				}
				//log.println("test");
				log.flush();
			}
		}
	}
	/*********************************屏幕日志**************************************/
	public enum LogLevel{
		DEBUG,INFO,ERROR,NULL;
		
		public boolean need(LogLevel level)
		{
			if(this.ordinal()>level.ordinal())
				return false;
			return true;
		}
	}
	public static LogLevel logLevel=LogLevel.DEBUG;
	public static void d(String key, String value)
	{
		if(logLevel.need(LogLevel.DEBUG))
			stdlog(String.format("debug, %s: %s", key,value));
	}
	public static void i(String key, String value)
	{
		if(logLevel.need(LogLevel.INFO))
			stdlog(String.format("info, %s: %s", key,value));
	}
	public static void e(String key, String value)
	{
		if(logLevel.need(LogLevel.ERROR))
			stdlog(String.format("error, %s: %s", key,value));
	}
	
	public static SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
	private static void stdlog(String format)
	{		
		String time=sf.format(new Date());
		System.out.println(String.format("[%s] %s", time,format));
	}
}
