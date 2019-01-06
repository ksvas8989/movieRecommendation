package com.recommendation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.recommendation.service.Movie;

public class MoviesExcelReader {
	
// We are passing movies information in Excel sheet, as we are not hitting database for information
// this excel file has to modified manually  
// information order as follows "movie name", "release year", "country", "likes", "genre"	
	
	private static final String FILE_PATH = "./moviesList.xlsx";

	// this method will read the excel and put the information in  map,
	// Here we are having movies list based on Genre, it will also provides all the data for
	// unregistered users
	public Map<String, List<Movie>> readMoviesListFromExcel() {

		Map<String, List<Movie>> moviesMap = new LinkedHashMap<String, List<Movie>>();
		List<Movie> allMoviesList = new ArrayList<Movie>();
		List<Movie> moviesList = null;
		String movieGenre = "";
		try {
			// we are using apache poi to read the excel
			FileInputStream excelFile = new FileInputStream(new File(FILE_PATH));
			Workbook workbook = new XSSFWorkbook(excelFile);

			int numberOfSheets = workbook.getNumberOfSheets();

			// looping over each workbook sheet
			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				Iterator<Row> rowIterator = sheet.iterator();

				// iterating over each row
				while (rowIterator.hasNext()) {

					Movie movie = new Movie();
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {

						movie.setName(cellIterator.next().getStringCellValue());
						movie.setReleaseYear((int) cellIterator.next().getNumericCellValue());
						movie.setCountry(cellIterator.next().getStringCellValue());
						movie.setLikesCount((int) cellIterator.next().getNumericCellValue());
						movieGenre = cellIterator.next().getStringCellValue();
						movie.setGenre(movieGenre);
					}

					allMoviesList.add(movie);
					if (moviesMap.containsKey(movieGenre)) {
						moviesMap.get(movieGenre).add(movie);
					} else {
						moviesList = new ArrayList<Movie>();
						moviesMap.put(movieGenre, moviesList);
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
// adding information for unregistered users
		moviesMap.put("unRegisteredUserList", allMoviesList);
		return moviesMap;

	}
}
