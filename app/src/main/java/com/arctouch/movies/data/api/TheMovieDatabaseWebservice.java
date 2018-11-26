package com.arctouch.movies.data.api;

import com.arctouch.movies.data.api.generated.GenreList;
import com.arctouch.movies.data.api.generated.UpcomingMovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * The Movie Database API interface for Retrofit use
 */
public interface TheMovieDatabaseWebservice {

    @GET("/3/movie/upcoming")
    Call<UpcomingMovieList> getUpcoming(@Query("api_key") String api_key, @Query("language") String language, @Query("page") int page);

    @GET("3/genre/movie/list")
    Call<GenreList> getMovieGenreList(@Query("api_key") String api_key, @Query("language") String language);

}
