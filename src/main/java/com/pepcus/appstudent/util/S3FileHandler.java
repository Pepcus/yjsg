package com.pepcus.appstudent.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

/**
 * Class to upload/remove/download media on S3 media-server 
 * 
 * @author Sandeep.Vishwakarma
 * @since 05-02-2020
 *
 */
@Component
public class S3FileHandler implements MediaHandler {

	private AmazonS3 s3Client = null;

	private AccessControlList accessControl = null;

	private String bucketName = null;

	S3FileHandler() {
		if (s3Client == null) {
			init();
		}
	}

	@Override
	public void init() {

		/*
		 * The ProfileCredentialsProvider will return your [default] credential
		 * profile by reading from the credentials file located at
		 * (~/.aws/credentials).
		 */
		try {
			s3Client = new AmazonS3Client(new DefaultAWSCredentialsProviderChain());
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.", e);
		}

		accessControl = new AccessControlList();
		accessControl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
	}

	@Override
	public String save(String mediaKey, String mediaName, String contentType, InputStream content) throws IOException {
		ObjectMetadata om = new ObjectMetadata();
		om.setContentType(contentType);
		PutObjectRequest putRequest = new PutObjectRequest(bucketName, filename(mediaKey, mediaName), content, om)
				.withAccessControlList(accessControl);
		s3Client.putObject(putRequest);
		String url = getFileUrl(mediaKey, mediaName, bucketName);
		if (StringUtils.isNotBlank(url)) {
			url = URLDecoder.decode(url, "UTF-8");
		}
		return url;
	}

	@Override
	public String getFileUrl(String mediaKey, String mediaName, String bucketName) {
		if (StringUtils.isNotBlank(bucketName)) {
			this.bucketName = bucketName;
		}
		return s3Client.getUrl(bucketName, filename(mediaKey, mediaName)).toString();
	}

	@Override
	public List<String> uploadFiles(String bucketName, String mediaKey, List<File> files) throws IOException {

		List<String> fileDescriptions = new ArrayList<>();
		for (File file : files) {
			String mediaName = file.getName();
			Path sourcePath = Paths.get(file.getPath());
			String fileUrl = save(bucketName, mediaKey, mediaName, Files.probeContentType(sourcePath),
					new FileInputStream(file));
			fileDescriptions.add(fileUrl);
		}
		return fileDescriptions;
	}

	@Override
	public String save(String bucketName, String mediaKey, String mediaName, String contentType, InputStream content)
			throws IOException {
		if (StringUtils.isNotBlank(bucketName)) {
			this.bucketName = bucketName;
		}
		return save(mediaKey, mediaName, contentType, content);
	}

	@Override
	public void remove(String mediaKey, String mediaName) throws IOException {
		remove(bucketName, mediaKey, mediaName);
	}

	@Override
	public void remove(String bucketName, String mediaKey, String mediaName) throws IOException {
		if (StringUtils.isBlank(mediaName)) {
			s3Client.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(mediaKey).withQuiet(false));
		} else {
			s3Client.deleteObject(new DeleteObjectRequest(bucketName, filename(mediaKey, mediaName)));
		}
	}

	@Override
	public InputStream load(String mediaKey, String mediaName) throws IOException {
		S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, filename(mediaKey, mediaName)));
		return object.getObjectContent();
	}

	@Override
	public FileStream load(String bucketName, String mediaKey, String mediaName) throws IOException {
		S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, filename(path(mediaKey), mediaName)));
		FileStream stream = new FileStream();
		stream.setInputStream(object.getObjectContent());
		stream.setLastModified(object.getObjectMetadata().getLastModified());
		stream.setContentLength(object.getObjectMetadata().getContentLength());
		stream.setContentType(object.getObjectMetadata().getContentType());
		return stream;
	}

	@Override
	public String loadText(String mediaKey, String mediaName) throws IOException {
		return IOUtils.toString(load(mediaKey, mediaName), "UTF-8");
	}

}
