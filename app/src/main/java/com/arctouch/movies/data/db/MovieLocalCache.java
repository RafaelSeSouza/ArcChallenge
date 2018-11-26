package com.arctouch.movies.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.util.Log;

import com.arctouch.movies.data.GenreData;
import com.arctouch.movies.data.UpcomingMovieData;
import com.arctouch.movies.util.AppThreadPool;

import java.util.List;

/**
 * Accesses the local database
 */
public class MovieLocalCache {

    private static final String LOG_TAG = MovieLocalCache.class.getSimpleName();

    private MovieDao mMovieDao;

    public MovieLocalCache(MovieDao movieDao) {
        this.mMovieDao = movieDao;
    }

    public DataSource.Factory<Integer, UpcomingMovieData> getUpcomingMovies() {
        return mMovieDao.getUpcomingMovies();
    }

    public LiveData<UpcomingMovieData> getUpcomingMovieById(final int id) {
        Log.d(LOG_TAG, "Getting movie with id " + id);
        return mMovieDao.getUpcomingMovieById(id);
    }

    public void insertUpcomingMovies(final List<UpcomingMovieData> listUpcoming) {
        if (listUpcoming == null) {
            Log.d(LOG_TAG, "Cool. No upcoming movies to insert.");
            return;
        }

        Log.d(LOG_TAG, "Inserting " + listUpcoming.size() + " upcoming movies.");

        AppThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                mMovieDao.insertUpcomingMovies(listUpcoming);
            }
        });
    }

    public List<GenreData> getMovieGenres() {
        return mMovieDao.getMovieGenres();
    }

    public void insertMovieGenres(final List<GenreData> listGenres) {
        if (listGenres == null) {
            Log.d(LOG_TAG, "Cool. No movie genres to insert.");
            return;
        }

        Log.d(LOG_TAG, "Inserting " + listGenres.size() + " movie genres.");
        AppThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                mMovieDao.insertMovieGenres(listGenres);
            }
        });
    }

}
