package bluemei.java.util.dao;

import bluemei.java.util.db.BluemeiQuery;
import bluemei.java.util.db.Table;

public interface SearchTableAble<E> {
	public Table<E> doSearchToTable(BluemeiQuery selectDemand) throws Exception;
}
