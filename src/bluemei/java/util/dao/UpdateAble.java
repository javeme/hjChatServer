package bluemei.java.util.dao;

import java.util.HashMap;

public interface UpdateAble<T> {
	public T doUpdate(T data) throws  Exception ;
}
