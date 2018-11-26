package com.arctouch.movies.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.arctouch.movies.data.GenreData;
import com.arctouch.movies.data.UpcomingMovieData;

/**
 * The Movie Database stores all the movie data locally
 */
@Database(
        entities = {GenreData.class
                , UpcomingMovieData.class}
        , version = 1
        , exportSchema = false
)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String DATABASE_FILE = "MovieDB.db";
    private static MovieDatabase mInstance = null;

    static public synchronized MovieDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, DATABASE_FILE).build();
        }

        return mInstance;
    }

    public abstract MovieDao movieDao();

    protected MovieDatabase() {
    }

}
