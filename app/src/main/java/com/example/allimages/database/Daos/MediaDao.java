package com.example.allimages.database.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.allimages.database.models.MediaModel;

import java.util.List;

@Dao
public interface MediaDao {

    @Insert
    void insert(MediaModel media);

    @Query("SELECT * FROM media_table")
    LiveData<List<MediaModel>> getAllMedia();

    @Query("SELECT COUNT(*) FROM media_table WHERE imagePath = :image_path ")
    LiveData<Integer> checkMediaExists(String image_path);

    @Delete
    void delete(MediaModel media);

}
