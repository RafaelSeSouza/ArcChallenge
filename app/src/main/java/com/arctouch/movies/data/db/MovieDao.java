package com.arctouch.movies.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.arctouch.movies.data.GenreData;
import com.arctouch.movies.data.UpcomingMovieData;

import java.util.List;

/**
 * Data Access Object for the movie database
 */
@Dao
public interface MovieDao {

    @Query("SELECT * FROM upcoming_movies")
    DataSource.Factory<Integer, UpcomingMovieData> getUpcomingMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUpcomingMovies(List<UpcomingMovieData> upcomingMovieData);

    @Query("SELECT * FROM upcoming_movies WHERE id = :id")
    LiveData<UpcomingMovieData> getUpcomingMovieById(int id);

    @Query("SELECT * FROM movie_genres")
    List<GenreData> getMovieGenres();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieGenres(List<GenreData> genreList);
}
