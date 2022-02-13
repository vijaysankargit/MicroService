package com.microservice.moviecatalogservice.resources;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import com.microservice.moviecatalogservice.models.ApplicationPropertiesConfiguration;
import com.microservice.moviecatalogservice.models.CatalogItem;
import com.microservice.moviecatalogservice.models.Movie;
import com.microservice.moviecatalogservice.models.Rating;
import com.microservice.moviecatalogservice.models.UserRating;
import com.microservice.moviecatalogservice.service.MovieInfo;
import com.microservice.moviecatalogservice.service.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private WebClient.Builder webclientBuilder;
	
	@Autowired
	MovieInfo movieInfo;
	
	@Autowired
	UserRatingInfo userRatingInfo;
	
	@Value("${welcome.note}")
	private String welcomeNote;
	
	@Autowired
	private ApplicationPropertiesConfiguration appPropConfig;
	
	@RequestMapping("/{userId}")
//	@HystrixCommand(fallbackMethod = "getFallbackCatalog")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		System.out.println(welcomeNote);
		System.out.println(appPropConfig.getHostname()+" "+ appPropConfig.getPort());
		UserRating userRating = userRatingInfo.getUserRating(userId);
		return userRating.getUserRating().stream().map(rating -> {
			return movieInfo.getCatalogItem(rating);
			}).collect(Collectors.toList());
	}
	
//	public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId){
//		return Arrays.asList(new CatalogItem("NO MOVIE","",0));
//	}
	
	@GetMapping("/applicationproperties")
	public String getAppPropsFromConfigServer() {
		return welcomeNote + appPropConfig.getHostname()+" "+ appPropConfig.getPort() ;
		
	}
}
