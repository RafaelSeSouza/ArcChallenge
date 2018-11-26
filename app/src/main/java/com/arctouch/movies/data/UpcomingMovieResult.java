package com.arctouch.movies.data;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

/**
 * Holds the returned data of upcoming movies, and also the errors it may have encountered
 */
public class UpcomingMovieResult {

    private LiveData<PagedList<UpcomingMovieData>> mData;
    private LiveData<String> mNetworkErrors;

    public UpcomingMovieResult(LiveData<PagedList<UpcomingMovieData>> data, LiveData<String> networkErrors) {
        this.mData = data;
        this.mNetworkErrors = networkErrors;
    }

    public LiveData<PagedList<UpcomingMovieData>> getData() {
        return mData;
    }

    public LiveData<String> getNetworkErrors() {
        return mNetworkErrors;
    }
}
