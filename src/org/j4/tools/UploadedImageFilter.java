package org.j4.tools;

import java.io.File;


public class UploadedImageFilter extends UploadedFileFilter {
	@Override
	public boolean accept(File dir, String name) {
		return	super.accept(dir, name) &&
				(name.endsWith(".jpg") || name.endsWith(".JPG")) ||
				(name.endsWith(".png") || name.endsWith(".PNG")) ||
				(name.endsWith(".gif") || name.endsWith(".GIF"));
	}
}
