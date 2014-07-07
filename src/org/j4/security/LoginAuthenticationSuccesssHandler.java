package org.j4.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.j4.service.LogService;
import org.j4.utils.EhcacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
/**
 * 登录成功处理
 * @author jiang
 *
 */
public class LoginAuthenticationSuccesssHandler implements
		AuthenticationSuccessHandler {

	private String defaultUrl;
	@Autowired
	private LogService logService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		//登录成功后，关闭页面缓存
		EhcacheUtil.setCacheDisabled(true);
		response.sendRedirect(request.getContextPath() + defaultUrl);
		logService.addLog(authentication.getName(), "login");
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

}
