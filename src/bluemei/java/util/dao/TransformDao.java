package bluemei.java.util.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bluemei.java.util.Log;
import bluemei.java.util.db.BluemeiQuery;
import bluemei.java.util.db.DatabasesPool;
import bluemei.java.util.db.DbUtil;
import bluemei.java.util.db.Row;
//转换
public abstract class  TransformDao<T> extends AbstractDao<T>
{
	public TransformDao(){
		super();
	}
	public TransformDao(String tableName, String dbName, DatabasesPool dbsPool)
	{
		super(tableName,dbName,dbsPool);
	}
	//根据类属性名称获取字段名, 子类可重写
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
	//根据类属性名称获取表单名称, 子类可重写
	protected String getFormName(String propertyName){
		return getFieldName(propertyName);
	}
	//从表单中取出数据
	private Object getValueFromForm(Map<String,String> request, String property, Class<?> type)
	{
		Object value=null;
		String strValue=null;
		strValue=request.get(getFormName(property));
		if(strValue==null){
			return null;
		}
		if(type.equals(Integer.TYPE)){
			value=Integer.valueOf(strValue);
		}
		else if(type.equals(String.class)){
			value=strValue;
		}
		else if(type.equals(Date.class)){
			value=new Date(Long.valueOf(strValue));
		}
		else if(type.equals(Boolean.TYPE)){
			value=Boolean.valueOf(strValue);
		}
		else if(type.equals(Byte.TYPE)){
			value=Byte.valueOf(strValue);
		}
		else if(type.equals(Short.TYPE)){
			value=Short.valueOf(strValue);
		}
		else if(type.equals(Long.TYPE)){
			value=Long.valueOf(strValue);
		}
		else if(type.equals(Float.TYPE)){
			value=Boolean.valueOf(strValue);
		}
		else if(type.equals(Double.TYPE)){
			value=Double.valueOf(strValue);
		}
		return value;
	}
	//从结果集取出数据
	private Object getValueFromResultSet(ResultSet rs, String property,Class<?> type) throws SQLException
	{
		Object value=null;
		if(type.equals(Integer.class)||type.equals(int.class)){
			value=rs.getInt(this.getFieldName(property));
		}
		else if(type.equals(String.class)){
			value=rs.getString(this.getFieldName(property));
		}
		else if(type.equals(Date.class)){
			value=rs.getDate(this.getFieldName(property));
		}
		else if(type.equals(Boolean.class)){
			value=rs.getBoolean(this.getFieldName(property));
		}
		else if(type.equals(Byte.class)||type.equals(byte.class)){
			value=rs.getByte(this.getFieldName(property));
		}
		else if(type.equals(Short.class)||type.equals(short.class)){
			value=rs.getShort(this.getFieldName(property));
		}
		else if(type.equals(Long.class)||type.equals(long.class)){
			value=rs.getLong(this.getFieldName(property));
		}
		else if(type.equals(Float.class)||type.equals(float.class)){
			value=rs.getFloat(this.getFieldName(property));
		}
		else if(type.equals(Double.class)||type.equals(double.class)){
			value=rs.getDouble(this.getFieldName(property));
		}
		else
			value=rs.getObject(this.getFieldName(property));
		return value;
	}
	
	//根据对象构造sql语句,利用反射机制
	protected HashMap<String, Object> parseData2SqlMap(T data) throws Exception{
		HashMap<String,Object> row=new HashMap<String,Object>();
		//取属性值
		Field fields[]=data.getClass().getFields();//getDeclaredFields
		for(int i=0;i<fields.length;i++){
			Log.d("TransformDao.parseData2SqlMap",fields[i].getName()+":"+fields[i].get(data));
		}
		String tmpKey=null;
		char oldChar, newChar;
		Object value=null;
		Method methods[]=data.getClass().getMethods();
		for(int i=0;i<methods.length;i++){
			//getter
			if(methods[i].getName().startsWith("get")){
				tmpKey=methods[i].getName().substring("get".length());
				if(tmpKey.equals("Class"))
					continue;				
			}
			else if(methods[i].getName().startsWith("is")){
				tmpKey=methods[i].getName().substring("is".length());			
			}
			else
				continue;
			
			if(tmpKey.length()>0){
				newChar=oldChar=tmpKey.charAt(0);
				if(oldChar>='A'&&oldChar<='Z'){
					newChar+=32;
				}
				tmpKey=tmpKey.replaceFirst(oldChar+"", newChar+"");
				value=methods[i].invoke(data);
				if(value!=null){
					row.put(getFieldName(tmpKey), value);
				}
			}
		}
		return row;		
	}
	
