package com.example.diplomm.controller;

import com.example.diplomm.service.FileService;
import com.example.diplomm.users.FileInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FileController.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                MediaType.TEXT_PLAIN_VALUE, "Test content".getBytes());
        FileInfo uploadedFile = new FileInfo();
        uploadedFile.setId(1L);
        when(fileService.uploadFile(file, 1L)).thenReturn(uploadedFile);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files")
                        .file(file)
                        .param("userId", "1"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void testGetFilesByUser() throws Exception {
        // Создаем тестовые данные
        FileInfo file1 = new FileInfo();
        file1.setId(1L);
        FileInfo file2 = new FileInfo();
        file2.setId(2L);
        List<FileInfo> files = new ArrayList<>();
        files.add(file1);
        files.add(file2);

        // Задаем mock-поведение для FileService
        when(fileService.getFilesByUser(1L)).thenReturn(files);

        // Отправляем запрос на получение файлов для пользователя
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
    }

    @Test
    public void testDeleteFile() throws Exception {
        // Задаем mock-поведение для FileService
        doNothing().when(fileService).deleteFile(1L);

        // Отправляем запрос на удаление файла
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/files/1"))
                .andExpect(status().isNoContent());
    }
}
