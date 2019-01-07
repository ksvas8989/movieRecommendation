package com.recommendation.service;

/* this call will have all the information about movie */
public class Movie implements Comparable<Movie> {

	private String name;
	private int releaseYear;
	private String Country;
	private int likesCount;
	private String genre;
	public  String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getReleaseYear() {
		return releaseYear;
	}
	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public int getLikesCount() {
		return likesCount;
	}
	public void setLikesCount(int likesCount) {
		this.likesCount = likesCount;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	
	@Override
public boolean equals(Object anObject) {
    if (this == anObject) {
        return true;
    }
    if (anObject instanceof Movie) {
        Movie movie = (Movie) anObject;
       return name.equals(movie.getName());
    }
    return false;
}

	//this will sort the list based on user likes count
	public int compareTo(Movie movie) {
		 if(likesCount <= movie.likesCount)  
			return 1;  
			else  
			return -1;  
			}  
	}
