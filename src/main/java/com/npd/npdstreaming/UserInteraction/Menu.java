package com.npd.npdstreaming.UserInteraction;
import com.npd.npdstreaming.Repository.SeriesRepository;
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
    private List<SeriesData> listedSeries = new ArrayList<>();
    private List<Serie> series = new ArrayList<>();

    @Autowired
    private SeriesRepository seriesRepository;

    @Value("${omdb.api.key}")
    private String OmdbApiKey;

    public Menu (ApiConsumption apiConsumption, DataConvert convert) {
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
                    6 - Top 5 séries
                    
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
                    topSeries();
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
        SeriesData data = getSeriesData();
        Serie s = new Serie(data);
        seriesRepository.save(s);
        System.out.println(data);
    }

    private SeriesData getSeriesData() {
        System.out.println("Digite o nome da série que deseja buscar: ");
        var serieName  = scan.nextLine();
        var json = apiConsumption.obtainData(ADDRESS + serieName.replace(" ", "+") + FINAL_ADDRESS + OmdbApiKey);
        SeriesData data = convert.obtainData(json, SeriesData.class);
        return data;
    }

    private void searchEpisodes(){
        listSeries();
        System.out.println("Digite uma série para ver os episodios: ");
        var serieName = scan.nextLine();

        Optional<Serie> serie = seriesRepository.findByTitleContainingIgnoreCase(serieName);

        if (serie.isPresent()) {
            List<SeasonsData> seasons = new ArrayList<>();

            for (int i = 1; i <= serie.get().getSeasons(); i++) {
                var json = apiConsumption.obtainData(ADDRESS + serie.get().getTitle().replace(" ", "+") + "&season=" + i + FINAL_ADDRESS + OmdbApiKey);
                SeasonsData seasonsData = convert.obtainData(json, SeasonsData.class);
                seasons.add(seasonsData);
            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(s -> s.episodes().stream()
                            .map(e -> new Episode(s.number(), e)))
                    .collect(Collectors.toList());
            serie.get().setEpisodes(episodes);
            seriesRepository.save(serie.get());
        } else {
            System.out.println("Série não encontrada!");
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

    public void searchForSeriesByCategory() {
        System.out.println("Digite a categoria que deseja buscar: ");
        var categoryName = scan.nextLine();
        Category category = Category.fromStringPt(categoryName);
        List<Serie> seriesByCategory = seriesRepository.findByGenre(category);
        System.out.println("\nSéries da catégoria " + categoryName + ":");
        seriesByCategory.forEach(s ->
                System.out.println("\nTítulo: " + s.getTitle() +
                        "\nSinopse: " + s.getSynopsis() +
                        "\nAvaliação: " + s.getImdbRating()));
    }

}
