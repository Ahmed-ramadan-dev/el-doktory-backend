package com.spring.boot.Service.ServiceHelp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.spring.boot.Exception.CloudinaryOperationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String upload(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());

            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new CloudinaryOperationException(
                    "Failed to upload file: " + file.getOriginalFilename(), e);
        }
    }

    public void deleteByUrl(String url) {
        if (url == null || !url.contains(".")) {
            return;
        }
        try {
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            String publicId = fileName.substring(0, fileName.lastIndexOf("."));
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new CloudinaryOperationException(
                    "Failed to delete file from Cloudinary", e);
        }
    }
}
