package com.ciro.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        String resourceType = "auto";
        String originalFilename = file.getOriginalFilename();

        String publicId = UUID.randomUUID().toString();

        if (originalFilename != null && originalFilename.contains(".")) {
            String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            baseName = baseName.replaceAll("[^a-zA-Z0-9-_]", "_");

            String shortUuid = UUID.randomUUID().toString().substring(0, 8);

            publicId = baseName + "_" + shortUuid;
            if (extension.equalsIgnoreCase(".pdf")) {
                resourceType = "raw";
                publicId += extension;
            }
        }

        Map params = ObjectUtils.asMap(
                "resource_type", resourceType,
                "public_id", publicId
        );

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

        return uploadResult.get("secure_url").toString();
    }
}