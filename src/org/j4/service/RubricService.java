package org.j4.service;

import java.util.Date;
import java.util.Iterator;

import org.j4.dao.BaseDao;
import org.j4.model.Rubric;
import org.j4.model.Site;
import org.j4.tools.CmsResponce;
import org.j4.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
@Service("rubricService")
public class RubricService {
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	@Autowired
	private SiteService siteService;

	@Autowired
	BaseDao baseDao;

	/**
	 * Returns rubric by id. The search is performed in all sites.
	 * 
	 * @param id
	 * @return
	 */
	public Rubric get(long id) {
		return findRubricInAllSites(id);
	}

	private Rubric findRubricInAllSites(long id) {
		for (Site site : siteService.getAllSites()) {
			Rubric r = getRubric(site.getRootRubric(), id, null);
			if (null != r) {
				return r;
			}
			//return getRubric(site.getRootRubric(), id, null);
		}
		return null;
	}

	/**
	 * Finds rubric recursively by it's id or permalink
	 * 
	 * @param parent
	 *            parent rubric to start search from
	 * @param id
	 *            id of rubric we are looking
	 * @param permalink
	 *            permalink of rubric we are looking
	 * @return
	 */
	private Rubric getRubric(Rubric parent, long id, String permalink) {
		if (null == parent) {
			return null;
		}
		if ((StringUtils.hasText(permalink) && permalink.equals(parent.getUrl())) || parent.getId() == id) {
			return parent;
		}
		for (Iterator<Rubric> i = parent.getChildren().iterator(); i.hasNext();) {
			Rubric ret = getRubric(i.next(), id, permalink);
			if (null != ret) {
				return ret;
			}
		}
		return null;
	}

	/**
	 * Gets rubric from site by rubrics permalink
	 * 
	 * @param site
	 * @param permalink
	 * @return
	 */
	public Rubric getRubric(Site site, String permalink) {
		if (null == site || !StringUtils.hasText(permalink)) {
			return null;
		}
		return getRubric(site.getRootRubric(), 0, permalink);
	}

	/**
	 * Adds a rubric. Rubric must have parent rubric set.
	 * 
	 * @param rubric
	 *            rubric to add
	 * @param rubricIndex
	 *            index at which rubric should be added to parent's child list
	 */
	@Transactional
	public void addRubric(Rubric rubric, int rubricIndex) {
		rubric.getParent().addChild(rubric, rubricIndex);
		baseDao.save(rubric.getParent());
	}

	/**
	 * Adds a rubric, rubric must have parent rubric set.
	 */
	@Transactional
	public void add(Rubric o) {
		addRubric(o, 0);
	}

	/**
	 * Deletes a rubric
	 * 
	 * @param r
	 *            rubric to delete
	 */
	@Transactional
	public void delete(Rubric r) {
		if (null != r) {
			if (null != r.getParent()) {
				r.getParent().deleteChild(r);
				baseDao.deleteRubric(r);
				baseDao.save(r.getParent());
			}
		}
	}

	/**
	 * Persists an object
	 * 
	 * @param context
	 */
	@Transactional
	public void save(Rubric rubric) {
		baseDao.save(rubric);
	}

