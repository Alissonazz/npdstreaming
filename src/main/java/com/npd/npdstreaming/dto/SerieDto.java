package com.npd.npdstreaming.dto;

import com.npd.npdstreaming.model.Category;

public record SerieDto(Long id,
                       String title,
                       Integer season,
                       Double omdbRating,
                       Category genre,
                       String actors,
                       String poster,
                       String sinopse) {
}
