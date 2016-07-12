package bluemei.java.util.dao;

import bluemei.java.util.db.Row;

public interface InsertAble<T> {
	public boolean insertOneRow(String[] colNames, Row<T> row) throws Exception;
	public boolean doInsert(T data) throws  Exception;
}


