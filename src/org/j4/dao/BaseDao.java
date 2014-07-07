package org.j4.dao;

import java.util.List;

import org.j4.model.Banner;
import org.j4.model.Box;
import org.j4.model.Log;
import org.j4.model.Rubric;
import org.j4.model.Site;
import org.j4.model.Slide;

public interface BaseDao {
	
	public void save(Site site);
	
	public void save(Rubric rootRubric);
	
	public void addRubric(Rubric rubric);
	
	public void save(Box box);
	
	public void save(Slide slide);

	public Site getSite(String host, String language);

	public Rubric getRubric(long id);
	
	public List<Site> getAllSites();
	
	public void deleteRubric(Rubric r);

	public void deleteBanner(Banner banner);

	public void deleteSite(Site site);

	public void delete(Slide slide);
	
	public void save(Log log);

}
