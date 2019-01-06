package com.recommendation.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.recommendation.util.ComparatorUtil;
import com.recommendation.util.MoviesExcelReader;

public class MovieRecommendationLogic {
	public static final int MAX_LIMIT = 20;

	public static void main(String[] args) {
		// we manually adding all the user input in this class
		
		Stack<String> usercountries = new Stack<String>();
		Stack<String> userGenres = new Stack<String>();
		Set<String> watchedHistoy = new LinkedHashSet<String>();
		// We need to add watch history movie here
		watchedHistoy.add("English movie6");

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

		MoviesExcelReader obj = new MoviesExcelReader();
		Map<String, List<Movie>> moviesMap = obj.readMoviesListFromExcel();
		getUserPrefferredMovies(moviesMap, user);
	}

	public static List<Movie> getUserPrefferredMovies(Map<String, List<Movie>> moviesMap, User user) {
		List<Movie> moviesAsperPrefference = new ArrayList<Movie>();
		List<String> watchHistoryList = new ArrayList<String>();
		watchHistoryList.addAll(user.getWatchHistory());

		// this logic is for registered users
		if (user.isRegisteredUser()) {
			for (String userPreferredGenre : user.getGenre()) {
				List<Movie> selectedMovies = moviesMap.get(userPreferredGenre);
				// Iterating movies list as per ranking
				Collections.sort(selectedMovies, ComparatorUtil.userRatingComparator);
				for (Movie movie : selectedMovies) {
					// we should not add watch history into the list 
					if (moviesAsperPrefference.size() < MAX_LIMIT && !watchHistoryList.contains(movie.getName().trim())) {
						moviesAsperPrefference.add(movie);
					}
				}
			}

		}
// logic for unregistered users.
		else {
			List<Movie> unregistedUserMovies = moviesMap.get("unRegisteredUserList");
			Collections.sort(unregistedUserMovies, ComparatorUtil.userRatingComparator);
			for (Movie movie : unregistedUserMovies) {
				if (moviesAsperPrefference.size() < MAX_LIMIT) {
					moviesAsperPrefference.add(movie);
				}
			}
		}
		
		// if still the list size is less than max limit(20), we need to consider user country selection 
		if (moviesAsperPrefference.size() < MAX_LIMIT) {
			List<Movie> allMovies = moviesMap.get("unRegisteredUserList");
			for (String country : user.getCountry()) {

				for (Movie movie : allMovies) {
					if (!moviesAsperPrefference.contains(movie) && country.equals(movie.getCountry().trim())
							&& moviesAsperPrefference.size() < MAX_LIMIT) {
						moviesAsperPrefference.add(movie);
					}

				}
			}
		}
		System.out.println(moviesAsperPrefference.size());
		for (Movie m : moviesAsperPrefference) {
			System.out.println(m.getName() + " :" + m.getLikesCount());
		}
		return moviesAsperPrefference;

	}
}
