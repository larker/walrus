package org.j4.tools;

import java.io.File;
import java.io.FilenameFilter;

import org.springframework.util.StringUtils;

public class UploadedFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		if (! StringUtils.hasText(name)) {
			return false;
		}
		File test = new File(dir.getAbsoluteFile() + File.separator + name);
		return	null != test &&
				test.isFile() &&
				! test.isHidden() &&
				! test.isDirectory() &&
				test.canRead();
	}
}
