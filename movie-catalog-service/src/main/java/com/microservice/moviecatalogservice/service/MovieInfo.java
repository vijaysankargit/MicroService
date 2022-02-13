package com.microservice.moviecatalogservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.moviecatalogservice.models.CatalogItem;
import com.microservice.moviecatalogservice.models.Movie;
import com.microservice.moviecatalogservice.models.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class MovieInfo {
	
	@Autowired
	private RestTemplate restTemplate;
	
	/*threadpoolkey is used for bulkhead pattern because if each microservice is allocated with particular threads then it
	cannot interfere/use other microservice threads*/
	@HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
			threadPoolKey = "movieInfoPool",
			threadPoolProperties = {
					@HystrixProperty(name = "coreSize", value = "20"),
					@HystrixProperty(name = "maxQueueSize", value = "10")
			})
	public CatalogItem getCatalogItem(Rating rating) {
	////Microservice to Microservice Communication via Rest Template
				Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
				
				//Microservice to Microservice Communication via Web Client
//				Movie movie = webclientBuilder.build()
//				.get()
//				.uri("http://localhost:8082/movies/"+rating.getMovieId())
//				.retrieve()
//				.bodyToMono(Movie.class)
//				.block();
				
				return new CatalogItem(movie.getName(),"autobots",rating.getRating());
	}
	
	public CatalogItem getFallbackCatalogItem(Rating rating) {
		return new CatalogItem("Movie Name Not Found","",rating.getRating());
	}

}
