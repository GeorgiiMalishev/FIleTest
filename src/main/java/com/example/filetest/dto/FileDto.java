package com.example.filetest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileDto {
    @Pattern(regexp = "^[A-Za-z0-9+/=]+$", message = "Invalid base64 file content")
    @NotEmpty
    private String fileData;

    @NotEmpty(message = "Title shouldn't be empty")
    private String title;

    private String description;
    private LocalDateTime creationDate;
}
