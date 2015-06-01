package cn.gx.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.gx.dao.RoleDao;
import cn.gx.entity.Permission;
import cn.gx.entity.Role;
import cn.gx.service.RoleService;

@Service
@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class RoleServiceImpl implements RoleService {

	private static Log log=LogFactory.getLog(RoleServiceImpl.class);
	@Resource
	private RoleDao dao;
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=false)
	public void addRole(Role role) {
		dao.save(role);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Role> findAll() {
		List<Role> roles=dao.findAll();
		log.info("find All Roles");
		return roles;
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Role> findByIds(Integer[] ids) {
		log.info("find Role By ids");
		if(ids==null){
			throw new RuntimeException("没有参数");
		}else if(ids.length==1){
			Role role = dao.find(ids[0]);
			ArrayList<Role> list = new ArrayList<Role>();
			list.add(role);
			return list;
		}else{
			List<Role> roles=dao.findByIds(ids);
			return roles;
		}
	}
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Permission> findPermsByRoleId(Integer id) {
		return dao.getRolesPerm(id);
	}

}
