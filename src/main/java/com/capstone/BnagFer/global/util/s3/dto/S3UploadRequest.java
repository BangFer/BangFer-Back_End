package com.capstone.BnagFer.global.util.s3.dto;

import lombok.Builder;

@Builder
public record S3UploadRequest(
        Long userId,
        String dirName
) {
}