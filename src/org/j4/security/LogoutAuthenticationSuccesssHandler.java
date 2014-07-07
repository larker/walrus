package org.j4.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.j4.utils.EhcacheUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class LogoutAuthenticationSuccesssHandler implements
		LogoutSuccessHandler {

	private String defaultUrl;

	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		//退出成功后，清理页面缓存
		EhcacheUtil.clearCache();
		response.sendRedirect(request.getContextPath() + defaultUrl);
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

}
