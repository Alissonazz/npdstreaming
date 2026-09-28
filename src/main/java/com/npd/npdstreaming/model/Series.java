package com.npd.npdstreaming.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private Integer season;
    private Double imdbRating;
    @Enumerated(EnumType.STRING)
    private Category genre;
    private String actors;
    private String poster;
    private String sinopse;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodes> episodes = new ArrayList<>();

    public Series() {}

    public Series(SerieData seriesData) {
        this.title = seriesData.title();
        this.season = seriesData.seasons();
        this.imdbRating = seriesData.imdbRating() != null ? Double.valueOf(seriesData.imdbRating()) : null;
        this.genre = Category.fromString(seriesData.genre().split(",")[0].trim());
        this.actors = seriesData.actors();
        this.poster = seriesData.poster();
        this.sinopse = seriesData.sinopse().trim();
    }

    public void setEpisodes(List<Episodes> episodes) {
        episodes.forEach(e -> e.setSerie(this));
        this.episodes = episodes;
    }

    public List<Episodes> getEpisodes() {
        return episodes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public Category getGenre() {
        return genre;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return
                "\nGênero: " + genre +
                        "\nTítulo: " + title+
                        "\nSinopse: " + sinopse +
                        "\nTemporadas: " + season +
                        "\nAvaliação Imdb: " + imdbRating +
                        "\nAtores: " + actors +
                        "\nLink do Pôster: " + poster +
                        "\nEpisodios: " + episodes;
    }

}