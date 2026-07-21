package com.npd.npdstreaming.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeriesData(@JsonAlias("Title") String title,
                         @JsonAlias("totalSeasons") Integer seasons,
                         @JsonAlias("imdbVotes") String imdbRating) {
}
