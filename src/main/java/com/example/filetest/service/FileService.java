package com.example.filetest.service;

import com.example.filetest.entity.FileEntity;
import com.example.filetest.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления файлами.
 * Предоставляет методы для сохранения, получения и получения файлов с пагинацией и сортировкой.
 */
@Service
@Slf4j
public class FileService {

    private final FileRepository fileRepository;

    /**
     * Конструктор сервиса.
     *
     * @param fileRepository Репозиторий для работы с файлами
     */
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * Сохраняет файл.
     *
     * @param fileEntity сущность файла для сохранения
     * @return ID сохраненного файла
     */
    public Long saveFile(FileEntity fileEntity) {
        log.info("Сохранение файла");
        fileEntity.setCreationDate(LocalDateTime.now());
        var savedFile = fileRepository.save(fileEntity);
        log.info("Файл сохранен с ID: {}", savedFile.getId());
        return savedFile.getId();
    }

    /**
     * Получает файл по его ID.
     *
     * @param id ID файла
     * @return Optional с сущностью файла, если найден
     */
    public Optional<FileEntity> getFile(Long id) {
        log.info("Получение файла с ID: {}", id);
        Optional<FileEntity> fileEntity = fileRepository.findById(id);
        if (fileEntity.isPresent()) {
            log.info("Файл с ID {} найден", id);
        } else {
            log.warn("Файл с ID {} не найден", id);
        }
        return fileEntity;
    }

    /**
     * Получает все файлы.
     *
     * @return список всех файлов
     */
    public List<FileEntity> getAllFiles() {
        log.info("Получение всех файлов");
        List<FileEntity> files = fileRepository.findAll();
        log.info("Найдено файлов: {}", files.size());
        return files;
    }

    /**
     * Получает файлы с пагинацией и сортировкой.
     *
     * @param page номер страницы (начинается с 0)
     * @param size размер страницы
     * @param sortDirection направление сортировки (например, "ASC" или "DESC")
     * @param sortBy поле для сортировки
     * @return страница с файлами
     */
    public Page<FileEntity> getFiles(int page, int size, String sortDirection, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        log.info("Получение файлов с пагинацией и сортировкой");
        Page<FileEntity> files = fileRepository.findAll(pageable);
        log.info("Найдено файлов: {}", files.getTotalElements());
        return files;
    }

    /**
     * Удаляет файл по его ID.
     *
     * @param id ID файла для удаления
     * @return true, если файл был успешно удален, иначе false
     */
    public boolean deleteFile(Long id) {
        log.info("Удаление файла с ID: {}", id);
        if (fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
            log.info("Файл с ID {} удален", id);
            return true;
        } else {
            log.warn("Файл с ID {} не найден", id);
            return false;
        }
    }
}
