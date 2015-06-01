package cn.gx.service;

import java.util.List;

import cn.gx.entity.Role;
import cn.gx.entity.User;


public interface UserService{
	
	User findUserByUsername(String currentUsername);

	List<Role> findAllRoles();

	void register(User user, Integer[] ids);

	List<User> pageList(int beginIndex, int endIndex);

	int findRecords();

	List<User> findAllUsers();

	List<Role> findRolesByUserId(Integer id);

	String findEmail(String email);
	
	
}
