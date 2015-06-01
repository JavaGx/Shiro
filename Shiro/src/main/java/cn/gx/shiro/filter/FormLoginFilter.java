package cn.gx.shiro.filter;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class FormLoginFilter extends FormAuthenticationFilter {

	private HttpServletRequest req;
	private HttpServletResponse resp;
	
	@Override
	protected boolean executeLogin(ServletRequest request,
			ServletResponse response) throws Exception {
		 this.req = (HttpServletRequest) request;
		 this.resp = (HttpServletResponse) response;
		UsernamePasswordToken token=new UsernamePasswordToken(getUsername(req),getPassword(req)); 
		Subject subject = SecurityUtils.getSubject();
		boolean authenticated = subject.isAuthenticated();
		if(!authenticated){
			try{
				token.setRememberMe(true);
				subject.login(token);
				return onLoginSuccess(token, subject, req, resp);
			} catch (AuthenticationException e) {
				/*String message = e.getClass().getSimpleName();
				if ("IncorrectCredentialsException".equals(message)) {
					model.addAttribute("message", "密码错误");
				} else if ("UnknownAccountException".equals(message)) {
					model.addAttribute("message", "账号不存在");
				} else if ("LockedAccountException".equals(message)) {
					model.addAttribute("message", "账号被锁定");
				} else {
					model.addAttribute("message", "未知错误");
				}*/
				e.printStackTrace();
				return onLoginFailure(token, e, req, resp);
			}
		}else{
			return false;
		}
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		this.req = (HttpServletRequest) request;
		this.resp = (HttpServletResponse) response;
		resp.sendRedirect(req.getContextPath()+"/user/login.do");
		return false;
	}

}
