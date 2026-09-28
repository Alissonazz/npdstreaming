package com.npd.npdstreaming.service;

import com.npd.npdstreaming.dto.EpisodeDto;
import com.npd.npdstreaming.dto.SerieDto;
import com.npd.npdstreaming.model.Category;
import com.npd.npdstreaming.model.Series;
import com.npd.npdstreaming.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    SeriesRepository seriesRepository;

    public List<SerieDto> getAllSeries() {
        return dataConvert(seriesRepository.findAll());
    }

    public List<SerieDto> getRecentReleases() {
        return dataConvert(seriesRepository.findRecentReleases(PageRequest.of(0, 5)));
    }

    public List<SerieDto> getTop5Series() {
        return dataConvert(seriesRepository.findTop5ByOrderByImdbRatingDesc());
    }

    public SerieDto getSerieById(Long id) {
        Optional<Series> serie = seriesRepository.findById(id);
        if(serie.isPresent()) {
            Series s = serie.get();
            return new SerieDto(s.getId(), s.getTitle(), s.getSeason(), s.getImdbRating(), s.getGenre(),
                    s.getActors(), s.getPoster(), s.getSinopse());
        }
        return null;
    }

    public List<EpisodeDto> getAllSeasons(Long id) {
        Optional<Series> serie = seriesRepository.findById(id);
        if(serie.isPresent()) {
            Series s = serie.get();
            return s.getEpisodes().stream()
                    .map(e -> new EpisodeDto(e.getTitle(), e.getNumber(), e.getSeason()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodeDto> getEpisodeBySeason(Long id, Integer season) {
        return seriesRepository.findEpisodesBySeason(id, season);
    }

    public List<SerieDto> getSeriesByCategory(String categoryName) {
        Category category = Category.fromString(categoryName);
        return dataConvert(seriesRepository.findByGenre(category));
    }

    public List<EpisodeDto> getEpisodesByRating(Long id) {
        return seriesRepository.findByTopEpisodesBySerieId(id);
    }

    private List<SerieDto> dataConvert(List<Series> series) {
        return series.stream()
                .map(s -> new SerieDto(s.getId(), s.getTitle(), s.getSeason(), s.getImdbRating(), s.getGenre(),
                        s.getActors(), s.getPoster(), s.getSinopse()))
                .collect(Collectors.toList());
    }


}
