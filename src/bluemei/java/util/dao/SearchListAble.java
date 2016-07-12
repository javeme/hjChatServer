package bluemei.java.util.dao;

import java.util.List;

import bluemei.java.util.db.BluemeiQuery;

public interface SearchListAble<E> {
	public boolean exist(E data) throws Exception;
	public List<E> doSearchToList(BluemeiQuery selectDemand) throws  Exception;
	//public List<E> doSearchToList(SelectDemand selectDemand,int numPerPage,int page)throws  Exception;
}
