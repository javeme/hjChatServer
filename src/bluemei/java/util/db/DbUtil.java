package bluemei.java.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import bluemei.java.util.Log;

import com.sun.rowset.CachedRowSetImpl;


public class DbUtil {
	//private static Connection con=null;
	/*
	protected static String[] fieldName=null;
	protected static String tableName=null;

	
	public static void initFieldName(String[] names){
		fieldName=names;
	}
	public static void initTableName(String name){
		tableName=name;
	}*/
	/*
	public static void setConnection(Connection conn){
		con=conn;
	}*/
	//ִ��sql���
	public static boolean execute(String sql, Connection con) throws SQLException
	{
		Log.d("DbUtil.execute.sql",sql);
		Statement stmt = con.createStatement();
		try{
			return stmt.execute(sql);
		}catch (SQLException e) {
			throw e;
		}finally{
			stmt.close();
		}
	}
	//������
	public static boolean createTable(String tableName,Field fields[],Connection con) throws SQLException{
		//create table tbName(id int not null,name varchar(20))
		String sql="create table "+tableName+"( ";
		String tmp;
		for(int i=0;i<fields.length;i++){
			//				   id int(10) not null default NULL PRIMARY KEY  COMMENT '��ע'
			tmp=String.format(" %s %s(%d) 	%s 		default %s 		%s 			%s,",
					fields[i].getName(), fields[i].getType(), fields[i].getLength(),  
					fields[i].isNullAble()?"":"NOT NULL",
					fields[i].getDefaultValue(),
					fields[i].isPrimaryKey()?"PRIMARY KEY":"",
					fields[i].getComment()==null?"":("COMMENT '"+fields[i].getComment()+"'")
			);
			tmp=tmp.replace("(0)", "");
			sql+=tmp;
		}
		sql=sql.substring(0,sql.length()-1);
		sql+=")";
		return execute(sql,con);
	}
	//����һ��
	public static boolean insert(Connection con,String tableName,HashMap row) throws SQLException
	{
		PreparedStatement pstmt=null;	
		Object[] cols=row.keySet().toArray();
		String strField="",strValue="";
		int i,length=row.size();
		for(i=0;i<length;i++){
			strField+=cols[i]+",";
			strValue+="?,";
		}
		strField=strField.substring(0,strField.length()-1);
		strValue=strValue.substring(0,strValue.length()-1);
		String sql=String.format("insert into %s(%s)values(%s)",tableName,strField,strValue);
		Log.d("DbUtil.insert.sql",sql);
		int count[]=new int[]{-1};
		try
		{
			//con.setAutoCommit(false);
			pstmt=con.prepareStatement(sql);
			for(i=0;i<length;i++)
			{
				pstmt.setObject(i+1, row.get(cols[i]));	
				Log.d("DbUtil.insert",cols[i]+"--"+row.get(cols[i]));
			}
			pstmt.addBatch();
			count=pstmt.executeBatch(); 
			//con.commit();
		}catch(SQLException e){
			//con.rollback();
			pstmt.close();
			throw e;
		}	
		return count[0]>=0;
	}
	//����һ��
	public static boolean insertOneRow(Connection con,String tableName,String[] fieldName,Row row) throws SQLException{		
		return insert(con,tableName,fieldName,new Row[] {row});
	}
	//�������
	public static boolean insert(Connection con,String tableName,String[] fieldNames,Row[] rows) throws SQLException{		
		PreparedStatement pstmt=null;//PreparedStatement�Ƿ�� �Զ��ع�����,�Ƿ���Ҫ��������?
		String strField="";
		String strValue="";
		int rowCount=rows.length;
		int count[]=new int[rowCount];
		for(int i=0;i<fieldNames.length;i++){
			strField+=fieldNames[i]+",";
			strValue+="?,";
		}
		strField=strField.substring(0,strField.length()-1);
		strValue=strValue.substring(0,strValue.length()-1);
		//String sql="insert into "+tableName+"("+strField+")values("+strValue+")";
		String sql=String.format("insert into %s(%s)values(%s)",tableName,strField,strValue);
		//Log.d("DbUtil.",sql);
		//boolean isSuccess=true;
		try
		{
			con.setAutoCommit(false);
			pstmt=con.prepareStatement(sql);			
			for(int i=0;i<rowCount;i++){//��
				for(int j=0;j<fieldNames.length;j++){//����ÿһ��
					pstmt.setObject(j+1, rows[i].get(j));
					//Log.d("DbUtil.",(String)datas[i].getDataByArray()[j]);
				}
				pstmt.addBatch();
			}	
			count=pstmt.executeBatch(); 
			con.commit();
		}catch(SQLException e){
			//isSuccess=false;
			con.rollback();
			pstmt.close();
			throw e;
		}finally{
			pstmt.close();
		}
		for(int i=0;i<rowCount;i++){	//Log.d("DbUtil.",count[i]);
			if(count[i]>0)
				continue;
			else{
				count[0]=0;//???????
				break;
			}		
		}		
		return count[0]>0;
	}	
	
