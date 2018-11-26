package com.arctouch.movies.ui;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.movies.R;
import com.arctouch.movies.data.UpcomingMovieData;
import com.arctouch.movies.viewmodel.MovieViewModel;

/**
 * Creates and populates each view for the recycler view
 */
public class MovieViewHolder extends ViewHolder {

    private MovieViewModel mViewModel;
    private UpcomingMovieData mData;

    private TextView mTextTitle;
    private TextView mTextReleaseDate;
    private TextView mTextGenres;
    private ImageView mImageBackground;

    private OnMovieViewHolderListener mCallback;

    public static MovieViewHolder create(ViewGroup parent, MovieViewModel viewModel) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_view_holder, parent, false);
        return new MovieViewHolder(view, viewModel);
    }


    public MovieViewHolder(View view, MovieViewModel viewModel) {
        super(view);

        mTextTitle = view.findViewById(R.id.movie_holder_text_title);
        mTextReleaseDate = view.findViewById(R.id.movie_holder_text_release);
        mTextGenres = view.findViewById(R.id.movie_holder_text_genres);
        mImageBackground = view.findViewById(R.id.movie_holder_image_backdrop);
        mViewModel = viewModel;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onMovieSelected(mData.getId());
                }
            }
        });

    }

    public void bind(UpcomingMovieData data, OnMovieViewHolderListener listener) {
        Resources resources = mTextTitle.getResources();
        mData = data;
        mCallback = listener;

        if (mData == null) {
            mTextTitle.setText(resources.getString(R.string.loading));
            mTextReleaseDate.setVisibility(View.GONE);
            mTextGenres.setVisibility(View.GONE);
            mImageBackground.setImageResource(android.R.color.black);
        } else {
            mTextTitle.setText(mData.getTitle() != null ? mData.getTitle() : resources.getString(R.string.no_title));

            String genres = mViewModel.getGenresCommaSeparated(mData);
            mTextGenres.setText(resources.getString(R.string.genre_title, genres));
            mTextGenres.setVisibility(genres != null && !genres.isEmpty() ? View.VISIBLE : View.GONE);

            mTextReleaseDate.setText(resources.getString(R.string.release_date_title, mData.getReleaseDate()));
            mTextReleaseDate.setVisibility(mData.getReleaseDate() != null && !mData.getReleaseDate().isEmpty() ? View.VISIBLE : View.GONE);

            mViewModel.getBackdropImage(mData, mImageBackground);
        }

    }

    public interface OnMovieViewHolderListener {
        void onMovieSelected(int id);
    }

}
