package com.alkemy.java.service.impl;

import com.alkemy.java.service.IFileService;
import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("error.match.file.url")
    private String errorMatchFileUrl;

    @Value("success.deleted")
    private String errorSuccessDeleted;

    @Autowired
    MessageSource messageSource;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        File mainFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        String fileName = "";
        String fileUrl ="";
        try {
            FileOutputStream stream = new FileOutputStream(mainFile);
            stream.write(file.getBytes());
            fileName = System.currentTimeMillis()+"-"+mainFile.getName();
            fileUrl =  endpointUrl + "/" + bucketName +"/"+ fileName;
            PutObjectRequest request = new PutObjectRequest(bucketName,fileName,mainFile);
            amazonS3.putObject(request);
            
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return fileUrl;
    }
    
    

    @Override
    public List<String> getFullUrFilesFromAWSS3() {

        ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName);

        List<S3ObjectSummary> objects = result.getObjectSummaries();

        List<String> list = objects.stream().map(item -> {
            return endpointUrl.concat("/").concat(bucketName).concat("/").concat(item.getKey());
        }).collect(Collectors.toList());

        return list;
    }

    @Override
    public InputStream downloadFileFromAWSS3(String file) throws Exception {

        try {
            if (amazonS3.doesObjectExist(bucketName, file)) {
                S3Object object = amazonS3.getObject(bucketName, file);
                return object.getObjectContent();
            }
            throw new FileNotFoundException(messageSource.getMessage(errorMatchFileUrl, null, Locale.getDefault()));

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String deleteFileFromAWSS3(String file) throws Exception {

        try {
            if(amazonS3.doesObjectExist(bucketName, file)){
                amazonS3.deleteObject(bucketName, file);
                return messageSource.getMessage(errorSuccessDeleted, null, Locale.getDefault());
            }
            throw new FileNotFoundException(messageSource.getMessage(errorMatchFileUrl, null, Locale.getDefault()));

        } catch (SdkClientException e) {
            throw new SdkClientException(e.getMessage());
        } catch (AmazonClientException e) {
            throw new AmazonClientException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
