package org.j4.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.j4.model.Banner;
import org.j4.model.Box;
import org.j4.model.Log;
import org.j4.model.Rubric;
import org.j4.model.Site;
import org.j4.model.Slide;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
@Repository("baseDao")
public class BaseHibernateDao extends HibernateDaoSupport implements BaseDao, Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void save(Site site) {
		getHibernateTemplate().merge(site);
	}

	public void save(Rubric rubric) {
		getHibernateTemplate().update(rubric);
	}

	public void save(Box box) {
		getHibernateTemplate().saveOrUpdate(box);
	}

	public void addRubric(Rubric rubric) {
		getHibernateTemplate().persist(rubric);
	}

	public void deleteRubric(Rubric r) {
		getHibernateTemplate().delete(r);
	}

	public Rubric getRubric(long id) {
		return (Rubric) getHibernateTemplate().get(Rubric.class, id);
	}

	public Site getSite(String host, String language) {
		Site site = new Site();
		site.setLanguage(language);
		site.setHost(host);
		return (Site) getListHead(getHibernateTemplate().findByExample(site, 0, 1));
	}

	private Object getListHead(List<?> list) {
		if (null != list && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public void deleteBanner(Banner banner) {
		getHibernateTemplate().delete(banner);
	}

	@SuppressWarnings("unchecked")
	public List<Site> getAllSites() {
		return getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(Site.class).addOrder(Order.asc("index")));
	}

	public void deleteSite(Site site) {
		getHibernateTemplate().delete(site);
	}

	public void delete(Slide slide) {
		getHibernateTemplate().delete(slide);
	}	

	@Override
	public void save(Slide slide) {
		getHibernateTemplate().saveOrUpdate(slide);
	}

	@Override
	public void save(Log log) {
		getHibernateTemplate().save(log);
	}


}
