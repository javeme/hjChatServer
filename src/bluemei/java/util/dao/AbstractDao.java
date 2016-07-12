package bluemei.java.util.dao;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bluemei.java.util.Log;
import bluemei.java.util.db.BluemeiQuery;
import bluemei.java.util.db.DatabasesPool;
import bluemei.java.util.db.DbArgs;
import bluemei.java.util.db.DbUtil;
import bluemei.java.util.db.KeyRelation;
import bluemei.java.util.db.Row;

public abstract class AbstractDao<T>  implements IParameter2Row
	,InsertAble<T>,DeleteAble,UpdateAble<T>,SearchListAble<T>,ParaParseAble<T>
{
	protected String dbName=null,tableName=null;
	//protected Connection con=null;
	protected DatabasesPool dbsPool=null;
	
	public AbstractDao()
	{
		this(null);
	}
	public AbstractDao(DatabasesPool dbsPool){
		this.dbsPool=dbsPool;
	}
	public AbstractDao(String tableName,String dbName,DatabasesPool dbsPool)
	{
		this.dbName=dbName;
		this.dbsPool=dbsPool;
		this.tableName=tableName;
	}
	
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public DatabasesPool getDbsPool() {
		return dbsPool;
	}
	public void setDbsPool(DatabasesPool dbsPool) {
		this.dbsPool = dbsPool;
	}
	//��ȡ������������(�������÷�����ƻ�ȡbean����������)
	public String[] getColNames() throws Exception {
		String colNames[]=null;
		Connection con=null;
		try {
			con = dbsPool.getPool(dbName).getOneConnection();
			colNames=DbUtil.getColNames(con,tableName);
		} catch (Exception e) {
			dbsPool.returnOneConnection(dbName,con);
			throw e;
		}
		dbsPool.returnOneConnection(dbName,con);
		return colNames;
	}
	//��reques�н���������
	public Row<Object> dealwithParameter(Map<String,String> request, String[] colNames)
		throws NoSuchAlgorithmException, Exception 
	{
		Row row=new Row();
		//��ȡ�ǿղ���		
		for(int i=0;i<colNames.length;i++)
		{
			Object tmp=request.get(colNames[i]);
			if(tmp!=null)
			{					
				colNames[row.size()]=colNames[i];
				Log.d("AbstractDao.dealwithParameter","����"+row.size()+"("+colNames[i]+") is:"+tmp);
				row.add(tmp); 
				//count++;
			}
		}
		/*Ϊ����Ч?
		int formItemNum=row.size();
		if(formItemNum<colNames.length)
		{
			String tmp[]=new String[formItemNum];
			for(int i=0;i<formItemNum;i++)
			{
				tmp[i]=colNames[i];
			}
			colNames=tmp;
		}*/
		return row;
	}
	public HashMap<String,Object> dealwithParameterToMap(Map<String,String> request,String[] colNames) 
	{
		HashMap<String,Object> rowMap=new HashMap<String,Object>();
		for(int i=0;i<colNames.length;i++)
		{
			Object tmp=request.get(colNames[i]);
			if(tmp!=null)
			{
				Log.d("AbstractDao.dealwithParameterToMap","����"+i+"("+colNames[i]+") is:"+tmp);
				rowMap.put(colNames[i], tmp); 
				//count++;
			}
		}
		return rowMap;
	}
	//����������ü�������
	public String[] resizeColumnNum(Row row,String[] colNames)
	{
		int formItemNum=row.size();
		if(formItemNum<colNames.length)
		{
			String tmp[]=new String[formItemNum];	
			for(int i=0;i<formItemNum;i++)
			{
				tmp[i]=colNames[i];
			}
			colNames=tmp;
		}
		return colNames;
	}

	//���ݼ�������������������ֵ��
	@Override
	public BluemeiQuery parseParameterWithLikeOr(String []keys,Map<String,String> request,BluemeiQuery selectDemand)
	{
		//*
		if(selectDemand==null)
			selectDemand=new BluemeiQuery();
		String value;
		for(int i=0;i<keys.length;i++)
		{
			value=request.get(keys[i]);
			if(value!=null)
				selectDemand.addItemWithLikeOr(keys[i], "%"+value+"%");//for like
		}
		return selectDemand;//*/
		/*
		KeyRelation kr[]=new KeyRelation[keys.length];
		for(int i=0;i<keys.length;i++)
		{
			kr[i]=new KeyRelation();
			kr[i].key=keys[i];
			kr[i].logic="or";
			kr[i].operate="like";
		}
		return parseParameter(kr,request);*/
	}
	@Override
	public BluemeiQuery parseParameterWithAnd(String []keys,Map<String,String> request,BluemeiQuery selectDemand)
	{
		if(selectDemand==null)
			selectDemand=new BluemeiQuery();
		String value;
		for(int i=0;i<keys.length;i++)
		{
			value=request.get(keys[i]);
			if(value!=null)
				selectDemand.addItemWithAnd(keys[i],value);//for and
		}
		return selectDemand;
	}	
	@Override
	public BluemeiQuery parseParameter(KeyRelation keys[],Map<String,String> request)
	{
		BluemeiQuery selectDemand=new BluemeiQuery();
		String value;
		for(int i=0;i<keys.length;i++)
		{
			value=request.get(keys[i].key);
			if(value!=null)
			{
				selectDemand.addItem(keys[i].key, value, keys[i].operate, keys[i].logic);
			}
		}
		return selectDemand;
	}
	//����,���б���ʽ����,������������ҳ��װ��SelectDemand���������
	@Override
	public List<T> doSearchToList(BluemeiQuery selectDemand)throws Exception 
	{
		DbArgs args=new DbArgs();
		List<T> list=null;
		try {
			args.con=this.dbsPool.getPool(dbName).getOneConnection();
			args.tableName=tableName;
			args.rs=DbUtil.search(args,selectDemand);			

			ResultSet rs=args.rs;
			
			rs.last(); 
			int len=rs.getRow();
			rs.beforeFirst();
			
			int rowPos=0;
			int numPerPage=selectDemand.getPageMsg().numPerPage;//ÿҳ����
			int page=selectDemand.getPageMsg().currentPage;//��ʾ�ڼ�ҳ
			Log.d("AbstractDao.doSearchToList","�����:"+len+",page:"+page +",numPerPage:"+numPerPage+",con addr:"+args.con.hashCode());
			if(numPerPage<=0)//ȫ��
			{
				numPerPage=len;
				rowPos=0;
			}		
			if(page<=1) //��һҳ
			{
				page=1;
				rowPos=0;
			}		
			else //��nҳ
			{
				rowPos=(page-1)*numPerPage;
			}
			if(numPerPage>len)
				numPerPage=len;
			if(rowPos>=len)
				rowPos=len;
			
			if(rowPos>0)
				rs.absolute(rowPos);
			list=onSearch(rs,numPerPage);
		}finally{			
			args.close();
			dbsPool.getPool(dbName).returnOneConnection(args.con);
		}
		return list;	
	}
	protected List<T> doSearchAllToList(BluemeiQuery selectDemand)
			throws Exception
	{
		return doSearchToList(selectDemand);
	}
	//�����¼
	@Override
	public boolean doInsert(T data) throws Exception {
		boolean isSuccess = false;
		Connection con=null;
		
		if(check4Insert(data))
		{
			try {
				con = dbsPool.getPool(dbName).getOneConnection();
				HashMap<String,Object> dataMap = parseData2SqlMap(data);	
				isSuccess=DbUtil.insert(con, tableName, dataMap);
			}finally {
				dbsPool.returnOneConnection(dbName,con);
			}
			
		}			
		
		return isSuccess;
	}
	//���¼�¼
	@Override
	public T doUpdate(T data) throws Exception {
		boolean isSuccess = false;
		Connection con=null;		
		if(check4Update(data))
		{
			try{
				con = dbsPool.getPool(dbName).getOneConnection();
				HashMap<String,Object> dataMap = parseData2SqlMap(data);	
				isSuccess=DbUtil.updateById(getId(data), con, tableName, dataMap);		
			}finally{
				dbsPool.returnOneConnection(dbName,con);
			}
		}	
		return isSuccess?data:null;
	}
	protected abstract HashMap<String, Object> parseData2SqlMap(T data)throws Exception;//for doInsert framework
	protected abstract boolean check4Insert(T data) throws Exception;
	protected abstract boolean check4Update(T data) throws Exception;
	protected abstract List<T> onSearch(final ResultSet rs, int num) throws SQLException, Exception;
	protected abstract long getId(T data) throws Exception;
	//protected abstract void setId(T destData, T sourceData);
}
