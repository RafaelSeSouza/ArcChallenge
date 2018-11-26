package com.arctouch.movies.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.net.Uri;
import android.util.Log;

import com.arctouch.movies.data.GenreData;
import com.arctouch.movies.data.UpcomingMovieData;
import com.arctouch.movies.data.UpcomingMovieResult;
import com.arctouch.movies.data.api.TheMovieDatabaseWebservice;
import com.arctouch.movies.data.api.generated.GenreList;
import com.arctouch.movies.data.db.MovieDatabase;
import com.arctouch.movies.data.db.MovieLocalCache;
import com.arctouch.movies.util.AppThreadPool;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The repository handles data from all the sources (local database, webservice, etc)
 */
public class MovieRepository {
    private static final String LOG_TAG = MovieRepository.class.getSimpleName();

    private static final String URL_BASE = "https://api.themoviedb.org/";
    private static final String API_KEY = "1f54bd990f1cdfb230adb312546d765d";
    private static final int DATABASE_PAGE_SIZE = 20;

    private String mLanguageTag;
    private MovieLocalCache mCache;
    private TheMovieDatabaseWebservice mService;

    public MovieRepository(Application application) {
        // Get the language tag to be used on queries
        mLanguageTag = Locale.getDefault().toLanguageTag();

        // Create the local cache database
        mCache = new MovieLocalCache(MovieDatabase.getInstance(application).movieDao());

        // Create the webservice
        mService = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TheMovieDatabaseWebservice.class);

        // Finally, let's populate our movie genres to be used.
        populateMovieGenreCache();
    }

    public UpcomingMovieResult getUpcomingMovies() {
        Log.d(LOG_TAG, "Get upcoming movies");

        DataSource.Factory<Integer, UpcomingMovieData> dataSourceFactory = mCache.getUpcomingMovies();

        // Callback to get data as demanded
        UpcomingMoviesBoundaryCallback boundaryCallback = new UpcomingMoviesBoundaryCallback(mCache, mService, mLanguageTag, API_KEY);

        // Create our paged list
        LiveData<PagedList<UpcomingMovieData>> data = new LivePagedListBuilder<>(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build();

        return new UpcomingMovieResult(data, boundaryCallback.getNetworkErrors());
    }

    /**
     * Only gets the upcoming movie data from the cache
     */
    public LiveData<UpcomingMovieData> getUpcomingMovieById(final int id) {
        Log.d(LOG_TAG, "Get upcoming movie by id " + id);
        return mCache.getUpcomingMovieById(id);
    }

    public String getMovieGenre(int id) {
        Log.d(LOG_TAG, "Get movie genre id " + id + " - " + mMovieGenres.get(id));
        return mMovieGenres.get(id);
    }

    // Our private movie genre in-memory cache, to keep things simple.
    private static HashMap<Integer, String> mMovieGenres = new HashMap<>();

    /**
     * Populate our in-memory cache with data from the local database and also updated data from the webservice.
     */
    private void populateMovieGenreCache() {
        // First off, if we already have the genres, bail.
        if (!mMovieGenres.isEmpty()) return;

        // Load all the genres from our local database for a speedy initialization.
        Log.d(LOG_TAG, "Load Movie Genres from DB");

        AppThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                for (GenreData genre : mCache.getMovieGenres()) {
                    Log.d(LOG_TAG, " -> Load genre " + genre.getName() + " to memory cache from db");
                    mMovieGenres.put(genre.getId(), genre.getName());
                }
            }
        });

        // Then, we go online and download the genres from the webservice
        mService.getMovieGenreList(API_KEY, mLanguageTag).enqueue(new Callback<GenreList>() {
            @Override
            public void onResponse(Call<GenreList> call, Response<GenreList> response) {
                Log.d(LOG_TAG, "Downloading Movie Genres - Response: " + response.toString());

                if (response.isSuccessful()) {
                    GenreList list = response.body();
                    if (list != null && list.getGenres() != null) {
                        Log.d(LOG_TAG, "Downloading Movie Genres - Inserting " + list.getGenres().size() + " movie genres.");

                        // Save the list to our local database
                        mCache.insertMovieGenres(list.getGenres());

                        // And put it on our in-memory cache for use
                        for (GenreData genre : list.getGenres()) {
                            mMovieGenres.put(genre.getId(), genre.getName());
                        }
                    } else {
                        Log.d(LOG_TAG, "Downloading Movie Genres - Response successful but no results found.");
                    }
                } else {
                    Log.d(LOG_TAG, "Downloading Movie Genres - Response unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<GenreList> call, Throwable t) {
                Log.d(LOG_TAG, "Downloading Movie Genres - Response unsuccessful");
            }
        });

    }


    // TODO: Read from the configuration API. Follow the directions from the API online doc, including download different sizes of images.
    private static final String URL_IMG_BASE = "https://image.tmdb.org/t/p/w780";

    /**
     * Loads a image from a path.
     *
     * @return A Picasso request
     */
    public RequestCreator getImage(String path) {
        Log.d(LOG_TAG, "Load image: " + path);

        return Picasso.get().load(Uri.parse(URL_IMG_BASE + path));
    }

}