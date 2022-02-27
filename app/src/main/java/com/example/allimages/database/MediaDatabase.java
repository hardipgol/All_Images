package com.example.allimages.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.allimages.database.Daos.MediaDao;
import com.example.allimages.database.models.MediaModel;

@Database(entities = {MediaModel.class}, version = 1, exportSchema = false)
public abstract class MediaDatabase extends RoomDatabase {

    private static MediaDatabase INSTANCE;

    public abstract MediaDao mediaDao();

    public static MediaDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (MediaDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(
                            context, MediaDatabase.class, "MEDAI_DATABASE")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
