package cn.gx.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.gx.dao.UserDao;
import cn.gx.entity.Role;
import cn.gx.entity.User;
import cn.gx.mail.MailSend;
import cn.gx.service.RoleService;
import cn.gx.service.UserService;

@Service
@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public class UserServiceImpl implements UserService{
	
	private static Log log=LogFactory.getLog(UserServiceImpl.class);
	@Resource
	private UserDao dao;
	
	@Resource
	private RoleService rs;
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public User findUserByUsername(String currentUsername) {
		log.info("find User by username");
		return dao.findUserByUsername(currentUsername);
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<Role> findAllRoles() {
		List<Role> list = rs.findAll();
		if(list!=null){
			log.info("find Roles");
			return list;
		}else{
			throw new RuntimeException("---------查询角色出错！");
		}
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public void register(User user, Integer[] ids) {
		List<Role> roles=rs.findByIds(ids);
		user.setRoles(roles);
		log.info("register user");
		dao.save(user);
		new MailSend(user.getEmail()).sendMail();
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<User> pageList(int beginIndex, int endIndex) {
		List<User> users=dao.pageRecords(beginIndex,endIndex);
		log.info("record from "+beginIndex+" to "+endIndex);
		return users;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public int findRecords() {
		int records=dao.findRecords();
		log.info("the number of records is "+records);
		return records;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<User> findAllUsers() {
		List<User> list = dao.findAll();
		log.info("find all user");
		return list;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public List<Role> findRolesByUserId(Integer id) {
		List<Role> roles=dao.getUsersRole(id);
		return roles;
	}
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public String findEmail(String email) {
		return dao.findEmail(email);
	}
}
