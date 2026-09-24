package com.npd.npdstreaming.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeData(@JsonAlias("Title") String title,
                          @JsonAlias("Episode")Integer number,
                          @JsonAlias("imdbRating") String ratingEpisode,
                          @JsonAlias("Released") String releaseDate) {

}
