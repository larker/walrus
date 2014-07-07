package org.j4.service;

import java.util.Date;

import org.j4.dao.BaseDao;
import org.j4.model.Log;
import org.j4.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("logService")
public class LogService {
	@Autowired
	private BaseDao dao;
	
	public void addLog(String user, String note) {
		Log log = new Log();
		log.setIp(WebUtils.getIpAddr());
		log.setBroswer(WebUtils.getBrowse());
		log.setDate(new Date());
		log.setUser(user);
		log.setNote(note);
		dao.save(log);
	};
}
