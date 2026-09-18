package com.npd.npdstreaming.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private Integer seasons;
    private Double omdbRating;
    @Enumerated(EnumType.STRING)
    private Category genre;
    private String actors;
    private String poster;
    private String synopsis;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episode> episodes = new ArrayList<>();

    public Serie() {}

    public Serie(SeriesData seriesData) {
        this.title = seriesData.title();
        this.seasons = seriesData.seasons();
        this.omdbRating = OptionalDouble.of(Double.valueOf(seriesData.imdbRating())).orElse(0);
        this.genre = Category.fromString(seriesData.genre().split(",")[0].trim());
        this.actors = seriesData.actors();
        this.poster = seriesData.poster();
        this.synopsis = seriesData.synopsis().trim();
    }

    public void setEpisodes(List<Episode> episodes) {
        episodes.forEach(e -> e.setSerie(this));
        this.episodes = episodes;
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

    public Integer getSeasons() {
        return seasons;
    }

    public void setSeasons(Integer seasons) {
        this.seasons = seasons;
    }

    public Double getOmdbRating() {
        return omdbRating;
    }

    public void setOmdbRating(Double omdbRating) {
        this.omdbRating = omdbRating;
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

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public String toString() {
        return
                "\nGênero: " + genre +
                        "\nTítulo: " + title+
                        "\nSinopse: " + synopsis +
                        "\nTemporadas: " + seasons+
                        "\nAvaliação Imdb: " + omdbRating +
                        "\nAtores: " + actors +
                        "\nLink do Pôster: " + poster +
                        "\nEpisodios: " + episodes;
    }

}