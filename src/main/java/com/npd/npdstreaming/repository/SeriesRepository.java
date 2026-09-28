package com.npd.npdstreaming.repository;

import com.npd.npdstreaming.dto.EpisodeDto;
import com.npd.npdstreaming.model.Category;
import com.npd.npdstreaming.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    List<Series> findByGenre(Category category);

    List<Series> findTop5ByOrderByImdbRatingDesc();

    @Query("""
        SELECT s FROM Series s 
        JOIN s.episodes e 
        WHERE e.releaseDateEpisode IS NOT NULL
        GROUP BY s.id 
        ORDER BY MAX(e.releaseDateEpisode) DESC
        """)
    List<Series> findRecentReleases(Pageable pageable);

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE s.id = :id and e.season = :season")
    List<EpisodeDto> findEpisodesBySeason(Long id, Integer season);

    @Query("""
            SELECT e FROM Episodes e
            JOIN e.serie s
            WHERE s.id = :id
            ORDER BY e.ratingEpisode DESC
            LIMIT 5
            """)
    List<EpisodeDto> findByTopEpisodesBySerieId(Long id);
}



