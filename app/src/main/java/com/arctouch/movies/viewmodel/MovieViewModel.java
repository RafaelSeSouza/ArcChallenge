package com.arctouch.movies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.widget.ImageView;

import com.arctouch.movies.data.UpcomingMovieData;
import com.arctouch.movies.data.UpcomingMovieResult;
import com.arctouch.movies.repository.MovieRepository;

/**
 * View Model for the Movies, connecting the UI with the Repository
 */
public class MovieViewModel extends ViewModel {

    private MovieRepository mRepository;

    private UpcomingMovieResult mUpcomingResults;

    public LiveData<PagedList<UpcomingMovieData>> upcomingMovies;
    public LiveData<String> networkErrors;

    public MovieViewModel(Application application) {
        this.mRepository = new MovieRepository(application);
    }

    public void getUpcomingMovies() {
        mUpcomingResults = mRepository.getUpcomingMovies();

        if (mUpcomingResults != null) {
            upcomingMovies = mUpcomingResults.getData();
            networkErrors = mUpcomingResults.getNetworkErrors();
        }
    }

    public LiveData<UpcomingMovieData> getUpcomingMovie(int id) {
        return mRepository.getUpcomingMovieById(id);
    }

    /**
     * Asks the repository for a Picasso request to load the backdrop image into a image view
     */
    public void getBackdropImage(UpcomingMovieData data, ImageView view) {
        if (data == null || view == null) return;

        mRepository.getImage(data.getBackdropPath()).into(view);
    }

    /**
     * Asks the repository for a Picasso request to load the poster image into a image view
     */
    public void getPosterImage(UpcomingMovieData data, ImageView view) {
        if (data == null || view == null) return;

        mRepository.getImage(data.getPosterPath()).into(view);
    }

    /**
     * Returns a comma separated list with all the genres of this upcoming movie
     */
    public String getGenresCommaSeparated(UpcomingMovieData data) {
        String genres = "";

        for (int genreId : data.getGenreIds()) {
            String genreName = mRepository.getMovieGenre(genreId);

            if (genreName != null) {
                genres = genres.concat(genres.isEmpty() ? "" : " / ").concat(genreName);
            }
        }

        return genres;
    }

}