package com.example.diplomm.service;

import com.example.diplomm.repository.FileRepository;
import com.example.diplomm.repository.UserRepository;
import com.example.diplomm.users.FileInfo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;


@Service
public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Autowired
    public FileService(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FileInfo uploadFile(MultipartFile file, Long userId) throws IOException {
        logger.info("Загрузка файла: ", file.getOriginalFilename());
        String filename = file.getOriginalFilename();

        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilename(filename);
        fileInfo.setUserId(userId);
        fileInfo = fileRepository.save(fileInfo);

        String path = "files/" + file.getOriginalFilename();
        fileInfo.setPath(path);
        fileRepository.save(fileInfo);

        Files.copy(file.getInputStream(), Paths.get(path));

        logger.info("Файл успешно загружен", file.getOriginalFilename());
        return fileInfo;
    }

    @Transactional(readOnly = true)
    public List<FileInfo> getFilesByUser(Long userId) {
        logger.info("Получение файлов: ", userId);
        return fileRepository.findByUserId(userId);
    }

    @Transactional
    public ResponseEntity<Object> deleteFile(Long fileId) {
        logger.info("Удаление файла с ID: ", fileId);
        Optional<FileInfo> fileOptional = fileRepository.findById(fileId);
        if (fileOptional.isPresent()) {
            FileInfo file = fileOptional.get();
            fileRepository.delete(file);
            try {
                deleteFileFromLocal(file.getPath());
                logger.info("Файл  успешно удален", file.getPath());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (IOException e) {
                logger.error("Ошибка при удалении файла: ", file.getPath(), e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.warn("Файл с ID  не найден", fileId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private void deleteFileFromLocal(String path) throws IOException {
        logger.info("Удаление файла: ", path);
        Files.deleteIfExists(Paths.get(path));
    }
}
