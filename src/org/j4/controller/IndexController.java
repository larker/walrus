package org.j4.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.j4.model.Site;
import org.j4.security.Security;
import org.j4.service.SiteService;
import org.j4.utils.EhcacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/index")
public class IndexController {
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
	@Autowired
	SiteService siteService;
	@Resource(name = "fileUrl")
	private String fileUrl;
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("index");
		try {
			ModelMap model = this.makeModel(request);
			if (null == model.get("currentRubric")) {
				return new ModelAndView("404");
			}
			mav.addObject("model", model);
		} catch (Exception e) {
			logger.error(e);
			mav = new ModelAndView("500");
		}
		return mav;
	}
	
	@RequestMapping("/rubric")
	public ModelAndView rubric(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("rubric");
		try {
			ModelMap model = this.makeModel(request);
			if (null == model.get("currentRubric")) {
				return new ModelAndView("404");
			}
			mav.addObject("model", model);
		} catch (Exception e) {
			logger.error(e);
			mav = new ModelAndView("500");
		}
		return mav;
	}
	
	private ModelMap makeModel(HttpServletRequest request) {
		Site site = siteService.getSite(request);
		ModelMap model = new ModelMap();
		if (null == site) {
			return model;
		}
		model.addAttribute("site", site);
		//model.addAttribute("sites", siteService.getSites("cn"));

		model.addAttribute("currentRubric", siteService.getCurrentRubric(site, request.getParameter("rubricId")));
		if (Security.loggedOnUserHasAdminRole()) {
			model.addAttribute("isAdmin", true);
			// 页面是否启用缓存
			model.addAttribute("isCacheDisabled", EhcacheUtil.isCacheDisabled());
		}

		model.addAttribute("isArchive", "1".equals(request.getParameter("archive")));
		model.addAttribute("fullContextPath", getFullContextPath(request));
		model.addAttribute("contextPath", request.getContextPath());
		model.addAttribute("serverPort", request.getServerPort());
		model.addAttribute("requestURL", request.getRequestURL());
		model.addAttribute("queryString", request.getQueryString());
		model.addAttribute("servletPath", request.getServletPath());
		model.addAttribute("fileUrl", fileUrl);
		return model;
	}
	
	@RequestMapping("/404")
	public ModelAndView _404() {
		return new ModelAndView("404");
	}
	@RequestMapping("/500")
	public ModelAndView _500() {
		return new ModelAndView("500");
	}
	
	public String getFullContextPath(HttpServletRequest request) {
		String port = (80 == request.getServerPort()) ? "" : (":" + request.getServerPort());
		return request.getScheme() + "://" + request.getServerName() + port + request.getContextPath();
	}


	public String getFileUrl() {
		return fileUrl;
	}


	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
}
