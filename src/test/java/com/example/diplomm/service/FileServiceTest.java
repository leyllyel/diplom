package com.example.diplomm.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.diplomm.repository.FileRepository;
import com.example.diplomm.repository.UserRepository;
import com.example.diplomm.users.FileInfo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test") // Если используете разные профили
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    @MockBean
    private FileRepository fileRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testUploadFile() throws IOException {
        // Создаем mock-файл
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", "Test content".getBytes());

        // Создаем mock-объект FileInfo
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(1L);
        fileInfo.setFilename("test.txt");
        fileInfo.setUserId(1L);
        fileInfo.setPath("files/test.txt");

        // Устанавливаем mock-поведение для fileRepository
        when(fileRepository.save(any(FileInfo.class))).thenReturn(fileInfo);

        // Вызываем метод uploadFile
        FileInfo result = fileService.uploadFile(file, 1L);

        // Проверяем результат
        assertEquals(fileInfo.getId(), result.getId());
        assertEquals(fileInfo.getFilename(), result.getFilename());
        assertEquals(fileInfo.getUserId(), result.getUserId());
        assertEquals(fileInfo.getPath(), result.getPath());

        // Проверяем, что файл был сохранен на диске
        assertTrue(Files.exists(Paths.get("files/test.txt")));
    }

    @Test
    public void testGetFilesByUser() {
        // Создаем mock-данные для файлов пользователя
        FileInfo file1 = new FileInfo();
        file1.setId(1L);
        FileInfo file2 = new FileInfo();
        file2.setId(2L);
        List<FileInfo> files = new ArrayList<>();
        files.add(file1);
        files.add(file2);

        // Устанавливаем mock-поведение для fileRepository
        when(fileRepository.findByUserId(1L)).thenReturn(files);

        // Вызываем метод getFilesByUser
        List<FileInfo> result = fileService.getFilesByUser(1L);

        // Проверяем результат
        assertEquals(2, result.size());
        assertEquals(file1, result.get(0));
        assertEquals(file2, result.get(1));
    }

    @Test
    public void testDeleteFile() throws IOException {
        // Создаем mock-объект FileInfo
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(1L);
        fileInfo.setPath("files/test.txt");

        // Устанавливаем mock-поведение для fileRepository
        when(fileRepository.findById(1L)).thenReturn(Optional.of(fileInfo));
        doNothing().when(fileRepository).delete(fileInfo);

        // Вызываем метод deleteFile
        fileService.deleteFile(1L);

        // Проверяем, что файл был удален из базы данных
        verify(fileRepository, times(1)).delete(fileInfo);

        // Проверяем, что файл был удален с диска
        assertFalse(Files.exists(Paths.get("files/test.txt")));
    }
}