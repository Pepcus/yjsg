package com.pepcus.appstudent.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface MediaHandler {

	public void init();

	public String save(String mediaKey, String mediaName, String contentType, InputStream in) throws IOException;

	public String save(String bucketName, String mediaKey, String mediaName, String contentType, InputStream in)
			throws IOException;

	public void remove(String mediaKey, String mediaName) throws IOException;

	public void remove(String bucketName, String mediaKey, String mediaName) throws IOException;

	public InputStream load(String mediaKey, String mediaName) throws IOException;

	public FileStream load(String bucketName, String mediaKey, String mediaName) throws IOException;
	
	public String getFileUrl(String mediaKey, String mediaName, String bucketName) throws IOException;

	public String loadText(String mediaKey, String mediaName) throws IOException;

	public List<String> uploadFiles(String bucketName, String mediaKey, List<File> files) throws IOException;

	// helper methods

	/**
	 * 
	 * @param mediaKey
	 *            - path to media
	 * @param mediaName
	 *            - media name
	 * @param mediaSufix
	 *            - suffix being used between media name and extension
	 * @return
	 */
	default public String filename(String mediaKey, String mediaName, String mediaSufix) {
		int extPos = mediaName.lastIndexOf('.');
		if (extPos > 0) {
			return filename(mediaKey, mediaName.substring(0, extPos) + "-" + mediaSufix + mediaName.substring(extPos));
		} else {
			return filename(mediaKey, mediaName);
		}
	}

	default public String filename(String mediaKey, String mediaName) {
		return path(mediaKey) + mediaName;
	}

	default public String paths(String... paths) {
		StringBuilder sb = new StringBuilder();
		for (String p : paths) {
			sb.append(path(p));
		}
		return sb.toString();
	}

	default public String path(String path) {
		if (path == null || path.isEmpty()) {
			return "";
		} else {
			return path + (path.endsWith(getPathSeparator()) ? "" : getPathSeparator());
		}
	}

	default public String getPathSeparator() {
		return "/";
	}

}
