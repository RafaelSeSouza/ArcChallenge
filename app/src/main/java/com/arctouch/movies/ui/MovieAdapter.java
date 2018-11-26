package com.arctouch.movies.ui;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.arctouch.movies.data.UpcomingMovieData;
import com.arctouch.movies.viewmodel.MovieViewModel;

/**
 * Manages views for each of the items on the recycler view
 */
public class MovieAdapter extends PagedListAdapter<UpcomingMovieData, RecyclerView.ViewHolder> {

    private MovieViewModel mViewModel;
    private MovieViewHolder.OnMovieViewHolderListener mCallback;

    public MovieAdapter(MovieViewModel viewModel, MovieViewHolder.OnMovieViewHolderListener callback) {
        super(DIFF_CALLBACK);

        this.mViewModel = viewModel;
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return MovieViewHolder.create(viewGroup, mViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        UpcomingMovieData item = getItem(i);
        if (item != null) {
            ((MovieViewHolder) viewHolder).bind(item, mCallback);
        }
    }

    /**
     * This callback is only used when we need to know if it's the same movie
     */
    public static final DiffUtil.ItemCallback<UpcomingMovieData> DIFF_CALLBACK = new DiffUtil.ItemCallback<UpcomingMovieData>() {
        @Override
        public boolean areItemsTheSame(@NonNull UpcomingMovieData oldMovie, @NonNull UpcomingMovieData newMovie) {
            return oldMovie.getId() == newMovie.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull UpcomingMovieData oldMovie, @NonNull UpcomingMovieData newMovie) {
            return oldMovie.equals(newMovie);
        }
    };

}
