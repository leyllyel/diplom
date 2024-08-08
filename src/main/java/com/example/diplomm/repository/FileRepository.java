package com.example.diplomm.repository;

import com.example.diplomm.users.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileInfo, Long> {
    List<FileInfo> findByUserId(Long userId);
}

