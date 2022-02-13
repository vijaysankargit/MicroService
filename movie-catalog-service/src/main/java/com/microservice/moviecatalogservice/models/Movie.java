package com.microservice.moviecatalogservice.models;

public class Movie {
	
	private String movieId;
	private String name;
	
	//If we are using RestTemplate to get Movie Info from movieinfo Microservice,then we need a empty constructor
	public void movie() {}
	
	public Movie(String movieId, String name) {
		this.movieId = movieId;
		this.name = name;
	}
	public String getMovieId() {
		return movieId;
	}
	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
