package com.example.allimages.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.allimages.R;
import com.example.allimages.adapter.GalleryAdapter;
import com.example.allimages.database.models.MediaModel;
import com.example.allimages.databinding.ActivityFavMediaBinding;
import com.example.allimages.viewModel.MediaViewModel;

import java.util.List;

public class FavMediaActivity extends AppCompatActivity {

    private ActivityFavMediaBinding binding;
    private MediaViewModel mediaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favorite List");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fav_media);
        mediaViewModel = new ViewModelProvider(this).get(MediaViewModel.class);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setHasFixedSize(true);

        mediaViewModel.getAllMedia().observe(this, new Observer<List<MediaModel>>() {
            @Override
            public void onChanged(List<MediaModel> mediaModels) {
                binding.recyclerView.setAdapter(new GalleryAdapter(FavMediaActivity.this, mediaModels, mediaViewModel));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}