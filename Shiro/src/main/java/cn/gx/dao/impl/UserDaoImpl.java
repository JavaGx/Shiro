package cn.gx.dao.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import cn.gx.dao.UserDao;
import cn.gx.dao.base.BaseDaoImpl;
import cn.gx.entity.Role;
import cn.gx.entity.User;

@Repository
@SuppressWarnings("unchecked")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	@PersistenceContext
	private EntityManager em;

	public User findUserByUsername(String currentUsername) {
		List<User> users= em.createNativeQuery("{call findByName(?)}",User.class)
				.setParameter(1, currentUsername).setHint("org.hibernate.cacheable", true).getResultList();
		
		if(users.size()>0){
			User user=users.get(0);
			return user;
		}else{
			return null;
		}
	}

	public List<User> pageRecords(int beginIndex, int endIndex) {
		List<User> users=em.createNativeQuery("{call pageRecords(?,?)}",User.class)
				.setParameter(1, beginIndex).setParameter(2, endIndex)
				.setHint("org.hibernate.cacheable", true).getResultList();
		return users;
	}

	public int findRecords() {
		List<BigInteger> records=em.createNativeQuery("select count(id) from user").getResultList();
		if(records.size()>0){
			return records.get(0).intValue();
		}else{
			return 0;
		}
	}

	public List<Role> getUsersRole(Integer id) {
		return em.createNativeQuery("{call getUsersRole(?)}", Role.class)
			.setParameter(1, id).setHint("org.hibernate.cacheable", true).getResultList();
	}

	public String findEmail(String email) {
		List<String> e=em.createNativeQuery("{call getEmail(?)}", String.class)
						.setParameter(1, email).setHint("org.hibernate.cacheable", true).getResultList();
		if(e.size()>0){
			return e.get(0);
		}else{
			return null;
		}
	}
	
}
