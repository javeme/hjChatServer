package bluemei.java.util.db;

import java.util.ArrayList;

public class Rows<E> {	
	private ArrayList<Row<E>> data=null;
	public Rows()
	{
		data=new ArrayList<Row<E>>();
	}
	public Rows(int size)
	{
		data=new ArrayList<Row<E>>(size);
	}
	public Rows(Row<E> rows[])
	{
		for(int i=0;i<rows.length;i++)
			data.add(rows[i]);
	}
	public int size()
	{
		return data.size();
	}
	public void addRow(Row<E> row)
	{
		data.add(row);
	}
	public Row<E> get(int index)
	{
		return data.get(index);
	}
}
