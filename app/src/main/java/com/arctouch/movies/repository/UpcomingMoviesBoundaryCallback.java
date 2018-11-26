package com.arctouch.movies.repository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arctouch.movies.data.UpcomingMovieData;
import com.arctouch.movies.data.api.TheMovieDatabaseWebservice;
import com.arctouch.movies.data.api.generated.UpcomingMovieList;
import com.arctouch.movies.data.db.MovieLocalCache;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Gets called when the user reaches the end of the list so that we can fetch more movies for him/her.
 **/
public class UpcomingMoviesBoundaryCallback extends PagedList.BoundaryCallback<UpcomingMovieData> {

    private static final String LOG_TAG = UpcomingMoviesBoundaryCallback.class.getSimpleName();

    public UpcomingMoviesBoundaryCallback(MovieLocalCache cache, TheMovieDatabaseWebservice service, String languageTag, String apiKey) {
        super();
        this.mCache = cache;
        this.mService = service;
        this.mApiKey = apiKey;
        this.mLanguageTag = languageTag;
    }

    private MovieLocalCache mCache;
    private TheMovieDatabaseWebservice mService;
    private String mLanguageTag;
    private String mApiKey;

    private int mLastPage = 1;
    private boolean mUpdating = false;
    private MutableLiveData<String> mNetworkErrors = new MutableLiveData<>();

    public MutableLiveData<String> getNetworkErrors() {
        return mNetworkErrors;
    }

    @Override
    public void onZeroItemsLoaded() {
        update();
    }

    @Override
    public void onItemAtEndLoaded(@NonNull UpcomingMovieData itemAtEnd) {
        update();
    }

    private void update() {
        if (mUpdating) return;

        Log.d(LOG_TAG, "Updating");

        mUpdating = true;

        // Ask the webservice for a nice page with the upcoming movies
        mService.getUpcoming(mApiKey, mLanguageTag, mLastPage).enqueue(new Callback<UpcomingMovieList>() {

            @Override
            public void onResponse(Call<UpcomingMovieList> call, Response<UpcomingMovieList> response) {
                Log.d(LOG_TAG, "Response: " + response.toString());

                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "Response successful");

                    UpcomingMovieList list = response.body();
                    if (list != null) {
                        Log.d(LOG_TAG, "Total Results: " + list.getTotalResults() + " - " + "Page " + list.getPage() + "/" + list.getTotalPages()
                                + " - This list total: " + list.getUpcomingMovieData().size());

                        // Save the results, and add the page for the next request
                        mCache.insertUpcomingMovies(list.getUpcomingMovieData());
                        ++mLastPage;
                    }

                    mUpdating = false;
                } else {
                    Log.d(LOG_TAG, "Response unsuccessful");

                    if (response.errorBody() != null) {
                        try {
                            mNetworkErrors.postValue(response.errorBody().string());
                        } catch (IOException e) {
                            mNetworkErrors.postValue("Error parsing error: " + e.getMessage());
                        }
                    } else {
                        mNetworkErrors.postValue("Error unknown.");
                    }
                    mUpdating = false;
                }
            }

            @Override
            public void onFailure(Call<UpcomingMovieList> call, Throwable error) {
                Log.e(LOG_TAG, "Error getting data");
                mNetworkErrors.postValue(error.getMessage());
                mUpdating = false;
            }

        });

    }
}
