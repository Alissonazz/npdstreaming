package com.npd.npdstreaming.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
public class Episodes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer season;
    @Column(name = "title")
    private String titleEpisode;
    @Column(name = "number")
    private Integer numberEpisode;
    @Column(name = "rating")
    private Double ratingEpisode;
    @Column(name = "release_date")
    private LocalDate releaseDateEpisode;

    @ManyToOne
    private Series serie;

    public Episodes(){}

    public Episodes(Integer seasonNumber, EpisodeData episodeData) {
        this.season = seasonNumber;
        this.titleEpisode = episodeData.title();
        this.numberEpisode = episodeData.number();

        try {
            this.ratingEpisode = Double.valueOf(episodeData.ratingEpisode());
        } catch (NumberFormatException ex) {
            this.ratingEpisode = 0.0;
        }

        try {
            this.releaseDateEpisode = LocalDate.parse(episodeData.releaseDate());
        } catch (DateTimeParseException ex) {
            this.releaseDateEpisode = null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Series getSerie() {
        return serie;
    }

    public void setSerie(Series serie) {
        this.serie = serie;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDateEpisode = releaseDate;
    }

    public void setAssessment(Double assessment) {
        this.ratingEpisode = assessment;
    }

    public void setNumber(Integer number) {
        this.numberEpisode = number;
    }

    public void setTitle(String title) {
        this.titleEpisode = title;
    }

    public Integer getSeason() {
        return season;
    }

    public String getTitle() {
        return titleEpisode;
    }

    public Integer getNumber() {
        return numberEpisode;
    }

    public Double getAssessment() {
        return ratingEpisode;
    }

    public LocalDate getReleaseDate() {
        return releaseDateEpisode;
    }

    @Override
    public String toString() {
        return  "season=" + season +
                ", titleEpisode='" + titleEpisode + '\'' +
                ", numberEpisode=" + numberEpisode +
                ", assessmentEpisode='" + ratingEpisode + '\'' +
                ", releaseDateEpisode=" + releaseDateEpisode;

    }
}
