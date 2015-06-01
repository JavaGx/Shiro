package cn.gx.shiro.realm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

import cn.gx.entity.Permission;
import cn.gx.entity.Role;
import cn.gx.entity.User;
import cn.gx.service.RoleService;
import cn.gx.service.UserService;
import cn.gx.util.SessionUtils;

public class MyRealm extends AuthorizingRealm {

	@Resource
	private UserService us;
	@Resource
	private RoleService rs;
	
	/**
	 * 权限验证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		User user=(User) SessionUtils.getSession().getAttribute("user");
		
		if(user!=null){
			
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			
			List<String> roleList=new ArrayList<String>();
			List<String> permList=new ArrayList<String>();
			
			List<Role> roles = user.getRoles();
			if(roles!=null&&roles.size()>0){
				for(Role role:roles){
					roleList.add(role.getRole_name());
					List<Permission> perms =role.getPerms();
					if(perms!=null&&perms.size()>0){
						for(Permission perm:perms){
							permList.add(perm.getPermission_name());
						}
					}
				}
			}
			
			info.addRoles(roleList);
			
			info.addStringPermissions(permList);
			return info;
		}
		
		return null;
	}


	/**
	 * 登录验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token=(UsernamePasswordToken) authcToken;
		String currentUsername=token.getUsername();
		User user= us.findUserByUsername(currentUsername);
		if(user!=null){
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(),this.getName());
			
			List<Role> roles = us.findRolesByUserId(user.getId());
			
			if(roles!=null&&roles.size()>0){
				
				for(Role role:roles){
					List<Permission> perms =rs.findPermsByRoleId(role.getId());
					
					if(perms!=null&&perms.size()>0){
						role.setPerms(perms);
					}
				}
				user.setRoles(roles);
			}
			
			Session session=SessionUtils.getSession();
		    if(session!=null){
		    	session.setAttribute("user", user);
		    }
			return info;
			
		}else{
			return null;
		}
	}

}
