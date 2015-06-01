package cn.gx.dao.base;

import java.util.List;


public interface BaseDao<T> {
	
	void save(T t);
	
	void delete(int id);
	
	void update(T t);
	
	T find(int id);
	
	List<T> findAll();
	
	List<T> findByIds(Integer[] ids);
}
