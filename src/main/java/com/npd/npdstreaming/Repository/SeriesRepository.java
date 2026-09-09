package com.npd.npdstreaming.Repository;

import com.npd.npdstreaming.model.Category;
import com.npd.npdstreaming.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Serie, Long> {

    Optional<Serie> findByTitleContainingIgnoreCase (String titleSerie);

    List<Serie> findByActorsContainingIgnoreCase (String nameActor);

    List<Serie> findByOrderByImdbRatingDesc ();

    List<Serie> findByGenre (Category category);

}
