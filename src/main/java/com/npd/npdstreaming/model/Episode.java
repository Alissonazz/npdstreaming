package com.npd.npdstreaming.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer season;
    @Column(name = "Title")
    private String titleEpisode;
    @Column(name = "Number")
    private Integer numberEpisode;
    @Column(name = "Assessment")
    private Double assessmentEpisode;
    @Column(name = "Release_Date")
    private LocalDate releaseDateEpisode;

    @ManyToOne
    private Serie serie;

    public Episode (){}

    public Episode(Integer seasonNumber, EpisodesData episodesData) {
        this.season = seasonNumber;
        this.titleEpisode = episodesData.title();
        this.numberEpisode = episodesData.number();

        try {
            this.assessmentEpisode = Double.valueOf(episodesData.assessment());
        } catch (NumberFormatException ex) {
            this.assessmentEpisode = 0.0;
        }

        try {
            this.releaseDateEpisode = LocalDate.parse(episodesData.releaseDate());
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

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDateEpisode = releaseDate;
    }

    public void setAssessment(Double assessment) {
        this.assessmentEpisode = assessment;
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
        return assessmentEpisode;
    }

    public LocalDate getReleaseDate() {
        return releaseDateEpisode;
    }

    @Override
    public String toString() {
        return  "season=" + season +
                ", titleEpisode='" + titleEpisode + '\'' +
                ", numberEpisode=" + numberEpisode +
                ", assessmentEpisode='" + assessmentEpisode + '\'' +
                ", releaseDateEpisode=" + releaseDateEpisode;

    }
}
