package com.npd.npdstreaming.controller;

import com.npd.npdstreaming.dto.EpisodeDto;
import com.npd.npdstreaming.dto.SerieDto;
import com.npd.npdstreaming.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService serieService;

    @GetMapping
    public List<SerieDto> getSeries() {
       return serieService.getAllSeries();
    }

    @GetMapping("/top5")
    public List<SerieDto> getTopSeries() {
        return serieService.getTop5Series();
    }

    @GetMapping("/releases")
    public List<SerieDto> getRecentReleases() {
        return serieService.getRecentReleases();
    }

    @GetMapping("/{id}")
    public SerieDto getSerieById(@PathVariable Long id) {
        return serieService.getSerieById(id);
    }

    @GetMapping("/{id}/seasons/all")
    public List<EpisodeDto> getAllSeasons(@PathVariable Long id) {
        return serieService.getAllSeasons(id);
    }

    @GetMapping("/{id}/seasons/{season}")
    public List<EpisodeDto> getEpisodeBySeason(@PathVariable Long id, @PathVariable Integer season) {
        return serieService.getEpisodeBySeason(id, season);
    }

    @GetMapping("/category/{categoryName}")
    public List<SerieDto> getSeriesByCategory(@PathVariable String categoryName) {
        return serieService.getSeriesByCategory(categoryName);
    }

    @GetMapping("/{id}/seasons/bests")
    public List<EpisodeDto> getEpisodesByRating(@PathVariable Long id) {
        return serieService.getEpisodesByRating(id);
    }



}
