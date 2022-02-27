package com.example.allimages.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.allimages.database.Daos.MediaDao;
import com.example.allimages.database.MediaDatabase;
import com.example.allimages.database.models.MediaModel;

import java.util.List;

public class MediaRepository {
    private MediaDao mediaDao;
    private LiveData<List<MediaModel>> allMedia;

    public MediaRepository(Application application) {
        MediaDatabase database = MediaDatabase.getDatabase(application);
        mediaDao = database.mediaDao();
        allMedia = mediaDao.getAllMedia();
    }

    public void insert(MediaModel media) {
        new InsertMediaAsyncTask(mediaDao).execute(media);
    }

    public void delete(MediaModel media) {
        new DeleteMediaAsyncTask(mediaDao).execute(media);
    }

    public LiveData<Integer>  checkMediaExists(String image_path){
        return mediaDao.checkMediaExists(image_path);
    }

    public LiveData<List<MediaModel>> getAllMedia() {
        return allMedia;
    }

    private static class InsertMediaAsyncTask extends AsyncTask<MediaModel, Void, Void> {
        private MediaDao noteDao;

        private InsertMediaAsyncTask(MediaDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(MediaModel... media) {
            noteDao.insert(media[0]);
            return null;
        }
    }

    private static class DeleteMediaAsyncTask extends AsyncTask<MediaModel, Void, Void> {
        private MediaDao mediaDao;

        private DeleteMediaAsyncTask(MediaDao mediaDao) {
            this.mediaDao = mediaDao;
        }

        @Override
        protected Void doInBackground(MediaModel... media) {
            mediaDao.delete(media[0]);
            return null;
        }
    }

    /*private static class checkMediaAsyncTask extends AsyncTask<String, Void, List<MediaModel>> {
        private MediaDao mediaDao;

        private checkMediaAsyncTask(MediaDao mediaDao) {
            this.mediaDao = mediaDao;
        }

        @Override
        protected List<MediaModel> doInBackground(String... media) {

            return mediaDao.checkMediaExists(media[0]);
        }

        @Override
        protected void onPostExecute(List<MediaModel> mediaModels) {
            super.onPostExecute(mediaModels);
        }
    }*/

}
