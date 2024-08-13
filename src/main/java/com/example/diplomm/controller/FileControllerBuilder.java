package com.example.diplomm.controller;

import com.example.diplomm.service.FileService;

public class FileControllerBuilder {
    private FileService fileService;

    public FileControllerBuilder setFileService(FileService fileService) {
        this.fileService = fileService;
        return this;
    }

    public FileController createFileController() {
        return new FileController(fileService);
    }
}