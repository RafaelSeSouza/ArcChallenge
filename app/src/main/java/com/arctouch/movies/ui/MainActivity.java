package com.arctouch.movies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.arctouch.movies.R;

public class MainActivity extends AppCompatActivity implements MovieViewHolder.OnMovieViewHolderListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // The bottom navigation is here for future use, to add more options of navigation (like Top Rated and Search)
    private BottomNavigationView mBottomNav;

    private Fragment mFragUpcoming = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNav = findViewById(R.id.main_bottom_nav);
        mBottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        selectUpcoming();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_upcoming:
                    selectUpcoming();
                    return true;
            }
            return false;
        }

    };

    private void selectUpcoming() {
        if (mFragUpcoming == null) {
            mFragUpcoming = UpcomingFragment.newInstance();
        }

        loadFragment(mFragUpcoming);
    }

    private void loadFragment(Fragment fragment) {
        Log.d(LOG_TAG, "Loading fragment");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_placeholder, fragment)
                .commit();
    }

    /**
     * From the listener. When the user select a movie on MovieViewHolder, it calls this method to open the activity with the movie details
     */
    @Override
    public void onMovieSelected(int id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE_ID, id);
        startActivity(intent);
    }
}
