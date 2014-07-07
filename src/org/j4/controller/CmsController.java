package org.j4.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.j4.model.Banner;
import org.j4.model.BannerBox;
import org.j4.model.Box;
import org.j4.model.ImageBox;
import org.j4.model.Rubric;
import org.j4.model.Site;
import org.j4.model.Slide;
import org.j4.model.SlideshowBox;
import org.j4.model.TextBox;
import org.j4.service.BoxService;
import org.j4.service.FileService;
import org.j4.service.RubricService;
import org.j4.service.SiteService;
import org.j4.service.SlideService;
import org.j4.tools.CmsResponce;
import org.j4.tools.EditedEntity;
import org.j4.tools.UploadedFileFilter;
import org.j4.tools.UploadedImageFilter;
import org.j4.utils.EhcacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cms")
public class CmsController {
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
	@Autowired
	SiteService siteService;
	@Autowired
	RubricService rubricService;
	@Autowired
	BoxService boxService;
	@Autowired
	SlideService slideService;
	@Autowired
	protected FileService fileService;
	
	@Resource(name = "fileUrl")
	private String fileUrl;
	
	protected int thumbSize = 100;
	
	@RequestMapping("/saveField")
	@ResponseBody
	public Object saveField(HttpServletRequest request, String id, String value) {
		
		CmsResponce response = null;
		EditedEntity entity = new EditedEntity(id);
		logger.debug("entity:" + entity);

		try {
			if (entity.isEntity("rubric")) {
				if (entity.isField("title")) {
					response = rubricService.saveRubricTitle(entity.getId(), value);
				} else if (entity.isField("body")) {
					response = rubricService.saveRubricBody(entity.getId(), value);
				} else if (entity.isField("abstract")) {
					response = rubricService.saveRubricAbstract(entity.getId(), value);
				} else if (entity.isField("date")) {
					response = rubricService.saveRubricDate(entity.getId(), value);
				}
			} else if (entity.isEntity("box")) {
				if (entity.isField("title")) {
					response = boxService.saveBoxTitle((TextBox) siteService.getSite(request).getBox(entity.getId()), value);
				} else if (entity.isField("body")) {
					response = boxService.saveBoxBody((TextBox) siteService.getSite(request).getBox(entity.getId()), value);
				}
			} else if (entity.isEntity("slide")) {
				SlideshowBox slideshow = siteService.getSite(request).findSlideshow(Long.valueOf(entity.getId()));
				Slide slide = slideshow.getSlide(Long.valueOf(entity.getId()));
				if (entity.isField("body")) {
					response = slideService.saveSlideBody(slide, value);
				} else if (entity.isField("title")) {
					response = slideService.saveSlideTitle(slide, value);
				} else if (entity.isField("shortcut")) {
					response = slideService.saveSlideShortcut(slideshow, slide, value);
				} else {
					logger.warn("滑动列表不存在: " + entity);
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
			response = new CmsResponce(false, "出现异常"); 
		}

		if (null == response) {
			response = new CmsResponce(false, "出现错误"); 
		}
		return response;
	}
	
	@RequestMapping("/newSubRubric")
	@ResponseBody
	public Object newSubRubric(HttpServletRequest request, String currentRubricId, String value) {
		CmsResponce response = null;
		try {
			Rubric currRubric = rubricService.get(Long.valueOf(currentRubricId));
			response = rubricService.createRubric(value, currRubric);
		} catch (Exception ex) {
			logger.error(ex);
			response = new CmsResponce(false, "出现异常"); 
		}
		if (null == response) {
			response = new CmsResponce(false, "出现错误"); 
		}
		return response;
	}
	@RequestMapping("/deleteRubric")
	@ResponseBody
	public Object deleteRubric(HttpServletRequest request, String currentRubricId, String deleteRubricId) {
		try {
			if (currentRubricId.equals(deleteRubricId)) {
				return new CmsResponce(false, "不能删除正在展示的区块");
			}
			Rubric delRubric = rubricService.get(Long.valueOf(deleteRubricId));
			if (null != delRubric) {
				rubricService.delete(delRubric);
				return new CmsResponce(true, "删除成功");
			} else {
				return new CmsResponce(false, "出现错误");
			}
		} catch (Exception ex) {
			logger.error(ex);
			return new CmsResponce(false, "出现异常");
		}
	}
	
	
	@RequestMapping("/newSlide")
	@ResponseBody
	public Object newSlide(HttpServletRequest request, String slideshowId, String title) {
		CmsResponce response = null;
		try {
			if (! StringUtils.hasText(title)) {
				response = new CmsResponce(false, "滚动列标题不能为空");
			} else {
				
				if (!StringUtils.hasText(slideshowId)) {
					response = new CmsResponce(false, "滚动列id为空");
				} else {
					SlideshowBox slideshow = (SlideshowBox) siteService.getSite(request).getBox(slideshowId);
					if (null == slideshow) {
						response = new CmsResponce(false, "滚动列不存在");
					} else {
						response = boxService.addSlide(slideshow, title);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
			response = new CmsResponce(false, "出现异常");
		}
		return response;
	}
	@RequestMapping("/deleteSlide")
	@ResponseBody
	public Object deleteSlide(HttpServletRequest request, String slideId, String slideshowId) {
		CmsResponce response;
		try {
			response = null;
			SlideshowBox slideshow = (SlideshowBox) siteService.getSite(request).getBox(slideshowId);
			if (null == slideshow) {
				response = new CmsResponce(false, "滚动列不存在");
			} else {
				response = slideService.deleteSlide(slideshow, Long.valueOf(slideId));
			}
		} catch (Exception e) {
			logger.error(e);
			response = new CmsResponce(false, "出现异常");
		}
		return response;
	}
	
	
	@RequestMapping("/list")
	public ModelAndView listFiles(HttpServletRequest request, HttpServletResponse response, String type) {
		File destDir = new File(getFileRealPath(request));
		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("contextPath", request.getContextPath());
		logger.debug("DEST DIR: " + destDir.getAbsolutePath());
		FilenameFilter filter = (null != type && "image".equals(type)) ? new UploadedImageFilter() : new UploadedFileFilter();
		File[] fileList = destDir.listFiles(filter);
		List<File> list = new ArrayList<File>(Arrays.asList(fileList));
		Collections.sort(list);
		model.put("list", list);
		model.put("type", (null != type ? type : null));
		model.put("showFiles", true);
		
		return new ModelAndView("fileList", "model", model);
	}
	/**banner图片操作**/
	@RequestMapping("/bannerList")
	public ModelAndView bannerList(String boxId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("bannerList");
		if(null != boxId) {
			Box box = siteService.getSite(request).getBox(boxId);
			if(null != box) {
				BannerBox bannerBox = (BannerBox) box;
				mav.addObject("contextPath", request.getContextPath());
				mav.addObject("banners", bannerBox.getBanners());
				mav.addObject("boxId", boxId);
			}
		}
		return mav;
	}
	@RequestMapping("/deleteBanner")
	@ResponseBody
	public Object deleteBanner(String boxId, String bannerId, HttpServletRequest request) {
		Site site = siteService.getSite(request);
		BannerBox box = (BannerBox) site.getBox(boxId);
		if(null!=box) {
			Banner banner = box.getBanner(Integer.valueOf(bannerId));
			if(null != banner) {
				fileService.deleteFile(banner.getBanner(), getRealPath(request));
				box.getBanners().remove(banner);
				boxService.deleteBanner(banner);
				boxService.save(box);
				Banner b = box.getRandomBanner();
				if(null != b) {
					return new CmsResponce(true, "删除成功", makeChangeBannerResponse(box.getBoxId(), getBaseUrl(request) + b.getBanner(), b.getUrl()));
				} else {
					return new CmsResponce(true, "删除成功",  makeChangeBannerResponse(box.getBoxId(), "", ""));
				}
			} else {
				return new CmsResponce(false, "删除失败" + boxId);
			}
		} else {
			return new CmsResponce(false, "删除失败 " + boxId);
		}
	}
	@RequestMapping("/addBanner")
	@ResponseBody
	public Object addBanner(MultipartFile file, String boxId, String url, HttpServletRequest request) {
		if (null == file) {
			return new CmsResponce(false, "请选择图片");
		}
		if (null != boxId) {
			Site site = siteService.getSite(request);
			BannerBox box = (BannerBox) site.getBox(boxId);

			if (null == box) {
				return new CmsResponce(false, "出现错误" + boxId + ")");
			}

			if (null != file && file.getSize() > 0 && StringUtils.hasText(url)) {
				if (!fileService.isImage(file)) {
					return new CmsResponce(false, "图片格式错误，仅支持 JPG, GIF and PNG 格式图片");
				}

				String newFileName;
				try {
					newFileName = fileService.putFileToPlace(file, getFileRealPath(request));
				} catch (Exception ex) {
					return new CmsResponce(false, "图片生成错误，请注意路径配置： /WEB-INF/classes/cms.properties"
							+ ex);
				}

				Banner b = new Banner();
				b.setBanner(getFileUrl() + "/" + newFileName);
				b.setUrl(url);

				box.getBanners().add(b);
				
				boxService.save(box);
				return new CmsResponce(true, "添加成功", makeChangeBannerResponse(box.getBoxId(), b.getBanner(), b.getUrl()));
			} else {
				return new CmsResponce(false, "图片链接 URL 不能为空");
			}
		}

		return new CmsResponce(false, "出现错误");
	}
	@RequestMapping("/updateBannerUrl")
	@ResponseBody
	public Object updateBannerUrl(MultipartFile file, String boxId, String bannerId, String url, HttpServletRequest request) {
		if (null != boxId) {
			Site site = siteService.getSite(request);
			BannerBox box = (BannerBox) site.getBox(boxId);

			if (null == box) {
				return new CmsResponce(false, "出现错误 " + boxId);
			}

			if (StringUtils.hasText(url)) {
				Banner b = box.getBanner(Long.valueOf(bannerId));
				if (null != b) {
					b.setUrl(url);

					if (null != file && file.getSize() > 0) {
						if (!fileService.isImage(file)) {
							return new CmsResponce(false, "图片格式错误，仅支持 JPG, GIF and PNG 格式图片");
						}
						String newFileName;
						try {
							newFileName = fileService.putFileToPlace(file, getFileRealPath(request));
							b.setBanner(getFileUrl() + "/" + newFileName);
						} catch (Exception ex) {
							return new CmsResponce(false, "图片生成错误，请注意路径配置： /WEB-INF/classes/cms.properties"
									+ ex);
						}
					}
					boxService.save(box);
					return new CmsResponce(true, "修改成功", makeChangeBannerResponse(box.getBoxId(), getBaseUrl(request) + b.getBanner(), b.getUrl()));
				} else {
					return new CmsResponce(false, "出现错误" + bannerId);
				}
			} else {
				return new CmsResponce(false, "图片链接 URL 不能为空");
			}
		}

		return new CmsResponce(false, "出现错误");
	}
	private HashMap<String, Object> makeChangeBannerResponse(String boxId, String banner, String url) {
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put("boxId", boxId);
		options.put("banner", banner);
		options.put("link", url);
		return options;
	}
	
	@RequestMapping("/deleteFile")
	@ResponseBody
	public Object deleteFile(String fileName, String type, HttpServletRequest request) {
		CmsResponce response;
		try {
			fileService.deleteFile(fileName, getFileRealPath(request)+"/");
			return new CmsResponce(true, "删除成功");
		} catch (Exception e) {
			logger.error(e);
			response = new CmsResponce(false, "出现异常");
		}
		return response;
	}
	/**banner图片操作**/
	/****/
	@RequestMapping("/setVisibleForever")
	@ResponseBody
	public Object setVisibleForever(String rubricId, String visible) {
		CmsResponce response;
		try {
			String result = rubricService.setVisibleForever(rubricId, visible);
			if ("01".equals(result)) {
				response = new CmsResponce(true, "保存成功");
			} else {
				response = new CmsResponce(false, result);
			}
		} catch (Exception e) {
			response = new CmsResponce(false, "出现异常");
			logger.error(e);
		}
		return response;
	}
	@RequestMapping("/setVisibleFrom")
	@ResponseBody
	public Object setVisibleFrom(String rubricId, String date) {
		CmsResponce response;
		try {
			String[] result = rubricService.setVisibleFrom(rubricId, date);
			if ("01".equals(result[0])) {
				response = new CmsResponce(true, "保存成功");
			} else {
				response = new CmsResponce(false, result[0], result[1]);
			}
		} catch (Exception e) {
			response = new CmsResponce(false, "出现异常");
			logger.error(e);
		}
		return response;
	}
	@RequestMapping("/setVisibleTo")
	@ResponseBody
	public Object setVisibleTo(String rubricId, String date) {
		CmsResponce response;
		try {
			String[] result = rubricService.setVisibleTo(rubricId, date);
			if ("01".equals(result[0])) {
				response = new CmsResponce(true, "保存成功");
			} else {
				response = new CmsResponce(false, result[0], result[1]);
			}
		} catch (Exception e) {
			response = new CmsResponce(false, "出现异常");
			logger.error(e);
		}
		return response;
	}
	@RequestMapping("/setRubricMode")
	@ResponseBody
	public Object setRubricMode(String rubricId, String mode) {
		CmsResponce response;
		try {
			String result = rubricService.setRubricMode(rubricId, mode);
			if ("01".equals(result)) {
				response = new CmsResponce(true, "保存成功");
			} else {
				response = new CmsResponce(false, result);
			}
		} catch (Exception e) {
			response = new CmsResponce(false, "出现异常");
			logger.error(e);
		}
		return response;
	}
	@RequestMapping("/publishArticle")
	@ResponseBody
	public Object publishArticle(String rubricId) {
		CmsResponce response;
		try {
			String result = rubricService.publishArticle(rubricId);
			if ("01".equals(result)) {
				response = new CmsResponce(true, "保存成功");
			} else {
				response = new CmsResponce(false, result);
			}
		} catch (Exception e) {
			response = new CmsResponce(false, "出现异常");
			logger.error(e);
		}
		return response;
	}
	@RequestMapping("/unpublishArticle")
	@ResponseBody
	public Object unpublishArticle(String rubricId) {
		CmsResponce response;
		try {
			String result = rubricService.unpublishArticle(rubricId);
			if ("01".equals(result)) {
				response = new CmsResponce(true, "保存成功");
			} else {
				response = new CmsResponce(false, result);
			}
		} catch (Exception e) {
			response = new CmsResponce(false, "出现异常");
			logger.error(e);
		}
		return response;
	}
	
	@RequestMapping("/uploadFile")
	@ResponseBody
	public Object uploadFile(MultipartFile file, HttpServletRequest request) {
		if (null != file) {
			String newFileName;
			try {
				newFileName = fileService.putFileToPlace(file, getFileRealPath(request));
			} catch (Exception ex) {
				return new CmsResponce(false, "图片生成错误，请注意路径配置： /WEB-INF/classes/cms.properties");
			}
			return new CmsResponce(true, "保存成功", getFileUrl() + "/" + newFileName);
		} else {
			return new CmsResponce(false, "文件不能为空");
		}
	}
	
	
	
	@RequestMapping("/updateImage")
	@ResponseBody
	public Object updateImage(MultipartFile file, String boxId, HttpServletRequest request) {
		if (null == file) {
			return new CmsResponce(false, "请选择图片");
		}
		if (null != boxId) {
			Site site = siteService.getSite(request);
			ImageBox box = (ImageBox) site.getBox(boxId);

			if (null == box) {
				return new CmsResponce(false, "出现错误" + boxId + ")");
			}
			if (file.getSize() > 0) {
				if (!fileService.isImage(file)) {
					return new CmsResponce(false, "图片格式错误，仅支持 JPG, GIF and PNG 格式图片");
				}
				String newFileName;
				try {
					newFileName = fileService.putFileToPlace(file, getFileRealPath(request));
				} catch (Exception ex) {
					return new CmsResponce(false, "图片生成错误，请注意路径配置： /WEB-INF/classes/cms.properties ");
				}
				box.setImage(getFileUrl() + "/" + newFileName);
				boxService.save(box);
				return new CmsResponce(true, "保存成功", box);
			} else {
				fileService.deleteFile(box.getImage(), getRealPath(request));
				box.setImage("");
				boxService.save(box);
				return new CmsResponce(true, "", "");
			}
		}
		return new CmsResponce(false, "出现错误");
	}

	@RequestMapping("/setCacheDisabled")
	@ResponseBody
	public Object setCacheDisabled(boolean flag) {
		EhcacheUtil.setCacheDisabled(flag);
		return new CmsResponce(true, "设置成功");
	}
	
	protected String getFileBaseUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
				+ fileUrl + "/";
	}

	protected String getBaseUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	public String getFileRealPath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath(getFileUrl());
	}
	public String getRealPath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("/");
	}
}
