package com.example.allimages.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.allimages.database.models.MediaModel;
import com.example.allimages.repositories.MediaRepository;

import java.util.List;

public class MediaViewModel extends AndroidViewModel {
    private MediaRepository repository;
    private LiveData<List<MediaModel>> allMedia;
    private MediaModel checkMediaExists;

    public MediaViewModel(@NonNull Application application) {
        super(application);
        repository = new MediaRepository(application);
        allMedia = repository.getAllMedia();

    }

    public void insert(MediaModel media) {
        repository.insert(media);
    }
    public void delete(MediaModel media) {
        repository.delete(media);
    }

    public LiveData<List<MediaModel>> getAllMedia() {
        return allMedia;
    }

    public LiveData<Integer> loadFromMedia(String image_path) {

        return repository.checkMediaExists(image_path);
    }

}
