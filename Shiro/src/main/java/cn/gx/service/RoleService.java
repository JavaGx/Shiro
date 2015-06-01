package cn.gx.service;

import java.util.List;

import cn.gx.entity.Permission;
import cn.gx.entity.Role;

public interface RoleService {

	void addRole(Role role);

	List<Role> findAll();
	
	List<Role> findByIds(Integer[] ids);

	List<Permission> findPermsByRoleId(Integer id);
}
