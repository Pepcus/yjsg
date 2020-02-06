package com.pepcus.appstudent.util;

import java.io.InputStream;
import java.util.Date;

import lombok.Data;

@Data
public class FileStream {

  private InputStream inputStream;
  private Date lastModified;
  private long contentLength;
  private String contentType;

}
