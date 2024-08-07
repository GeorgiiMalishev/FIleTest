package com.example.filetest.dto;

import com.example.filetest.entity.FileEntity;

import java.util.Base64;

public class FileMapper {

    public static FileDto toDto(FileEntity fileEntity) {
        FileDto fileDto = new FileDto();
        fileDto.setFileData(Base64.getEncoder().encodeToString(fileEntity.getFileData()));
        fileDto.setTitle(fileEntity.getTitle());
        fileDto.setCreationDate(fileEntity.getCreationDate());
        fileDto.setDescription(fileEntity.getDescription());
        return fileDto;
    }

    public static FileEntity toEntity(FileDto fileDto) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileData(Base64.getDecoder().decode(fileDto.getFileData()));
        fileEntity.setTitle(fileDto.getTitle());
        fileEntity.setCreationDate(fileDto.getCreationDate());
        fileEntity.setDescription(fileDto.getDescription());
        return fileEntity;
    }
}