	//ɾ��
	public static boolean delete(Connection con,String tableName,String[] fieldNames,int[] id) throws SQLException{
		if(con==null)
			return false;
		PreparedStatement pstmt=null;
		int length=id.length;
		int count[]=new int[length];
		String sql=String.format("delete from %s where id=?", tableName);
			
		try
		{
			con.setAutoCommit(false);
			pstmt=con.prepareStatement(sql);//?????
			for(int i=0;i<length;i++){
				pstmt.setInt(i+1,id[i]);//1???????"?"??
				pstmt.addBatch();
			}
			count=pstmt.executeBatch();	
			con.commit();
		}catch(SQLException e){
			//isSuccess=false;
			con.rollback();
			pstmt.close();
			throw e;
		}finally{
			pstmt.close();
		}
		for(int i=0;i<length;i++){	//Log.d("DbUtil.",count[i]);
			if(count[i]>0)
				continue;
			else{
				count[0]=0;//??????
				break;
			}			
		}
		
		return count[0]>0;
	}
	//????
	public static boolean updateById(long id,Connection con,String tableName,String[] fieldNames,Row[] rows) throws SQLException{
		if(con==null)
			return false;
		PreparedStatement pstmt=null;
		int length=rows.length;
		int count[]=new int[length];
		String str="";
		for(int i=0;i<fieldNames.length;i++){
			str+=fieldNames[i]+"=?,";
		}
		str=str.substring(0,str.length()-1);
		String sql=String.format("update %s set %s where id=?", tableName,str);
		Log.d("DbUtil.updateById.sql",sql);
		try
		{
			con.setAutoCommit(false);
			pstmt=con.prepareStatement(sql);//?????			
			for(int i=0;i<length;i++){//����
				pstmt.setLong(fieldNames.length+1,id);
				for(int j=0;j<fieldNames.length;j++){//ÿ�еĸ���
					pstmt.setObject(j+1,rows[i].get(j+1));		
					//Log.d("DbUtil.",i+"-"+(j+1)+","+fieldNames.length);					
				}
				pstmt.addBatch();
			}
			count=pstmt.executeBatch();	
			con.commit();
		}catch(SQLException e){
			//isSuccess=false;
			con.rollback();
			pstmt.close();
			throw e;
		}finally{
			pstmt.close();
		}
		for(int i=0;i<length;i++){		//Log.d("DbUtil.",count[i]);
			if(count[i]>0)
				continue;
			else{
				count[0]=0;//???????
				break;
			}				
		}
		
		return count[0]>0;
	}
	public static boolean updateById(long id, Connection con, String tableName,HashMap rowMap) throws SQLException 
	{
		if(con==null)
			return false;
		PreparedStatement pstmt=null;
		int length=rowMap.size();
		if(length==0)
			return true;
		int count[]=new int[1];
		Object[] cols=rowMap.keySet().toArray();
		String str="";
		int i;
		for(i=0;i<length;i++){
			str+=cols[i]+"=?,";
		}
		str=str.substring(0,str.length()-1);
		String sql=String.format("update %s set %s where id=?", tableName,str);
		Log.d("DbUtil.updateById.sql",sql);
		try
		{
			con.setAutoCommit(false);
			pstmt=con.prepareStatement(sql);
			pstmt.setLong(length+1,id);
			for(i=0;i<length;i++){
				pstmt.setObject(i+1,rowMap.get(cols[i]));		
				Log.d("DbUtil.updateById",cols[i]+"="+rowMap.get(cols[i]));					
			}
			pstmt.addBatch();			
			count=pstmt.executeBatch();	
			con.commit();
		}catch(SQLException e){
			//isSuccess=false;
			con.rollback();
			pstmt.close();
			throw e;
		}finally{
			pstmt.close();
		}		
		return count[0]>0;
	}
	
