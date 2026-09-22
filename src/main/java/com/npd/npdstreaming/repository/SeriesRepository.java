package com.npd.npdstreaming.repository;

import com.npd.npdstreaming.dto.EpisodeDto;
import com.npd.npdstreaming.model.Category;
import com.npd.npdstreaming.model.Episode;
import com.npd.npdstreaming.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Serie, Long> {

    List<Serie> findByGenre(Category category);

    List<Serie> findTop5ByOrderByImdbRatingDesc();

    @Query("""
            SELECT s FROM Serie s
            JOIN s.episodes e
            GROUP BY s 
            ORDER BY MAX(e.releaseDateEpisode) DESC
            LIMIT 5
            """)
    List<Serie> findTop5Series();

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s.id = :id and e.season = :season")
    List<EpisodeDto> findEpisodesBySeason(Long id, Integer season);

    @Query("""
            SELECT e FROM Episode e
            JOIN e.serie s
            WHERE s.id = :id
            ORDER BY e.ratingEpisode DESC
            LIMIT 5
            """)
    List<EpisodeDto> findByTopEpisodesBySerieId(Long id);
}



