package com.pepcus.appstudent.util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

import lombok.extern.log4j.Log4j;

/**
 * Helper class to upload/remove/download media on media-server 
 * 
 * @author Sandeep.Vishwakarma
 * @since 05-02-2020
 *
 */
@Log4j
public class MediaHandlerHelper {

	/**
	 * Method to upload file on media server
	 * 
	 * @param mediaSaver
	 * @param file
	 * @param bucketName
	 * @param mediaKey
	 * @param mediaName
	 * @return
	 */
	public static String uploadFile(MediaHandler mediaSaver, File file, String bucketName, String mediaKey,
			String mediaName) {
		// Upload file on media server
		String fileUrl = null;
		try {
			Path sourcePath = Paths.get(file.getPath());
			fileUrl = mediaSaver.save(bucketName, mediaKey, mediaName, Files.probeContentType(sourcePath),
					new FileInputStream(file));
		} catch (AmazonServiceException ase) {
			log.error("Caught an AmazonServiceException : Request rejected with an error response for some reason.",
					ase);
			log.error("Error Message: " + ase.getMessage());
		} catch (AmazonClientException ace) {
			log.error("Caught an AmazonClientException, Problem to communicate with S3.", ace);
			// getMessage(), override method, will return serviceName, Status
			// Code, Error Code, Request Id
			log.error("Error Message: " + ace.getMessage());
		} catch (Exception e) {
			log.error("Error in uploading file on bucket : " + mediaName);
			log.error(e);
		}
		return fileUrl;
	}

	/**
	 * Method to load file from media server
	 * 
	 * @param mediaSaver
	 * @param bucketName
	 * @param mediaKey
	 * @param mediaName
	 * @return
	 */
	public static FileStream loadFile(MediaHandler mediaSaver, String bucketName, String mediaKey, String mediaName) {
		FileStream fileStream = null;
		try {
			fileStream = mediaSaver.load(bucketName, mediaKey, mediaName);
		} catch (AmazonServiceException ase) {
			log.error("Caught an AmazonServiceException : Request rejected with an error response for some reason.",
					ase);
			log.error("Error Message: " + ase.getMessage());
		} catch (AmazonClientException ace) {
			log.error("Caught an AmazonClientException, Problem to communicate with S3.", ace);
			// getMessage(), override method, will return serviceName, Status
			// Code, Error Code, Request Id
			log.error("Error Message: " + ace.getMessage());
		} catch (Exception e) {
			log.error("Error in getting file from bucket : " + mediaName);
			log.error(e);
		}

		return fileStream;
	}

	/**
	 * Method to delete file from media server
	 * 
	 * @param mediaSaver
	 * @param bucketName
	 * @param mediaKey
	 * @param mediaName
	 * @return
	 */
	public static boolean deleteFile(MediaHandler mediaSaver, String bucketName, String mediaKey, String mediaName) {

		boolean isDeleteSuccess = false;
		try {
			mediaSaver.remove(bucketName, mediaKey, mediaName);
			isDeleteSuccess = true;
		} catch (AmazonServiceException ase) {
			log.error("Caught an AmazonServiceException : Request rejected with an error response for some reason.",
					ase);
			log.error("Error Message: " + ase.getMessage());
		} catch (AmazonClientException ace) {
			log.error("Caught an AmazonClientException, Problem to communicate with S3.", ace);
			// getMessage(), override method, will return serviceName, Status
			// Code, Error Code, Request Id
			log.error("Error Message: " + ace.getMessage());
		} catch (Exception e) {
			log.error("Error in deleting file  : " + mediaName);
			log.error(e);
		}
		return isDeleteSuccess;
	}


}
