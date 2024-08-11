package com.example.diplomm.users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileInfoTest {

    @Test
    public void testConstructor() {
        FileInfo fileInfo = new FileInfo();
        assertNotNull(fileInfo);
    }

    @Test
    public void testSettersAndGetters() {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(1L);
        fileInfo.setFilename("test.txt");
        fileInfo.setPath("files/test.txt");
        fileInfo.setUserId(1L);

        assertEquals(1L, fileInfo.getId());
        assertEquals("test.txt", fileInfo.getFilename());
        assertEquals("files/test.txt", fileInfo.getPath());
        assertEquals(1L, fileInfo.getUserId());
    }
}