	/**
	 * Moves rubric to another parent
	 * 
	 * @param to
	 *            destination rubric
	 * @param subject
	 *            rubric to move
	 * @param rubricIndex
	 *            index at which rubric should be places in destination children
	 *            list
	 */
	@Transactional
	public void moveSubrubricToRubric(Rubric to, Rubric subject, int rubricIndex) {
		to.addChild(subject, rubricIndex);
		subject.setOrderno(rubricIndex);
		save(to);
		save(subject);
	}
	/**
	 * 创建
	 */
	@Transactional
	public CmsResponce createRubric(String name, Rubric currRubric) {
		Rubric r = new Rubric(name);
		r.setParent(currRubric);
		this.addRubric(r, 0);
		return new CmsResponce(true, "保存成功", name);
	}
	/**
	 * 更新标题
	 */
	@Transactional
	public CmsResponce saveRubricTitle(String rubricId, String newValue) {
		Rubric rubric = this.get(Long.valueOf(rubricId));
		rubric.setTitle(newValue);
		this.save(rubric);
		return new CmsResponce(true, "保存成功", newValue);
	}
	/**
	 * 更新时间
	 */
	@Transactional
	public CmsResponce saveRubricDate(String rubricId, String newValue) {
		Rubric rubric = this.get(Long.valueOf(rubricId));
		rubric.setDate(newValue);
		this.save(rubric);
		return new CmsResponce(true, "保存成功", newValue);
	}
	/**
	 * 更新摘要
	 */
	@Transactional
	public CmsResponce saveRubricAbstract(String rubricId, String newValue) {
		Rubric rubric = this.get(Long.valueOf(rubricId));
		rubric.setAbstr(newValue);
		this.save(rubric);
		return new CmsResponce(true, "保存成功", newValue);
	}
	/**
	 * 更新正文
	 */
	@Transactional
	public CmsResponce saveRubricBody(String rubricId, String newValue) {
		Rubric rubric = this.get(Long.valueOf(rubricId));
		rubric.setBody(newValue);
		this.save(rubric);
		return new CmsResponce(true, "保存成功", newValue);
	}
	
	
	/**
	 * 配置是否可永远访问
	 */
	@Transactional
	public String setVisibleForever(String rubricId, String visible) {
		Rubric rubric = this.get(Long.valueOf(rubricId));
		rubric.setVisibleForever("true".equals(visible));
		this.save(rubric);
		return "01";
	}
	/**
	 * 是否子区块
	 */
	@Transactional
	public String setLeaf(String rubricId, String leaf) {
		Rubric rubric = this.get(Long.valueOf(rubricId));
		rubric.setLeaf("true".equals(leaf));
		this.save(rubric);
		return "01";
	}
	/**
	 * 设置可访问时间段
	 */
	@Transactional
	public String[] setVisibleFrom(String rubricId, String date) {
		String[] arr = new String[2];
		Rubric rubric = this.get(Long.valueOf(rubricId));
		Date from = DateUtils.parseDate(date, "yyyy-MM-dd");
		if (datesAreFine(from, rubric.getVisibleTo())) {
			rubric.setVisibleFrom(from);
			this.save(rubric);
			arr[0] = "01";
		} else {
			arr[0] = "开始时间不能大于结束时间！";
			arr[1] = DateUtils.format(rubric.getVisibleFrom(), "yyyy-MM-dd");
		}
		return arr;
	}
	/**
	 * 设置可访问时间段
	 */
	@Transactional
	public String[] setVisibleTo(String rubricId, String date) {
		String[] arr = new String[2];
		Rubric rubric = this.get(Long.valueOf(rubricId));
		Date to = DateUtils.parseDate(date, new String[] {"yyyy-MM-dd"});
		if (datesAreFine(rubric.getVisibleFrom(), to)) {
			rubric.setVisibleTo(to);
			this.save(rubric);
			arr[0] = "01";
		} else {
			arr[0] = "结束时间不能小于开始时间！";
			arr[1] = DateUtils.format(rubric.getVisibleFrom(), "yyyy-MM-dd");
		}
		return arr;
	}
	private boolean datesAreFine(Date from, Date to) {
		return (null != from && null != to)
				&& (from.before(to) || from.equals(to));
	}
	
	/**
	 * 区块类型
	 */
	@Transactional
	public String setRubricMode(String rubricId, String mode) {
		Rubric rubric = this.get(Long.valueOf(rubricId));
		rubric.setMode( Enum.valueOf(Rubric.Mode.class, mode));
		this.save(rubric);
		return "01";
	}
	
	/**
	 * 发布
	 */
	@Transactional
	public String publishArticle(String rubricId) {
		Rubric rubric = this.get(Long.valueOf(rubricId));
		rubric.setOnline(true);
		this.save(rubric);
		return "01";
	}
	/**
	 * 不发布
	 */
	@Transactional
	public String unpublishArticle(String rubricId) {
		Rubric rubric = this.get(Long.valueOf(rubricId));
		rubric.setOnline(false);
		this.save(rubric);
		return "01";
	}
	
}
