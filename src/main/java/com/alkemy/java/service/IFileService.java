package com.alkemy.java.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface IFileService {

    String uploadFile(MultipartFile file) throws Exception;

    List<String> getFullUrFilesFromAWSS3();

    InputStream downloadFileFromAWSS3(String file)  throws Exception;

    String deleteFileFromAWSS3(String file) throws Exception ;

}
