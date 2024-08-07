package com.example.filetest.service;

import com.example.filetest.entity.FileEntity;
import com.example.filetest.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    private FileEntity fileEntity;

    @BeforeEach
    void setUp() {
        fileEntity = new FileEntity();
        fileEntity.setId(1L);
        fileEntity.setCreationDate(LocalDateTime.now());  // Убедитесь, что установка даты создаёт корректный объект
    }

    @Test
    void testSaveFile() {
        when(fileRepository.save(any(FileEntity.class))).thenReturn(fileEntity);

        Long savedFileId = fileService.saveFile(fileEntity);

        assertEquals(fileEntity.getId(), savedFileId);
        verify(fileRepository, times(1)).save(fileEntity);
    }

    @Test
    void testGetFile() {
        when(fileRepository.findById(1L)).thenReturn(Optional.of(fileEntity));

        Optional<FileEntity> foundFile = fileService.getFile(1L);

        assertTrue(foundFile.isPresent());
        assertEquals(fileEntity, foundFile.get());
        verify(fileRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllFiles() {
        List<FileEntity> fileList = List.of(fileEntity);
        when(fileRepository.findAll()).thenReturn(fileList);

        List<FileEntity> foundFiles = fileService.getAllFiles();

        assertEquals(fileList.size(), foundFiles.size());
        assertEquals(fileList.get(0), foundFiles.get(0));
        verify(fileRepository, times(1)).findAll();
    }

    @Test
    void testGetFilesWithPaginationAndSorting() {
        // Создаем несколько объектов для тестирования
        FileEntity fileEntity1 = new FileEntity();
        fileEntity1.setId(1L);
        fileEntity1.setCreationDate(LocalDateTime.now().minusDays(1));

        FileEntity fileEntity2 = new FileEntity();
        fileEntity2.setId(2L);
        fileEntity2.setCreationDate(LocalDateTime.now().plusDays(1));

        List<FileEntity> fileList = List.of(fileEntity1, fileEntity2);
        Page<FileEntity> page = new PageImpl<>(fileList, PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "creationDate")), fileList.size());

        when(fileRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Задаем параметры пагинации и сортировки
        int pageNumber = 0;
        int pageSize = 2;
        String sortDirection = "ASC";
        String sortBy = "creationDate";

        Page<FileEntity> resultPage = fileService.getFiles(pageNumber, pageSize, sortDirection, sortBy);

        assertEquals(2, resultPage.getTotalElements());
        assertEquals(fileEntity1.getId(), resultPage.getContent().get(0).getId());
        assertEquals(fileEntity2.getId(), resultPage.getContent().get(1).getId());
        verify(fileRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testDeleteFile_Success() {
        when(fileRepository.existsById(fileEntity.getId())).thenReturn(true);

        boolean result = fileService.deleteFile(fileEntity.getId());

        assertTrue(result);
        verify(fileRepository, times(1)).deleteById(fileEntity.getId());
    }

    @Test
    void testDeleteFile_FileNotFound() {
        when(fileRepository.existsById(fileEntity.getId())).thenReturn(false);

        boolean result = fileService.deleteFile(fileEntity.getId());

        assertFalse(result);
        verify(fileRepository, never()).deleteById(fileEntity.getId());
    }
}
