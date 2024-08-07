package com.example.filetest.controller;

import com.example.filetest.dto.FileDto;
import com.example.filetest.entity.FileEntity;
import com.example.filetest.dto.FileMapper;
import com.example.filetest.service.FileService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Контроллер для управления файлами.
 * Предоставляет эндпоинты для создания, получения, удаления и получения файлов с пагинацией и сортировкой.
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    /**
     * Конструктор контроллера.
     *
     * @param fileService Сервис для работы с файлами
     */
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Создает новый файл.
     *
     * @param fileDto DTO файла для создания
     * @return ID созданного файла
     */
    @PostMapping
    public ResponseEntity<Long> createFile(@RequestBody @Valid FileDto fileDto) {
        FileEntity fileEntity = FileMapper.toEntity(fileDto);
        Long id = fileService.saveFile(fileEntity);
        return ResponseEntity.ok(id);
    }

    /**
     * Получает файл по его ID.
     *
     * @param id ID файла
     * @return DTO файла, если найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<FileDto> getFile(@PathVariable Long id) {
        Optional<FileEntity> fileEntity = fileService.getFile(id);
        return fileEntity.map(entity -> ResponseEntity.ok(FileMapper.toDto(entity)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Получает все файлы.
     *
     * @return список DTO всех файлов
     */
    @GetMapping("/all")
    public ResponseEntity<List<FileDto>> getAllFiles() {
        List<FileEntity> fileEntities = fileService.getAllFiles();
        List<FileDto> fileDtos = fileEntities.stream()
                .map(FileMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(fileDtos);
    }

    /**
     * Получает файлы с пагинацией и сортировкой.
     *
     * @param page номер страницы (начинается с 0)
     * @param size размер страницы
     * @param sortDirection направление сортировки (например, "ASC" или "DESC")
     * @param sortBy поле для сортировки
     * @return страница DTO файлов
     */
    @GetMapping
    public ResponseEntity<Page<FileDto>> getFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "creationDate") String sortBy) {
        Page<FileEntity> filePage = fileService.getFiles(page, size, sortDirection, sortBy);
        Page<FileDto> fileDtoPage = filePage.map(FileMapper::toDto);
        return ResponseEntity.ok(fileDtoPage);
    }

    /**
     * Удаляет файл по его ID.
     *
     * @param id ID файла для удаления
     * @return ResponseEntity с кодом статуса, указывающим на результат операции
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        boolean isDeleted = fileService.deleteFile(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
