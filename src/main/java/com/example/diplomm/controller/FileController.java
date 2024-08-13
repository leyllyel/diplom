package com.example.diplomm.controller;

import com.example.diplomm.service.FileService;
import com.example.diplomm.users.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<FileInfo> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) throws IOException {
        FileInfo uploadedFile = fileService.uploadFile(file, userId);
        return new ResponseEntity<>(uploadedFile, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FileInfo>> getFilesByUser(@PathVariable Long userId) {
        List<FileInfo> files = fileService.getFilesByUser(userId);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        fileService.deleteFile(fileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}