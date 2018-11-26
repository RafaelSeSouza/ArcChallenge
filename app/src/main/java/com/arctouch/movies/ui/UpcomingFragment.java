package com.arctouch.movies.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arctouch.movies.R;
import com.arctouch.movies.data.UpcomingMovieData;
import com.arctouch.movies.viewmodel.MovieViewModel;
import com.arctouch.movies.viewmodel.MovieViewModelFactory;

/**
 * Fragment that lists the upcoming movies
 */
public class UpcomingFragment extends Fragment {

    private static final String LOG_TAG = UpcomingFragment.class.getSimpleName();

    private MovieViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    public static UpcomingFragment newInstance() {
        return new UpcomingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        mViewModel = ViewModelProviders.of(getActivity(), new MovieViewModelFactory(getActivity().getApplication())).get(MovieViewModel.class);

        setupRecyclerView(view);

        loadUpcomingMovies();

        return view;
    }

    private void setupRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.upcoming_recycler);

        // Add some flair
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // Set up a linear layout manager
        mLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Find out if the activity is a listener, if it is, let's keep an eye on it
        MovieViewHolder.OnMovieViewHolderListener listener = null;
        if (getActivity() instanceof MovieViewHolder.OnMovieViewHolderListener) {
            listener = (MovieViewHolder.OnMovieViewHolderListener) getActivity();
        }

        // And set up the adapter
        mMovieAdapter = new MovieAdapter(mViewModel, listener);
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    private void loadUpcomingMovies() {
        mViewModel.getUpcomingMovies();

        mViewModel.upcomingMovies.observe(this, new Observer<PagedList<UpcomingMovieData>>() {
            @Override
            public void onChanged(@Nullable PagedList<UpcomingMovieData> upcomingMoviePagedList) {
                Log.d(LOG_TAG, "List count:" + upcomingMoviePagedList.size());
                mMovieAdapter.submitList(upcomingMoviePagedList);
            }
        });

        mViewModel.networkErrors.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d(LOG_TAG, "Error: " + s);
                Toast.makeText(getView().getContext(), getString(R.string.network_error), Toast.LENGTH_LONG).show();
            }
        });
    }

}