	//����,���ַ�����ʽ���صı��(html)
	public static StringBuilder searchToStrTable(Connection con,String sql,int pageNum) throws SQLException{
		if(con==null)
			return null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		ResultSetMetaData rsm=null;
		try
		{
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			rsm=rs.getMetaData();
		}catch(SQLException e){
			rs.close();
			pstmt.close();
			throw e;
		}
		StringBuilder strTable=new StringBuilder();
		int i;
		int colCount=rsm.getColumnCount();
		
		strTable.append("<th>");
		for(i=0;i<colCount;i++)
		{
			strTable.append("<td>");
			strTable.append(rsm.getColumnName(i));
			strTable.append("</td>");
		}
		strTable.append("</th>");
		
		while(rs.next())
		{		
			strTable.append("<tr>");
			for(i=0;i<colCount;i++){
				strTable.append("<td>");
				strTable.append(rs.getObject(i+1).toString());
				strTable.append("</td>");
			}				
			strTable.append("</tr>");
		}	
		SQLException closeException=null;
		try{
			rs.close();
		}catch(SQLException e)
		{
			closeException=e;
		}
		try{
			pstmt.close();
		}catch(SQLException e)
		{
			closeException=e;
		}
		if(closeException!=null)
		{
			throw closeException;
		}			
		return strTable;
	}
	public static String[] getColNames(Connection con, String tableName) throws SQLException {
		PreparedStatement pstmt=null;
		ResultSet rs=null;		
		ResultSetMetaData rsm=null;
		try
		{
			pstmt=con.prepareStatement("select * from "+tableName);//�Ƿ���޸�Ϊ��ѯ��һ����¼����	
			rs=pstmt.executeQuery();
			rsm=rs.getMetaData();
		}catch(SQLException e){
			if(rs!=null)
				rs.close();
			pstmt.close();
			throw e;
		}
		int colCount=rsm.getColumnCount();
		String colNames[]=new String[colCount];
		int i;
		for(i=0;i<colCount;i++)
		{
			colNames[i]=rsm.getColumnName(i+1);
		}
		//����һ�ַ���:
		//DatabaseMetaData md=con.getMetaData();
		//md.getTables(catalog, schemaPattern, tableNamePattern, types);
		rs.close();
		pstmt.close();
		return colNames;
	}
	//����,����ResultSet�����(�Ƿ�ȫ?)
	public static ResultSet search(DbArgs args,BluemeiQuery demand) throws SQLException
	{
		String sql=parseToSql(args.tableName,demand);
		return search(args,sql);
	}
	public static ResultSet search(DbArgs args, String sql) throws SQLException 
	{
		if(args.con==null)
			return null;	
		Log.d("DbUtil.search.sql",sql);
		args.pstmt=args.con.prepareStatement(sql);// �ȹ���???
		args.rs=args.pstmt.executeQuery();		
		return args.rs;
	}
	//����,��RowSet��ʽ����
	public static RowSet searchRowSet(Connection con, String sql) throws SQLException {
		if(con==null)
			return null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		ResultSetMetaData rsm=null;
		try
		{
			pstmt=con.prepareStatement(sql);// �ȹ���???
			Log.d("DbUtil.searchRowSet.sql",sql);
			rs=pstmt.executeQuery();
		}catch(SQLException e){
			if(rs!=null)
				rs.close();
			pstmt.close();
			throw e;
		}
		CachedRowSet rowSet = new CachedRowSetImpl();
		rowSet.populate(rs);
		try{
			if(rs!=null)
				rs.close();
			pstmt.close();
		}catch(SQLException e){
			throw e;
		}
		return rowSet;
	}
	public static RowSet searchRowSet(Connection con, String tableName,BluemeiQuery demand) throws SQLException
	{
		String sql=parseToSql(tableName,demand);
		return searchRowSet(con,sql);
	}
	//��������RowMap��ϵ�����м�¼
	public static Table searchToTable(Connection con, String tableName,BluemeiQuery demand) throws SQLException
	{
		String sql=parseToSql(tableName,demand);		
		return searchToTable(con, tableName, sql);
	}
	public static Table searchToTable(Connection con,String tableName, String sql) throws SQLException
	{
		Table table=new Table();
		int i;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		ResultSetMetaData rsm=null;
		try
		{
			String cols[]=DbUtil.getColNames(con, tableName);
			table.setColNames(cols);
			Row row;
			pstmt=con.prepareStatement(sql);
			Log.d("DbUtil.searchToTable.sql",sql);
			rs=pstmt.executeQuery();
			while(rs.next())
			{
				row=new Row();
				for(i=0;i<cols.length;i++)
				{
					row.add(rs.getObject(cols[i]));
				}
				table.getRows().addRow(row);
			}
		}catch(SQLException e){
			if(rs!=null)
				rs.close();
			pstmt.close();
			throw e;
		}
		return table;
	}
	//����,����������ʽ����(����)
	public static Rows searchToRows(Connection con,String sql) throws SQLException{
		if(con==null)
			return null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;		
		try
		{
			pstmt=con.prepareStatement(sql);	//�Ƿ�������ηǷ��ַ�,��ֹע��?
			rs=pstmt.executeQuery();
		}catch(SQLException e){
			//isSuccess=false;
			rs.close();
			pstmt.close();
			throw e;
		}
		int colCount=rs.getMetaData().getColumnCount();
		Rows table=new Rows();
		while(rs.next())
		{		
			Row row=new Row();
			for(int i=0;i<colCount;i++){
				//row.add(rs.getString(fieldNames[i]));
				row.add(rs.getObject(i+1));
			}				
			table.addRow(row);
		}
		SQLException closeException=null;
		try{
			rs.close();
		}catch(SQLException e)
		{
			closeException=e;
		}
		try{
			pstmt.close();
		}catch(SQLException e)
		{
			closeException=e;
		}
		if(closeException!=null)
		{
			throw closeException;
		}
		return table;
	}
	/*//��mapת��Ϊwhere�Ӿ�(andģʽ)
	private static String parseSql(String tableName,HashMap<String, Object> whereMap)
	{		
		return parseSql("=","and",tableName,whereMap);
	}*/
	//��mapת��Ϊwhere�Ӿ�(operate��ʾ��ֵ�Թ�ϵ,logic��ʾ��������߼���ϵ)
	public static String parseToSql(String tableName,BluemeiQuery demand)
	{
		String sql="select ",strWhere=" where ",strGroup=" group by ",strOrder=" order by ";
		SelectRelation relation;
		String operate, logic="";
		Object value;
		int i,length;
		//����select * from table
		length=demand.getSearchColNum();
		String[] names = demand.getColName();
		for(i=0;i<length;i++)
		{
			if(i>0)
				sql+=",";
			sql+=names[i];
		}
		if(i==0)
			sql+="*";
		sql+=" from "+tableName;
		//����where�Ӿ�
		Object wheres[]=demand.whereMapKeySet().toArray();
		length=demand.whereMapSize();
		for(i=0;i<length;i++)
		{
			//strWhere+=wheres[i]+"='"+whereMap.get(wheres[i])+"' and ";
			relation=demand.getWhereMapItem(wheres[i].toString());
			operate=relation.getOperate();
			logic=relation.getLogic();
			value=relation.getValue();
			if(i==0)
				logic="";
			strWhere+=String.format(" %s %s %s '%s'",
					logic,wheres[i],operate,value);
		}
		//strWhere=strWhere.substring(0,strWhere.length()-logic.length());
		if(i!=0)
			sql+=strWhere;
		//����Group�Ӿ�
		Object groups[]=demand.getGroupNames();
		length=groups.length;
		for(i=0;i<length;i++)
		{
			if(i>0)
				strGroup+=",";
			strGroup+=groups[i];
		}
		if(i!=0)
			sql+=strGroup;
		
		//����Order�Ӿ�
		HashMap<String, Boolean> orderMap=demand.getOrderMap();
		Object orders[]=orderMap.keySet().toArray();
		length=orders.length;
		for(i=0;i<length;i++)
		{
			if(i>0)
				strOrder+=",";
			strOrder+=orders[i]+(orderMap.get(orders[i])?" asc":" desc");
		}
		if(i!=0)
			sql+=strOrder;
		return sql;
	}
}
