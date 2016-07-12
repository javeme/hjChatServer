package bluemei.java.util.dao;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import bluemei.java.util.db.Row;

public interface IParameter2Row {
	public Row dealwithParameter(Map<String,String> request, String[] colNames) 
		throws NoSuchAlgorithmException, Exception;
	public String[] getColNames() throws Exception ;
	public String[] resizeColumnNum(Row row,String[] colNames);
}
