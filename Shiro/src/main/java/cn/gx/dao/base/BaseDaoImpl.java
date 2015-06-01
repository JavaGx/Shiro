package cn.gx.dao.base;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> implements BaseDao<T> {

	private Class<T> clazz;
	
	public BaseDaoImpl() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz = (Class<T>) pt.getActualTypeArguments()[0];
	}

	@PersistenceContext
	private EntityManager em;
	
	public void save(T t) {
		em.persist(t);
	}
	
	public void delete(int id) {
		em.createQuery("delete from "+clazz.getSimpleName());
	}
	
	public void update(T t) {
		em.merge(t);
	}
	
	public T find(int id) {
		T t=em.find(clazz, id);
		return t;
	}
	
	public List<T> findAll(){
		List<T> list=em.createNativeQuery("{call findAll(?)}",clazz)
				.setParameter(1, clazz.getSimpleName()).setHint("org.hibernate.cacheable", true).getResultList();
		return list;
	}
	
	public List<T> findByIds(Integer[] ids){
		String hql=null;
		for(int i=0;i<ids.length;i++){
			if(i==0){
				hql+="ids[i]";
			}else{
				hql+=",ids[i]";
			}
		}
		List<T> list=em.createNativeQuery("{call findByIds(?,?)}",clazz)
				.setParameter(1, clazz.getSimpleName())
				.setParameter(2, hql).setHint("org.hibernate.cacheable", true).getResultList();
		return list;
	}

}
