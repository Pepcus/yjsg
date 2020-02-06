package com.pepcus.appstudent.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.pepcus.appstudent.exception.APIErrorCodes;
import com.pepcus.appstudent.exception.ApplicationException;

import lombok.extern.log4j.Log4j;

/**
 * @author Surabhi Bhawsar
 *
 */
@Component
@Log4j
public class S3FileUtils {


  /**
   * Method to download document from s3 media server
   * 
   * @param bucketName
   * @param mediaKey
   * @param mediaName
   * @param mediaHandler
   * @return
   * @throws IOException
   */
  public ResponseEntity<InputStreamResource> downloadDocumentFromServer(String bucketName, String mediaKey,
      String mediaName, MediaHandler mediaHandler) throws Exception {

    // Load file from media server for download
    FileStream fileStream = MediaHandlerHelper.loadFile(mediaHandler, bucketName, mediaKey, mediaName);
    if (fileStream == null) {
      throw new Exception("File not found with name : "+mediaName);
    }
    return getResponseEntity(mediaName, fileStream);
  }

  /**
   * Get response entity from fileStream
   * 
   * @param mediaName
   * @param fileStream
   * @return
   * @throws IOException
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private ResponseEntity<InputStreamResource> getResponseEntity(String mediaName, FileStream fileStream)
      throws IOException {

    ResponseEntity<InputStreamResource> respEntity = null;
    final InputStream fStream = fileStream.getInputStream();
    if (null != fStream) {
      String type = URLConnection.guessContentTypeFromName(mediaName);

      byte[] out = org.apache.commons.io.IOUtils.toByteArray(fStream);

      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.add("content-disposition", "attachment; filename=" + mediaName);
      responseHeaders.add("Content-Type", type);

      respEntity = new ResponseEntity(out, responseHeaders, HttpStatus.OK);
    } else {
      respEntity = new ResponseEntity("File Not Found", HttpStatus.NOT_FOUND);
    }
    return respEntity;
  }

  /**
   * 
   * @param bucketName
   * @param mediaKey
   * @param filePath
   * @param fileName
   * @param mediaHandler
   * @return
   */
  public String uploadDocument(String bucketName, String mediaKey, String filePath, String fileName,
      MediaHandler mediaHandler) {
    File documentFile = new File(filePath + fileName);

    // Upload file on media server
    String mediaName = fileName;
    String fileUrl = null;
    try {
      fileUrl = MediaHandlerHelper.uploadFile(mediaHandler, documentFile, bucketName, mediaKey, mediaName);
    } catch (Exception e) {
      log.error("Error in upload file on media server. " + fileName);
    }
    log.debug("Url of uploaded file on media server is : " + fileUrl);
    return fileUrl;
  }

  /**
   * Method to delete document from s3 media server
   * 
   * @param mediaKey
   * @param filePath
   * @param fileName
   * @return
   */
  public boolean deleteDocument(String bucketName, String mediaKey, String mediaName, MediaHandler mediaHandler) {

    boolean isDeleteSuccess = MediaHandlerHelper.deleteFile(mediaHandler, bucketName, mediaKey, mediaName);
    if (!isDeleteSuccess) {
      log.error("Error in deleting file on from media server. " + mediaName);
    }
    return isDeleteSuccess;
  }

  /**
   * Method to upload file on s3 media server
   * 
   * @param multipartFile
   * @param mediaKey
   * @return
   * @throws IOException
   */
  public String uploadFile(String bucketName, String mediaKey, String uploadUrl, MultipartFile multipartFile,
      MediaHandler mediaHandler) throws IOException {
    // Upload file to local temp storage
    String fileName = multipartFile.getOriginalFilename();
    FileUtil.uploadFile(multipartFile.getInputStream(), uploadUrl, fileName);

    // Upload file on S3
    String fileUrl = uploadDocument(bucketName, mediaKey, uploadUrl, fileName, mediaHandler);

    // cleanup temp storage
    cleanup(uploadUrl, fileName);

    if (fileUrl == null) {
      throw new ApplicationException("Error in upload file on media server. " + fileName);
    }
    return fileUrl;
  }

 

  /**
   * Cleanup resources
   * 
   * @param filePath
   * @param fileName
   */
  public void cleanup(String filePath, String fileName) {
    // Delete file from temp location after uploaded on media server
    try {
      FileUtil.deleteFile(filePath + fileName);
    } catch (Exception e) {
      // Ignore, we don't want to break application due to exception in cleanup
    }
  }
}
