package bluemei.java.util.dao;

import java.util.Map;

import bluemei.java.util.db.BluemeiQuery;
import bluemei.java.util.db.KeyRelation;

public interface ParaParseAble<T> {

	public T parseParameter(Map<String,String> request) throws Exception;
	public BluemeiQuery parseParameter(KeyRelation keys[],Map<String,String> request);
	public BluemeiQuery parseParameterWithLikeOr(String[]keys,Map<String,String> request
			,BluemeiQuery selectDemand);
	public BluemeiQuery parseParameterWithAnd(String[]keys,Map<String,String> request
			,BluemeiQuery selectDemand);
	//public boolean check(T data);
}
