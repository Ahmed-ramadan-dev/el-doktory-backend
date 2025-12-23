package com.spring.boot.Service.ServiceHelp;

import com.spring.boot.Exception.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileHelperService {

    private final CloudinaryService cloudinaryService;

    public FileHelperService(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }


    public List<String> uploadFiles(MultipartFile[] files) {
        if (files == null || files.length == 0 || Arrays.stream(files).allMatch(MultipartFile::isEmpty)) {
            throw new FileUploadException("لم يتم رفع أي ملفات");
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                urls.add(cloudinaryService.upload(file));
            }
        }
        return urls;
    }


    public List<String> deleteFiles(List<String> currentFiles, List<String> filesToDelete) {
        if (filesToDelete != null && !filesToDelete.isEmpty()) {
            for (String url : filesToDelete) {
                cloudinaryService.deleteByUrl(url);
                currentFiles.remove(url);
            }
        }
        return currentFiles;
    }
}
