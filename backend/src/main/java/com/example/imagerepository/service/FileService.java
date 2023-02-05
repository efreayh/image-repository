package com.example.imagerepository.service;

import com.example.imagerepository.model.FileEntity;
import com.example.imagerepository.model.LabelEntity;
import com.example.imagerepository.repository.FileRepository;
import com.example.imagerepository.repository.LabelRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final LabelRepository labelRepository;
    @Value("${imaggaApiKey}")
    private String imaggaAPIKey;
    @Value("${imaggaApiSecret}")
    private String imaggaAPISecret;
    private String imaggaAPIEndpoint;

    @Autowired
    private LabellingService labellingService;

    @Autowired
    public FileService(FileRepository fileRepository, LabelRepository labelRepository) {
        this.fileRepository = fileRepository;
        this.labelRepository = labelRepository;
        imaggaAPIEndpoint = "https://api.imagga.com/v2";
    }

    public void save(MultipartFile file) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(StringUtils.cleanPath(file.getOriginalFilename()));
        fileEntity.setContentType(file.getContentType());
        fileEntity.setData(file.getBytes());
        fileEntity.setSize(file.getSize());

        fileRepository.save(fileEntity);

        List<String> fileLabels = getFileLabels(file);

        for(String label: fileLabels) {
            saveFileLabel(fileEntity, label);
        }
    }

    private List<String> getFileLabels(MultipartFile file) throws IOException {

        List<String> labels = new ArrayList<>();

        JSONObject uploadResponse = labellingService.uploadFile(file);

        String uploadStatus = uploadResponse.getJSONObject("status").getString("type");

        if(uploadStatus.equalsIgnoreCase("success")) {
            String uploadId = uploadResponse.getJSONObject("result").getString("upload_id");

            JSONObject labelsResponse = labellingService.getLabels(uploadId);

            String labelsStatus = labelsResponse.getJSONObject("status").getString("type");

            if(labelsStatus.equalsIgnoreCase("success")) {

                JSONArray jsonLabels = labelsResponse.getJSONObject("result").getJSONArray("tags");

                for(int i = 0; i < jsonLabels.length(); i++) {
                    labels.add(jsonLabels.getJSONObject(i).getJSONObject("tag").getString("en"));
                }
            }
            else {
                throw new IOException();
            }
        }
        else {
            throw new IOException();
        }

        return labels;
    }

    private void saveFileLabel(FileEntity fileEntity, String label) {
        LabelEntity labelEntity = new LabelEntity();
        labelEntity.setLabel(label);
        labelEntity.setFile(fileEntity);

        labelRepository.save(labelEntity);
    }

    public Optional<FileEntity> getFile(String id) {
        return fileRepository.findById(id);
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    @Transactional
    public List<FileEntity> getAllFilesByKeyword(String keyword) {
        return fileRepository.findDistinctByNameLikeOrLabels_LabelLike(
                "%"+keyword.toLowerCase()+"%",
                "%"+keyword.toLowerCase()+"%"
        );
    }
}