package com.pepcus.appstudent.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility file to upload file, extract zip file, delete file, delete directory
 * etc.
 * 
 * @author Sandeep Vishwakarma
 * @since 05-02-2020
 *
 */
public class FileUtil {

	/**
	 * Method to upload file
	 * 
	 * @param uploadStream
	 * @param path
	 * @param fileName
	 * @throws IOException
	 */
	public static void uploadFile(InputStream uploadStream, String path, String fileName) throws IOException {

		String filePath = path + fileName;
		File writeDirectory = new File(path);
		try {
			// create output directory if it doesn't exist
			if (!writeDirectory.exists()) {
				writeDirectory.mkdirs();
			}

			OutputStream os = new FileOutputStream(filePath);
			IOUtils.copy(uploadStream, os);

			uploadStream.close();
			os.close();
		} catch (IOException e) {
			throw e;
		}
	}


 


	/**
	 * Method to delete file
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String path) {
		if (null != path) {
			File file = new File(path);
			if (file.exists() && file.isFile()) {
				return file.delete();
			}
		}
		return false;
	}



	/**
	 * Method get file name from url
	 * 
	 * @param fileUrl
	 * @return
	 */
	public static String getFileNameFromUrl(String fileUrl) {
		String fileName = null;
		if (StringUtils.isNotBlank(fileUrl)) {
			if (fileUrl.contains("\\")) {
				fileUrl = fileUrl.replace("\\", "/");
			}
			fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		}
		return fileName;
	}

	/**
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static String getFileName(String filePath, String fileName) {
		if (filePath == null) {
			return fileName;
		}

		return filePath + File.separator + fileName;
	}

 


}
