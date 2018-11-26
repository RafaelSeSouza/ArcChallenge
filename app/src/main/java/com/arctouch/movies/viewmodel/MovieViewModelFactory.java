package com.arctouch.movies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider.Factory;
import android.support.annotation.NonNull;

/**
 * Creates out Model Views, as they need an application to the injected.
 */
public class MovieViewModelFactory implements Factory {

    private Application mApplication;

    public MovieViewModelFactory(Application application) {
        mApplication = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieViewModel(mApplication);
    }

}
