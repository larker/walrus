package org.j4.service;

import org.j4.dao.BaseDao;
import org.j4.model.Slide;
import org.j4.model.SlideshowBox;
import org.j4.tools.CmsResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service("slideService")
public class SlideService {
	
	@Autowired
	BaseDao baseDao;
	
	
	@Transactional
	public CmsResponce saveSlideTitle(Slide slide, String value) {
		slide.setTitle(value);
		baseDao.save(slide);
		return new CmsResponce(true, "保存成功", value);
	}
	@Transactional
	public CmsResponce saveSlideBody(Slide slide, String value) {
		slide.setBody(value);
		baseDao.save(slide);
		return new CmsResponce(true, "保存成功");
	}
	@Transactional
	public CmsResponce saveSlideShortcut(SlideshowBox slideshow, Slide slide, String value) {
		Slide partner;
		
		int direction = Integer.parseInt(value);
		
		if (-1 == direction) {
			partner = slideshow.getPrevious((Slide) slide);
		} else {
			partner = slideshow.getNext((Slide) slide);
		}
		
		int orderno = partner.getOrderno();
		partner.setOrderno(((Slide) slide).getOrderno());
		((Slide) slide).setOrderno(orderno);

		slideshow.sortSlides();
		baseDao.save(slideshow);
		
		return new CmsResponce(true, "保存成功");
	}
	@Transactional
	public CmsResponce deleteSlide(SlideshowBox slideshow2, long slideId2) {
		SlideshowBox slideshow = slideshow2;
		Slide slide = slideshow.getSlide(slideId2);
		slideshow.deleteSlide(slide);
		baseDao.save(slideshow);
		baseDao.delete(slide);
		return new CmsResponce(true, "删除成功");
	}
	
}
