package com.recommendation.service;

import java.util.Set;
import java.util.Stack;
/* Using this we can pass the user information as input to the system */
public class User {

	// Using stack as user last input value should search first( To follow LIFO)
	private Stack<String> country;
	private Stack<String> genre;
	private boolean isRegisteredUser;
	private Set<String> watchHistory;


	public Stack<String> getCountry() {
		return country;
	}

	public void setCountry(Stack<String> country) {
		this.country = country;
	}

	public Stack<String> getGenre() {
		return genre;
	}

	public void setGenre(Stack<String> genre) {
		this.genre = genre;
	}

	public boolean isRegisteredUser() {
		return isRegisteredUser;
	}

	public void setRegisteredUser(boolean isRegisteredUser) {
		this.isRegisteredUser = isRegisteredUser;
	}

	public Set<String> getWatchHistory() {
		return watchHistory;
	}

	public void setWatchHistory(Set<String> watchHistory) {
		this.watchHistory = watchHistory;
	}
	
}
