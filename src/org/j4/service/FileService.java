package org.j4.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * A service that handles file operations
 */
@Service("fileService")
public class FileService {
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
	/**
	 * Directory where files are kept on the server

	/**
	 * Creates a unique file name, copies a file to destDir and returns a new
	 * file name
	 * 
	 * @return new filename
	 * @throws IOException
	 */
	public String putFileToPlace(MultipartFile file, String path) throws IOException {
		File newFile = getNewFile(file, path);
		file.transferTo(newFile);
		return newFile.getName();
	}

	private File getNewFile(MultipartFile file, String path) {
		String fileName = fixFileName(file.getOriginalFilename());
		
		File newFile = new File(path,fileName);
		if (newFile.exists()) {
			newFile = new File(path, getDestinationFileName(fileName));
		}
		return newFile;
	}

	private String fixFileName(String fileName) {
		while(fileName.contains("..")){
			fileName = fileName.replace("..", ".");
		}
		
		if(fileName.lastIndexOf(".")==0){
			fileName = System.currentTimeMillis() + fileName;
		}
		if(fileName.startsWith(".")){
			fileName = fileName.substring(1, fileName.length());
		}
		return fileName;
	}

	/**
	 * @param fileName
	 * @return unique file name based on fileName + timestamp +
	 *         originalExtension
	 */
	public static String getDestinationFileName(String fileName) {
		int cutIndex = fileName.lastIndexOf(".");
		String fileExt = "";
		String fileBase = "";
		if (-1 != cutIndex) {
			fileExt = fileName.substring(cutIndex);
			fileBase = fileName.substring(0, fileName.lastIndexOf("."));
			return fileBase + Long.toString(System.currentTimeMillis()) + fileExt;
		}
		return fileName + Long.toString(System.currentTimeMillis());
	}

	/**
	 * @return true if file is jpg, gif or png image
	 */
	public boolean isImage(MultipartFile file) {
		return "image/pjpeg".equals(file.getContentType()) || "image/jpeg".equals(file.getContentType()) || "image/gif".equals(file.getContentType())
				|| "image/png".equals(file.getContentType());
	}

	/**
	 * @return timestamp based unique name for thumbnail. i.e. timestamp.jpg
	 */

	/**
	 * Deletes a specified file
	 * 
	 * @param filePath
	 *            - file to delete
	 * @return true if file is successfully deleted
	 */
	public boolean deleteFile(String fileName, String path) {
		String file = path + fileName;
		File f = new File(file);
		if (!f.exists()) {
			logger.debug("FileService.deleteFile(): can't delete " + fileName + " - not exist");
		}
		if (!f.canWrite()) {
			logger.debug("FileService.deleteFile(): can't delete " + fileName + " - not allowed to delete");
		}
		if (f.isDirectory()) {
			logger.debug("FileService.deleteFile(): can't delete " + fileName + " - not file, but directory");
	    }
		return f.delete();
	}
	
}