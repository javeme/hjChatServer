package bluemei.java.util.db;

import java.util.ArrayList;

public class Table<E>
{
	private ArrayList<String> fieldNames=null;//��ʾ��?����
	private Rows<E> rows=null;//ÿһ�б�ʾһ���Ǐ�����د?List����
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
	public Object get(int index, String name)//index�е�name����
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
