package com.capstone.BnagFer.domain.board.dto.response;

import com.capstone.BnagFer.domain.board.entity.BoardImage;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record BoardImageDto(
        Long imageId,
        String boardImageUrl
) {

    public static List<BoardImageDto> from(List<BoardImage> images) {
        return images.stream()
                .map(image -> new BoardImageDto(image.getId(), image.getImageUrl()))
                .collect(Collectors.toList());
    }
}
