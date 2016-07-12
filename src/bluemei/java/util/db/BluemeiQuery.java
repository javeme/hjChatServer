package bluemei.java.util.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import bluemei.java.util.PageMsg;

public class BluemeiQuery {
	//select id from table where a=1 and b=2 order by a desc group by a
	private int numColume;//��ѯ����
	private String columeNames[]=null;//��ѯ����
	private HashMap<String , SelectRelation> whereMap=null;
	private HashMap<String , Boolean> orderMap=null;
	private List<String > groupList=null;
	
	private PageMsg pageMsg;//��ҳ
	
	public BluemeiQuery()
	{
		this(0);
	}
	public BluemeiQuery(int numColume)
	{
		this.numColume=numColume;
		columeNames=new String[numColume];
		whereMap=new HashMap<String , SelectRelation>();
		orderMap=new HashMap<String , Boolean>();
		groupList=new ArrayList<String >();		
	}
	public void setColumeName(String name,int i){
		columeNames[i]=name;
	}
	public BluemeiQuery(String key,String value)
	{
		this(0);
		addItemWithAnd(key, value);
	}
	//���where�Ӿ�����
	public void addItem(String key,Object value,String operate,String logic)
	{
		SelectRelation relation=new SelectRelation();
		relation.setLogic(logic);
		relation.setOperate(operate);
		relation.setValue(value);
		whereMap.put(key, relation);
	}
	//���where�Ӿ�����,��like
	public void addItemWithLikeOr(String key,Object value)
	{
		addItem(key,value,"like","or");
	}
	//���where�Ӿ�����,�Ե���
	public void addItemWithAnd(String key,Object value)
	{
		addItem(key,value,"=","and");
	}
	public void addAllItemWithLikeOr(HashMap<String, String> keyWordsMap)
	{
		Iterator<Entry<String, String>> iterator=keyWordsMap.entrySet().iterator();
		Entry<String, String> keywordEntry;
		while(iterator.hasNext())
		{
			keywordEntry=iterator.next();
			this.addItemWithLikeOr(keywordEntry.getKey(),"%"+keywordEntry.getValue()+"%");
		}
	}
	public void addAllItemWithAnd(HashMap<String, Object> record)
	{
		Iterator<Entry<String, Object>> iterator=record.entrySet().iterator();
		Entry<String, Object> searchEntry;
		while(iterator.hasNext())
		{
			searchEntry=iterator.next();
			this.addItemWithAnd(searchEntry.getKey(),searchEntry.getValue());
		}
	}	
	//��ȡwhere�Ӿ���ֵ
	public SelectRelation getWhereMapItem(String key)
	{
		return whereMap.get(key);
	}
	public int whereMapSize() {
		return whereMap.size();
	}
	public Set<String> whereMapKeySet() {
		return whereMap.keySet();
	}
	public Object removeWhereMapItem(String key) {
		return whereMap.remove(key);
	}
	//��������
	public void addOrder(String colName,boolean isAsc)
	{
		orderMap.put(colName, isAsc);
	}
	//���÷���
	public void addGroup(String colName)
	{
		groupList.add(colName);
		String[] names=new String[numColume+1];
		System.arraycopy(columeNames, 0, names, 0, numColume);
		names[numColume]=colName;
		columeNames=names;
		numColume++;
	}
	public int getSearchColNum() {
		return numColume;
	}
	public String[] getColName() {
		return columeNames;
	}
	public Object[] getGroupNames() {		
		return this.groupList.toArray();
	}
	public HashMap<String, Boolean> getOrderMap() {		
		return this.orderMap;
	}
	
	//��ҳ
	public void setPageMsg(PageMsg pageMsg) {
		this.pageMsg=pageMsg;
	}
	public PageMsg getPageMsg() {
		if(pageMsg==null)
			pageMsg=new PageMsg();
		return pageMsg;
	}
}
