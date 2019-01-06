package com.recommendation.util;

import java.util.Comparator;

import com.recommendation.service.Movie;

public class ComparatorUtil {
	/* This will sort the movie list based on movie ranking*/
	public static Comparator<Movie> userRatingComparator = new Comparator<Movie>() {

		public int compare(Movie arg0, Movie arg1) {
			if (arg0 != null && arg1 != null) {
				return arg1.getLikesCount() - arg0.getLikesCount();
			}
			return 0;
		}

	};
}
