package cn.gx.dao;

import java.util.List;

import cn.gx.dao.base.BaseDao;
import cn.gx.entity.Role;
import cn.gx.entity.User;

public interface UserDao extends BaseDao<User> {

	User findUserByUsername(String currentUsername);

	List<User> pageRecords(int beginIndex, int endIndex);

	int findRecords();

	List<Role> getUsersRole(Integer id);

	String findEmail(String email);

}
