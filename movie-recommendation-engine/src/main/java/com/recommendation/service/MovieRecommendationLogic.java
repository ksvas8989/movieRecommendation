package com.recommendation.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class MovieRecommendationLogic {
	// MAX limit is 20 in this use case
	public static final int MAX_LIMIT = 20;

	public static boolean dataLoaded = false;

	public static Set<Movie> allMoviesSortedByLikesCount = new TreeSet<Movie>();

	public static Map<String, Set<Movie>> moviesGroupedByGenre = new HashMap<String, Set<Movie>>();

	public static Map<String, Set<Movie>> moviesGroupedByCountry = new HashMap<String, Set<Movie>>();

	// We are passing movies information in Excel sheet, as we are not hitting
	// database for information
	// this excel file has to modified manually
	// information order as follows "movie name", "release year", "country",
	// "likes", "genre"

	private static final String FILE_PATH = "./moviesList.xlsx";

	public static void main(String[] args) {
		// we manually adding all the user input in this class

		Stack<String> usercountries = new Stack<String>();
		Stack<String> userGenres = new Stack<String>();
		Set<Movie> watchedHistoy = new TreeSet<Movie>();
		Movie movie = new Movie();
		movie.setName("English movie99");
		movie.setCountry("USA");
		movie.setGenre("Comedy");
		movie.setReleaseYear(2019);
	// We need to add watch history movie here
		watchedHistoy.add(movie);

		usercountries.push("INDIA");
		usercountries.push("USA");
		userGenres.push("Horror");
		userGenres.push("Comedy");

		User user = new User();
		user.setCountry(usercountries);
		user.setGenre(userGenres);
		// please change below value to "false" for unregistered users
		user.setRegisteredUser(true);
		user.setWatchHistory(watchedHistoy);
// we are loading all the user information, this can be user further
		if (!dataLoaded) {

			readMoviesListFromExcel();
			dataLoaded = true;
		}

		getUserPrefferredMovies(user);
	}

	public static List<Movie> getUserPrefferredMovies(User user) {
		List<Movie> moviesAsperPrefference = new ArrayList<Movie>();

		Set<Movie> watchedHistory = user.getWatchHistory();
		// this logic is for registered users
		if (user.isRegisteredUser()) {
			for (String userPreferredGenre : user.getGenre()) {
				Set<Movie> selectedMovies = moviesGroupedByGenre.get(userPreferredGenre);
				if (addMoviesToRecommendationList(moviesAsperPrefference, selectedMovies, watchedHistory)) {
					break;
				}
			}

			// if still the list size is less than max limit(20), we need to
			// consider user country selection
			if (moviesAsperPrefference.size() < MAX_LIMIT) {
				for (String userCountry : user.getCountry()) {
					Set<Movie> selectedMovies = moviesGroupedByCountry.get(userCountry);
					if (addMoviesToRecommendationList(moviesAsperPrefference, selectedMovies, watchedHistory)) {
						break;
					}
				}
			}

			if (moviesAsperPrefference.size() < MAX_LIMIT) {
				addMoviesToRecommendationList(moviesAsperPrefference, watchedHistory, null);
			}

		}
		// logic for unregistered users.
		else {
			addMoviesToRecommendationList(moviesAsperPrefference, allMoviesSortedByLikesCount, null);
		}

		for (Movie m : moviesAsperPrefference) {
			System.out.println(m.getName() + " :" + m.getLikesCount() + " :" + m.getGenre() + " : " + m.getCountry());
		}
		return moviesAsperPrefference;

	}
// Generic method to add information to result list
	private static boolean addMoviesToRecommendationList(List<Movie> moviesAsperPrefference, Set<Movie> moviesToAdd,
			Set<Movie> watchedHistory) {
		if (moviesToAdd == null) {
			return false;
		}
		for (Movie movie : moviesToAdd) {
			if (moviesAsperPrefference.size() >= MAX_LIMIT) {
				return true;
			}

			if (!moviesAsperPrefference.contains(movie)
					&& (watchedHistory == null || !watchedHistory.contains(movie))) {
				moviesAsperPrefference.add(movie);
			}
		}

		return false;
	}

	// this method will read the excel and put the information in map,
	// Here we are having movies list based on Genre, it will also provides all
	// the data for unregistered users
	public static void readMoviesListFromExcel() {

		try {
			// we are using apache poi to read the excel
			FileInputStream excelFile = new FileInputStream(new File(FILE_PATH));
			Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook(excelFile);

			int numberOfSheets = workbook.getNumberOfSheets();

			Set<Movie> moviesList = null;
			String movieGenre = "";

			// looping over each workbook sheet
			for (int i = 0; i < numberOfSheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				Iterator<Row> rowIterator = sheet.iterator();

				// iterating over each row
				while (rowIterator.hasNext()) {

					Movie movie = new Movie();
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					if (cellIterator.hasNext()) {

						movie.setName(cellIterator.next().getStringCellValue().trim());
						movie.setReleaseYear((int) cellIterator.next().getNumericCellValue());
						movie.setCountry(cellIterator.next().getStringCellValue().trim());
						movie.setLikesCount((int) cellIterator.next().getNumericCellValue());
						movieGenre = cellIterator.next().getStringCellValue().trim();
						movie.setGenre(movieGenre);
					}

					allMoviesSortedByLikesCount.add(movie);

					if (moviesGroupedByGenre.containsKey(movieGenre)) {
						moviesGroupedByGenre.get(movieGenre).add(movie);
					} else {
						moviesList = new TreeSet<Movie>();
						moviesList.add(movie);
						moviesGroupedByGenre.put(movieGenre, moviesList);
					}

					String countryName = movie.getCountry().trim();
					if (moviesGroupedByCountry.containsKey(countryName)) {
						moviesGroupedByCountry.get(countryName).add(movie);
					} else {
						moviesList = new TreeSet<Movie>();
						moviesList.add(movie);
						moviesGroupedByCountry.put(countryName, moviesList);
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
