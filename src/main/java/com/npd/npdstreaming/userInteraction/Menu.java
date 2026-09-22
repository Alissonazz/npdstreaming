package com.npd.npdstreaming.userInteraction;

import com.npd.npdstreaming.repository.SeriesRepository;
import com.npd.npdstreaming.model.*;
import com.npd.npdstreaming.service.ApiConsumption;
import com.npd.npdstreaming.service.DataConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Menu {
    private Scanner scan = new Scanner(System.in);
    private final ApiConsumption apiConsumption;
    private final DataConvert convert;

    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String FINAL_ADDRESS = "&apikey=";
    private List<SerieData> listedSeries = new ArrayList<>();
    private List<Serie> series = new ArrayList<>();
    private String serieName;

    @Autowired
    private SeriesRepository seriesRepository;

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    public Menu(ApiConsumption apiConsumption, DataConvert convert) {
        this.apiConsumption = apiConsumption;
        this.convert = convert;
    }

    public void showMenu() {
        var option = -1;
        while (option != 0) {
            var menu = """
                    \n
                    1 - Buscar séries
                    2 - Buscar séries por ator
                    3 - Buscar séries por categoria
                    4 - Buscar episódios
                    5 - Listar séries buscadas
                    6 - Filtrar séries
                    7 - Top 5 séries
                    8 - Top 5 Episódios
                    
                    0 - Sair                                
                    """;

            System.out.println(menu);
            option = scan.nextInt();
            scan.nextLine();

            switch (option) {
                case 1:
                    searchWebSeries();
                    break;
                case 2:
                    searchForSeriesByActor();
                    break;
                case 3:
                    searchForSeriesByCategory();
                    break;
                case 4:
                    searchEpisodes();
                    break;
                case 5:
                    listSeries();
                    break;
                case 6:
                    filterBySeasonAndRating();
                    break;
                case 7:
                    topSeries();
                    break;
                case 8:
                    topEpisodes();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void searchWebSeries() {
        series = seriesRepository.findAll();
        System.out.println("Digite o nome da série que deseja buscar: ");
        serieName = scan.nextLine();
        var json = apiConsumption.obtainData(ADDRESS + serieName.replace(" ", "+") + FINAL_ADDRESS + omdbApiKey);
        SerieData data = convert.obtainData(json, SerieData.class);
        Serie s = new Serie(data);
        if (series.stream().anyMatch(serie -> serie.getTitle().equalsIgnoreCase(serieName))) {
            System.out.println(data);
        } else {
            System.out.println(data);
            seriesRepository.save(s);
            saveEpisode(s);
        }
    }

    private void saveEpisode(Serie s) {
        Optional<Serie> serie = seriesRepository.findByTitleEqualsIgnoreCase(s.getTitle());
        if (serie.isPresent()) {
            List<SeasonData> seasons = new ArrayList<>();

            for (int i = 1; i <= serie.get().getSeason(); i++) {
                var jsonSeason = apiConsumption.obtainData(ADDRESS + serie.get().getTitle().replace(" ", "+") + "&season=" + i + FINAL_ADDRESS + omdbApiKey);
                SeasonData seasonData = convert.obtainData(jsonSeason, SeasonData.class);
                seasons.add(seasonData);
            }
            List<Episode> episodes = seasons.stream()
                    .flatMap(sd -> sd.episodes().stream()
                            .map(e -> new Episode(sd.number(), e)))
                    .collect(Collectors.toList());
            serie.get().setEpisodes(episodes);
            seriesRepository.save(serie.get());
        }
    }

    private void printEpisodes(String serieName) {
        List<Episode> episodes = seriesRepository.findEpisodeBySerieTitle(serieName);
        episodes.forEach(e -> System.out.printf(
                "Título: %s - Temporada: %d - Episódio: %d - Avaliação: %.1f%n",
                e.getTitle(), e.getSeason(), e.getNumber(), e.getAssessment()));
    }

    private void searchEpisodes() {
        System.out.println("Digite uma série para ver os episodios: ");
        serieName = scan.nextLine();
        Optional<Serie> serie = seriesRepository.findByTitleEqualsIgnoreCase(serieName);

        if (serie.isPresent()) {
            printEpisodes(serieName);
        } else {
            var json = apiConsumption.obtainData(ADDRESS + serieName.replace(" ", "+") + FINAL_ADDRESS + omdbApiKey);
            SerieData data = convert.obtainData(json, SerieData.class);
            Serie s = new Serie(data);
            seriesRepository.save(s);
            saveEpisode(s);
            printEpisodes(serieName);
        }
    }

    private void listSeries() {
        series = seriesRepository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);

    }

    private void searchForSeriesByActor() {
        System.out.println("Digite o nome do ator para ver suas produções: ");
        var actorName = scan.nextLine();
        List<Serie> seriesByActor = seriesRepository.findByActorsContainingIgnoreCase(actorName);
        if (seriesByActor.isEmpty()) {
            System.out.println("Certifique-se de que o nome do ator foi digitado corretamente.");
        } else {
            seriesByActor.forEach(s ->
                    System.out.println(s.getTitle() + " - Avaliação: " + s.getImdbRating())
            );
        }
    }

    public void topSeries() {
        System.out.println("Top 5 séries: ");
        List<Serie> seriesOrderByRating = seriesRepository.findByOrderByImdbRatingDesc();
        List<Serie> topFive = seriesOrderByRating.stream()
                .limit(5)
                .collect(Collectors.toList());

        topFive.forEach(s ->
                System.out.println(s.getTitle() + " - Avaliação: " + s.getImdbRating()));
    }

    private void searchForSeriesByCategory() {
        System.out.println("Digite gênero que deseja buscar: ");
        var genreName = scan.nextLine();
        Category category = Category.fromStringPt(genreName);
        List<Serie> seriesByGenre = seriesRepository.findByGenre(category);
        System.out.println("\nSéries da catégoria " + genreName + ":");
        seriesByGenre.forEach(s ->
                System.out.println("\nTítulo: " + s.getTitle() +
                        "\nSinopse: " + s.getSynopsis() +
                        "\nAvaliação: " + s.getImdbRating()));
    }


    private void filterBySeasonAndRating() {
        System.out.println("Filtrar séries até quantas temporadas? ");
        var totalSeasons = scan.nextInt();
        System.out.println("Com avaliação a partir de qual valor? ");
        var rating = scan.nextDouble();
        scan.nextLine();
        List<Serie> seriesFilter = seriesRepository.seriesBySeasonAndRating(totalSeasons, rating);
        System.out.println("*** Séries filtradas ***");
        seriesFilter.forEach(s ->
                System.out.println(s.getTitle() + "  - avaliação: " + s.getImdbRating()));
    }

    private void topEpisodes() {
        searchWebSeries();
        List<Episode> topFiveEpisodes = seriesRepository.findByTopEpisodes(serieName);
        topFiveEpisodes.forEach(e -> System.out.printf(
                "Título: %s - Temporada: %d - Episódio: %d - Avaliação: %.1f\n",
                e.getTitle(), e.getSeason(), e.getNumber(), e.getAssessment()));
    }

}