	//根据查询结果构造对象,利用反射机制
	protected List<T> onSearch(final ResultSet rs, int num) throws Exception{
		List<T> list=new ArrayList<T>(num);		
		T data=null;
		String property;
		char oldChar, newChar;
		Object value;
		for(int j=0;rs.next() && j<num;j++)
		{
			//data=(T) new Class<T>().newInstance();
			//data=(T)this.getClass().getMethod("parseParameter",
			//		Class.forName("javax.servlet.http.HttpServletRequest")).getReturnType().newInstance();
			data=(T)getDataClass().newInstance();//有木有更好的方法
			Method methods[]=data.getClass().getMethods();
			//对每一个属性赋值:data.setId(rs.getInt("id"));
			for(int i=0;i<methods.length;i++){
				//setter
				if(methods[i].getName().startsWith("set")){
					property=methods[i].getName().substring("set".length());
					if(property.length()>0){
						newChar = oldChar=property.charAt(0);
						if(oldChar>='A'&&oldChar<='Z'){
							newChar+=32;
						}
						property=property.replaceFirst(oldChar+"", newChar+"");
						value=getValueFromResultSet(rs,property,methods[i].getParameterTypes()[0]);
						if(value!=null){							
							Log.d("TransformDao.onSearch.property",property+"="+value+",class:"+value.getClass());
							methods[i].invoke(data,value);
						}
					}
				}
			}
			list.add(data);
		}
		return list;		
	}
	//从表单解析出对象,利用反射
	public T parseParameter(Map<String,String> request) throws Exception{
		String property;
		char oldChar, newChar;
		Object value;
		//T data=(T)this.getClass().getMethod("parseParameter",
		//		Class.forName("javax.servlet.http.HttpServletRequest")).getReturnType().newInstance();
		T data=(T)this.getDataClass().newInstance();
		Log.d("TransformDao.parseParameter.dataClass",data.getClass().toString());
		Method methods[]=data.getClass().getMethods();
		//对每一个属性赋值:data.setId(rs.getInt("id"));
		for(int i=0;i<methods.length;i++){
			//setter
			if(methods[i].getName().startsWith("set")){
				property=methods[i].getName().substring("set".length());
				if(property.length()>0){
					newChar = oldChar=property.charAt(0);
					if(oldChar>='A'&&oldChar<='Z'){
						newChar+=32;
					}
					property=property.replaceFirst(oldChar+"", newChar+"");	
					value=getValueFromForm(request,property,methods[i].getParameterTypes()[0]);	
					Log.d("TransformDao.parseParameter",property+"====>"+value);
					if(value!=null)
						methods[i].invoke(data,value);
				}
			}
		}
		return data;
	}
	//是否存在该记录
	public boolean exist(T data) throws Exception{
		//long id = this.getId(data);
		//BluemeiQuery query=new BluemeiQuery("id",id+"");
		BluemeiQuery query=new BluemeiQuery();
		HashMap<String, Object> record = parseData2SqlMap(data);
		//record.remove("id");
		query.addAllItemWithAnd(record);
		List<T> list = doSearchToList(query);
		if(list.size()>0){
			//setId(data,list.get(0));
			return true;
		}
		return false;
	}
	//存入对象到数据库,利用反射实现属性获取
	public boolean save(T data)throws Exception {		
		boolean isSuccess=false;
		HashMap<String, Object> row = parseData2SqlMap(data);
		Log.d("TransformDao.save","invoked map:"+row);
		Connection con=null;
		try {
			con = dbsPool.getPool(dbName).getOneConnection();
			
			boolean update=this.exist(data);
			if(update)
				isSuccess=DbUtil.updateById(getId(data), con, tableName, row);
			else
				isSuccess=DbUtil.insert(con, tableName, row);	
		}finally {
			dbsPool.returnOneConnection(dbName,con);
		}
		
		return isSuccess;	
	}
	@Override
	public boolean insertOneRow(String[] colNames, Row<T> row) throws Exception {
		Connection con=null;
		try {
			con = dbsPool.getPool(dbName).getOneConnection();
			return DbUtil.insertOneRow(con, tableName, colNames, row);
		}finally{
			dbsPool.returnOneConnection(dbName,con);
		}
	}	
	protected abstract Class<T> getDataClass();
}
