package bluemei.java.util.db;

import java.util.ArrayList;

public class Table<E>
{
	private ArrayList<String> fieldNames=null;//表示承?列名
	private Rows<E> rows=null;//每一行表示一条记彿存在丿?List里面
	public Table()
	{
		fieldNames=new ArrayList<String>();
		rows=new Rows<E>();
	}
	public void setColNames(String[] names)
	{
		fieldNames.clear();
		for(int i=0;i<names.length;i++)
			this.fieldNames.add(names[i]);
	}
	public String[] getColNames() {
		return (String[])fieldNames.toArray();
	}
	public Row<E> getLastRow()
	{
		return rows.get(rows.size()-1);
	}
	public Object get(int index, String name)//index行的name列名
	{
		int colPos=fieldNames.indexOf(name); 
		if(colPos==-1 || index<0||index>=rows.size()) 
			return null;
		Object value=rows.get(index).get(colPos); //System.out.println(colPos+"====="+value);
		return value;
	}
	public Rows<E> getRows()
	{
		return this.rows;
	}
	public int getRowNum()
	{
		return rows.size();
	}
}
