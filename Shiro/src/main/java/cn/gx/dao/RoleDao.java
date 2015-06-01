package cn.gx.dao;


import java.util.List;

import cn.gx.dao.base.BaseDao;
import cn.gx.entity.Permission;
import cn.gx.entity.Role;

public interface RoleDao extends BaseDao<Role> {

	List<Permission> getRolesPerm(Integer id);

}
