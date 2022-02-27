package com.example.allimages.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "media_table")
public class MediaModel {
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    private int id;

    @ColumnInfo(name = "imagePath")
    String imagePath;

    @ColumnInfo(name = "createdAt")
    String createdAt;

    public int getImgFav() {
        return imgFav;
    }

    public void setImgFav(int imgFav) {
        this.imgFav = imgFav;
    }

    private int imgFav=0;

}
