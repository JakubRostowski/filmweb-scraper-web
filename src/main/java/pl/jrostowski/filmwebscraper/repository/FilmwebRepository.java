package pl.jrostowski.filmwebscraper.repository;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.Movie;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class FilmwebRepository {

    private static final String URL = "https://www.filmweb.pl";

    public Map<Integer, Movie> getTopList() throws IOException {
        Elements urls = getUrls();
        Elements ranks = getRanks();

        Map<Integer, Movie> listOfMovies = new TreeMap<>();
        urls.parallelStream().forEach(href -> {
            int rankOfMovie = Integer.parseInt(ranks.get(urls.indexOf(href)).text());
            try {
                listOfMovies.put(rankOfMovie, getMovieData(rankOfMovie, href));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return listOfMovies;
    }

    private Elements getRanks() throws IOException {
        Elements ranks = new Elements();
        for (int i = 1; i <= 20; i++) {
            Connection connectList = Jsoup.connect(URL + "/ajax/ranking/film/" + i);
            Document documentList = connectList.get();
            Elements pageRanks = documentList.select("span.rankingType__position");
            ranks.addAll(pageRanks);
        }
        return ranks;
    }

    private Elements getUrls() throws IOException {
        Elements urls = new Elements();
        for (int i = 1; i <= 20; i++) {
            Connection connectList = Jsoup.connect(URL + "/ajax/ranking/film/" + i);
            Document documentList = connectList.get();
            Elements pageUrls = documentList.select("div.rankingType__card > div.rankingType__header > div > h2 > a");
            urls.addAll(pageUrls);
        }
        return urls;
    }

    private Movie getMovieData(int rank, Element href) throws IOException {
        Connection connectMovie = Jsoup.connect(URL + href.attr("href"));
        Document documentMovie = connectMovie.get();

        String title = documentMovie.select(".filmCoverSection__title").text();
        int year = Integer.parseInt(documentMovie.select(".filmCoverSection__year").text());
        String originalTitle = documentMovie.select(".filmCoverSection__originalTitle").text();
        double rate = Double.parseDouble(documentMovie.select("span.filmRating__rateValue:nth-child(2)").text().replaceAll(",", "."));
        double criticsRate;
        if (documentMovie.select("span.filmRating__rateValue:nth-child(1)").text().contains(",")) {
            criticsRate = Double.parseDouble(documentMovie.select("span.filmRating__rateValue:nth-child(1)").text().replaceAll(",", "."));
        } else {
            criticsRate = -1;
        }
        String length = documentMovie.select(".filmCoverSection__duration").text().replaceAll("godz.", "h").replaceAll("min.", "min");
        String director = documentMovie.select(".filmPosterSection__info.filmInfo > div:nth-child(4)").text().replaceAll("więcej", "");
        String screenwriter;
        String genre;
        String countryOfOrigin;
        if (director.isEmpty()) {
            director = documentMovie.select(".filmPosterSection__info.filmInfo > div:nth-child(5)").text().replaceAll("więcej", "");
            screenwriter = documentMovie.select(".filmPosterSection__info.filmInfo > div:nth-child(7)").text().replaceAll("więcej", "");
            genre = documentMovie.select(".filmPosterSection__info.filmInfo > div:nth-child(9)").text();
            countryOfOrigin = documentMovie.select(".filmPosterSection__info.filmInfo > div:nth-child(11)").text();
        } else {
            screenwriter = documentMovie.select(".filmPosterSection__info.filmInfo > div:nth-child(6)").text().replaceAll("więcej", "");
            genre = documentMovie.select(".filmPosterSection__info.filmInfo > div:nth-child(8)").text();
            countryOfOrigin = documentMovie.select(".filmPosterSection__info.filmInfo > div:nth-child(10)").text();
        }
        String poster = documentMovie.select("#filmPoster").attr("src");
        return new Movie(rank, title, year, originalTitle, rate, criticsRate, length, director, screenwriter, genre, countryOfOrigin, poster);
    }
}
