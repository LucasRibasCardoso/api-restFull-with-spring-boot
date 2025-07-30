package com.LucasRibasCardoso.api_rest_with_spring_boot.dto.file;

public record UploadFileResponseDto(
    String fileName, String fileDownloadUri, String fileType, long size) {}
