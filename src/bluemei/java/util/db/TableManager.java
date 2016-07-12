package bluemei.java.util.db;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableManager
{
	private String dbName;
	private DataAccess dataAccess;
	
	public TableManager(String dbName){
		this.dbName=dbName;
		dataAccess=new DataAccess(dbName);
	}
	public TableManager(String driver,String url,String user,String psw){
		dataAccess=new DataAccess(driver, url, user, psw);
	}
	public boolean create(Class beanClass,String tableName) throws SQLException, ClassNotFoundException{
		Connection con=null;
		try
		{
			con = dataAccess.getConnection();
			Field[] fields=getFieldsFromObject(beanClass);
			return DbUtil.createTable(tableName, fields, con);		
		}finally{
			if(con!=null)
				con.close();
		}
	}
	public boolean drop(String tableName) throws ClassNotFoundException, SQLException
	{
		String sql="drop table "+tableName;
		Connection con = null;
		try
		{
			con = new DataAccess(dbName).getConnection();
			return DbUtil.execute(sql, con);
		} finally{
			if(con!=null)
				con.close();
		}
	}
	
	private Field[] getFieldsFromObject(Class beanClass)
	{
		List<Field> fieldList=new ArrayList<Field>();
		Method methods[]=beanClass.getMethods();
		String property;
		char newChar,oldChar;
		for(int i=0;i<methods.length;i++){
			//getter
			if(methods[i].getName().startsWith("get")){
				property=methods[i].getName().substring("get".length());
				if(property.equals("Class"))
					continue;
				if(property.length()>0){
					newChar=oldChar=property.charAt(0);
					if(oldChar>='A'&&oldChar<='Z'){
						newChar+=32;
					}
					property=property.replaceFirst(oldChar+"", newChar+"");
					fieldList.add(new Field(getFieldName(property),methods[i].getReturnType()));
				}
			}
		}
		return fieldList.toArray(new Field[0]);
	}
	//根据属性名称获取字段名, 子类可重写
	protected String getFieldName(String propertyName)
	{
		String fieldName=propertyName;
		char ch;
		Pattern pattern=Pattern.compile("[A-Z]");
		Matcher matcher=pattern.matcher(propertyName);
		while(matcher.find()){
			ch=matcher.group().charAt(0);	
			ch+=32;
			fieldName=fieldName.replaceFirst(matcher.group(), "_"+ch);//是否有更好的替换方法?
		}
		return fieldName;
	}
}
