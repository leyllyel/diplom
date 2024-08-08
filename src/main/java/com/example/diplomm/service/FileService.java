package com.example.diplomm.service;
import com.example.diplomm.repository.FileRepository;
import com.example.diplomm.repository.UserRepository;
import com.example.diplomm.users.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Autowired
    public FileService(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FileInfo uploadFile(MultipartFile file, Long userId) throws IOException {
        String filename = file.getOriginalFilename();
        String path = saveFileToLocal(file);

        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilename(filename);
        fileInfo.setPath(path);
        fileInfo.setUserId(userId);

        return fileRepository.save(fileInfo);
    }

    @Transactional(readOnly = true)
    public List<FileInfo> getFilesByUser(Long userId) {
        return fileRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteFile(Long fileId) {
        Optional<FileInfo> fileOptional = fileRepository.findById(fileId);
        if (fileOptional.isPresent()) {
            FileInfo file = fileOptional.get();
            fileRepository.delete(file);
            deleteFileFromLocal(file.getPath());
        }
    }

    private String saveFileToLocal(MultipartFile file) throws IOException {
        String path = "files/" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), Paths.get(path));
        return path;
    }

    private void deleteFileFromLocal(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
