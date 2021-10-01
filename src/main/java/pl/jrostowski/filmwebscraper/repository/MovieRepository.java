package pl.jrostowski.filmwebscraper.repository;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;
import pl.jrostowski.filmwebscraper.entity.Movie;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public class MovieRepository {

    private static EntityManager em;

    public MovieRepository(EntityManager em) {
        MovieRepository.em = em;
    }

    public List getMoviesFromDatabase() {
        return em.createQuery("from Movie").getResultList();
    }

    public void createDatabase(Map<Integer, Movie> movies) {
        for (Map.Entry<Integer, Movie> movie : movies.entrySet()) {
            addMovie(movie.getValue());
        }
    }

    @Transactional
    public void addMovie(Movie movie) {
        em.persist(movie);
    }

    public boolean checkIfEmpty() {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM movie");
        String result = query.getResultList().get(0).toString();
        return result.equals("0");
    }

    @Transactional
    public void deleteMovie(Movie movie) {
        em.remove(movie);
    }

    @Transactional
    public void updateChangedMovie(Movie oldMovie, Movie newMovie) {
        oldMovie.setPosition(newMovie.getPosition());
        oldMovie.setRate(newMovie.getRate());
        oldMovie.setCriticsRate(newMovie.getCriticsRate());
        oldMovie.setTimeOfModification(new Timestamp(System.currentTimeMillis()));
    }

    @Transactional
    public void updatePositionToUnused(Movie movie) {
        movie.setPosition(-1);
    }

    @Transactional
    public void updateTimeOfModification(Movie checkedMovie) {
        checkedMovie.setTimeOfModification(new Timestamp(System.currentTimeMillis()));
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
