package com.npd.npdstreaming.Repository;

import com.npd.npdstreaming.model.Category;
import com.npd.npdstreaming.model.Episode;
import com.npd.npdstreaming.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTitleEqualsIgnoreCase(String titleSerie);

    List<Serie> findByActorsContainingIgnoreCase (String nameActor);

    List<Serie> findByOrderByOmdbRatingDesc();

    List<Serie> findByGenre (Category category);

    @Query("""
            SELECT e FROM Episode e 
            JOIN e.serie s 
            WHERE LOWER(s.title) = LOWER(:serieName) 
            ORDER BY e.season ASC
            """)
    List<Episode> findEpisodeBySerieTitle(String serieName);

    @Query("SELECT s FROM Serie s WHERE s.seasons <= :seasons AND s.omdbRating >= :omdbRating")
    List<Serie> seriesBySeasonAndRating(int seasons, double omdbRating);

    @Query("""
            SELECT e FROM Episode e 
            JOIN e.serie s 
            WHERE LOWER(s.title) = LOWER(:serieName) 
            ORDER BY e.assessmentEpisode DESC 
            LIMIT 5""")
    List<Episode> findByTopEpisodes(String serieName);
}
