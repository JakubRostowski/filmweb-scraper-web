package pl.jrostowski.filmwebscraper.repository;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class MovieRepository {

    private final String URL = "https://www.filmweb.pl";
    private static EntityManager em;

    public MovieRepository(EntityManager em) {
        MovieRepository.em = em;
    }

    public List getMoviesFromDatabase() {
        return em.createQuery("from Movie").getResultList();
    }

    public Map<Integer, Movie> getTopList() throws IOException {
        Elements urls = getUrls();
        Elements ranks = getRanks();

        Map<Integer, Movie> listOfMovies = new TreeMap<>();
        urls.parallelStream().forEach((href) -> {
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

        String title = documentMovie.select(".filmCoverSection__title > span:nth-child(1)").text();
        int year = Integer.parseInt(documentMovie.select(".filmCoverSection__year").text());
        String originalTitle = documentMovie.select(".filmCoverSection__orginalTitle").text();
        double rate = Double.parseDouble(documentMovie.select("span.filmRating__rateValue:nth-child(2)").text().replaceAll(",", "."));
        double criticsRate;
        if (documentMovie.select("span.filmRating__rateValue:nth-child(1)").text().contains(",")) {
            criticsRate = Double.parseDouble(documentMovie.select("span.filmRating__rateValue:nth-child(1)").text().replaceAll(",", "."));
        } else {
            criticsRate = -1;
        }
        String length = documentMovie.select(".filmCoverSection__filmTime").text().replaceAll("godz.", "h").replaceAll("min.", "min");
        String director = documentMovie.select("div.filmInfo__info:nth-child(3)").text().replaceAll("więcej", "");
        String screenwriter;
        String genre;
        String countryOfOrigin;
        if (director.isEmpty()) {
            director = documentMovie.select(".filmPosterSection__info > div:nth-child(4)").text().replaceAll("więcej", "");
            screenwriter = documentMovie.select("div.filmInfo__info:nth-child(6)").text().replaceAll("więcej", "");
            genre = documentMovie.select("div.filmInfo__info:nth-child(8)").text();
            countryOfOrigin = documentMovie.select("div.filmInfo__info:nth-child(10)").text();
        } else {
            screenwriter = documentMovie.select("div.filmInfo__info:nth-child(5)").text().replaceAll("więcej", "");
            genre = documentMovie.select("div.filmInfo__info:nth-child(7)").text();
            countryOfOrigin = documentMovie.select("div.filmInfo__info:nth-child(9)").text();
        }
        return new Movie(rank, title, year, originalTitle, rate, criticsRate, length, director, screenwriter, genre, countryOfOrigin);
    }

    public void createDatabase(Map<Integer, Movie> movies) {
        for (Map.Entry<Integer, Movie> movie : movies.entrySet()) {
            addMovie(movie.getValue());
        }
    }

    public void addMovie(Movie movie) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(movie);
        transaction.commit();
    }

    public boolean checkIfEmpty() {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM movie");
        String result = query.getResultList().get(0).toString();
        return result.equals("0");

    }

    public void deleteMovie(Movie movie) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(movie);
        transaction.commit();
    }

    public void updateChangedMovie(Movie oldMovie, Movie newMovie) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        oldMovie.setPosition(newMovie.getPosition());
        oldMovie.setRate(newMovie.getRate());
        oldMovie.setCriticsRate(newMovie.getCriticsRate());
        oldMovie.setTimeOfModification(new Timestamp(System.currentTimeMillis()));
        transaction.commit();
    }

    public void updatePositionToUnused(Movie movie) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        movie.setPosition(-1);
        transaction.commit();
    }

    public void updateTimeOfModification(Movie checkedMovie) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        checkedMovie.setTimeOfModification(new Timestamp(System.currentTimeMillis()));
        transaction.commit();
    }

    public void exportToExcel(Map<Integer, Movie> map, boolean IsNewExcelFormat) throws IOException {
        Workbook workbook = createWorkbookObject(IsNewExcelFormat);
        Sheet sheet = workbook.createSheet("Toplist");
        setHeaders(sheet);
        writeRows(map, sheet);
        autoSizeColumns(sheet);

        FileOutputStream fileOut = getFileExtension(IsNewExcelFormat);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    private void writeRows(Map<Integer, Movie> map, Sheet sheet) {
        map.forEach((rank, movie) -> {
            Row row = sheet.createRow(rank);

            for (int i = 0; i <= 10; i++) {
                Cell cell = row.createCell(i);
                switch (i) {
                    case 0:
                        cell.setCellValue(rank + ".");
                        break;
                    case 1:
                        cell.setCellValue(movie.getTitle());
                        break;
                    case 2:
                        cell.setCellValue(movie.getYear());
                        break;
                    case 3:
                        cell.setCellValue(movie.getOriginalTitle());
                        break;
                    case 4:
                        cell.setCellValue(movie.getRate());
                        break;
                    case 5:
                        cell.setCellValue(movie.getCriticsRate());
                        break;
                    case 6:
                        cell.setCellValue(movie.getLength());
                        break;
                    case 7:
                        cell.setCellValue(movie.getDirector());
                        break;
                    case 8:
                        cell.setCellValue(movie.getScreenwriter());
                        break;
                    case 9:
                        cell.setCellValue(movie.getGenre());
                        break;
                    case 10:
                        cell.setCellValue(movie.getCountryOfOrigin());
                        break;
                }
            }
        });
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < getHeaders().length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void setHeaders(Sheet sheet) {
        Row rowHeader = sheet.createRow(0);
        rowHeader.setHeightInPoints(30);
        String[] headers = getHeaders();
        for (int i = 0; i < 11; i++) {
            rowHeader.createCell(i).setCellValue(headers[i]);
        }
    }

    private Workbook createWorkbookObject(boolean IsNewExcelFormat) {
        if (IsNewExcelFormat) {
            return new XSSFWorkbook();
        } else {
            return new HSSFWorkbook();
        }
    }

    private FileOutputStream getFileExtension(boolean IsNewExcelFormat) throws IOException {
        if (IsNewExcelFormat) {
            return new FileOutputStream("toplist.xlsx");
        } else {
            return new FileOutputStream("toplist.xls");
        }
    }

    private String[] getHeaders() {
        return new String[]{
                "Rank", "Title", "Year", "Original title", "Rate", "Critics' rate",
                "Length", "Director", "Screenwriter", "Genre", "Country of origin"
        };
    }
}
