package org.j4.service;

import java.util.HashMap;

import org.j4.dao.BaseDao;
import org.j4.model.Banner;
import org.j4.model.Box;
import org.j4.model.Slide;
import org.j4.model.SlideshowBox;
import org.j4.model.TextBox;
import org.j4.tools.CmsResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("boxService")
public class BoxService {
	@Autowired
	private BaseDao dao;
	@Transactional
	public void save(Box box) {
		dao.save(box);
	}
	@Transactional
	public void deleteBanner(Banner banner) {
		dao.deleteBanner(banner);
	}
	@Transactional
	public void deleteSlide(Slide slide) {
		dao.delete(slide);
	}

	@Transactional
	public CmsResponce saveBoxTitle(TextBox textBox, String newValue) {
		textBox.setTitle(newValue);
		this.save(textBox);
		return new CmsResponce(true, "保存成功", newValue);
	}
	@Transactional
	public CmsResponce saveBoxBody(TextBox textBox, String newValue) {
		textBox.setBody(newValue);
		this.save(textBox);
		return new CmsResponce(true, "保存成功", newValue);
	}
	
	
	@Transactional
	public CmsResponce addSlide(SlideshowBox slideshow, String slideTitle) {
		Slide newSlide = new Slide();
		newSlide.setTitle(slideTitle);
		slideshow.addSlide(newSlide);
		dao.save(slideshow);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("slideId", newSlide.getId());
		params.put("title", newSlide.getTitle());
		params.put("slideshow", slideshow.getBoxId());
		params.put("body", newSlide.getBody());
		return new CmsResponce(true, "保存成功", params);
	}
}
