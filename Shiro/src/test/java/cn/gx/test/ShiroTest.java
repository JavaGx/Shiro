package cn.gx.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.gx.entity.Permission;
import cn.gx.entity.Role;
import cn.gx.entity.User;
import cn.gx.service.RoleService;
import cn.gx.service.UserService;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})*/
public class ShiroTest{
	
	@Resource
	private RoleService s;
	@Resource
	private UserService us;

	@Test
	public void loginTest(){
		Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager=factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken("aaa", "111");
		try {
			subject.login(token);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		if(subject.isAuthenticated()){
			System.out.println("登录成功");
		}
	}
	
	@Test
	public void loggingData(){
		/*Role role=new Role();
		role.setRole_name("admin");
		role.setDescription("管理员");
		List<Permission> perms=new ArrayList<Permission>();
		perms.add(new Permission("addUser","/register"));
		perms.add(new Permission("deleteUser","/delete"));
		perms.add(new Permission("updateUser","/register"));
		perms.add(new Permission("findAll","/findAll"));
		role.setPerms(perms);
		s.addRole(role);
		*/
		
		Role role=new Role();
		role.setRole_name("user");
		role.setDescription("普通用户");
		List<Permission> perms=new ArrayList<Permission>();
		perms.add(new Permission(1,"addUser","/register"));
		perms.add(new Permission(3,"updateUser","/register"));
		role.setPerms(perms);
		s.addRole(role);
	}
	
	@Test
	public void testUser(){
		User user=new User();
		user.setUsername("aa");
		user.setPassword("123");
		us.register(user, new Integer[]{1});
	}
}
