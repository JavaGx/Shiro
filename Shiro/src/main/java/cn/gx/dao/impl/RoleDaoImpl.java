package cn.gx.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import cn.gx.dao.RoleDao;
import cn.gx.dao.base.BaseDaoImpl;
import cn.gx.entity.Permission;
import cn.gx.entity.Role;

@Repository
@SuppressWarnings("unchecked")
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao {

	@PersistenceContext
	private EntityManager em;
	
	public List<Permission> getRolesPerm(Integer id) {
		return em.createNativeQuery("{call getRolesPerm(?)}", Permission.class)
				.setParameter(1, id).setHint("org.hibernate.cacheable", true)
				.getResultList();
	}


}
