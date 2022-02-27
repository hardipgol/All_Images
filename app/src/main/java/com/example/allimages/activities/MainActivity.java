package com.example.allimages.activities;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allimages.R;
import com.example.allimages.adapter.GalleryAdapter;
import com.example.allimages.adapter.pictureFolderAdapter;
import com.example.allimages.database.models.MediaModel;
import com.example.allimages.database.models.imageFolder;
import com.example.allimages.databinding.ActivityMainBinding;
import com.example.allimages.interfaces.itemClickListener;
import com.example.allimages.utils.FileUtil;
import com.example.allimages.viewModel.MediaViewModel;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MediaViewModel mediaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        requirePermission();
        mediaViewModel = new ViewModelProvider(this).get(MediaViewModel.class);

        binding.simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                } else {
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                }
            }
        });

        binding.switchGroupBy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ArrayList<imageFolder> folds = FileUtil.getPicturePaths(MainActivity.this);
                    RecyclerView.Adapter folderAdapter = new pictureFolderAdapter(folds, MainActivity.this, new itemClickListener() {
                        @Override
                        public void onPicClicked(String pictureFolderPath, String folderName) {
                            List<MediaModel> mFiles = FileUtil.findMediaFiles(getApplicationContext(), pictureFolderPath);
                            binding.recyclerView.setAdapter(new GalleryAdapter(MainActivity.this, mFiles, mediaViewModel));
                        }
                    });
                    binding.recyclerView.setAdapter(folderAdapter);
                } else {
                    List<MediaModel> mFiles = FileUtil.findMediaFiles(getApplicationContext(), ""); // media file or
                    binding.recyclerView.setAdapter(new GalleryAdapter(MainActivity.this, mFiles, mediaViewModel));
                }
            }
        });


        binding.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavMediaActivity.class);
                startActivity(intent);
            }
        });

        if (binding.simpleSwitch.isChecked()) {
            binding.recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        } else {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        }
        binding.recyclerView.setHasFixedSize(true);

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

    private void requirePermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                if (binding.switchGroupBy.isChecked()) {
                    ArrayList<imageFolder> folds = FileUtil.getPicturePaths(MainActivity.this);
                    RecyclerView.Adapter folderAdapter = new pictureFolderAdapter(folds, MainActivity.this, new itemClickListener() {
                        @Override
                        public void onPicClicked(String pictureFolderPath, String folderName) {
                            List<MediaModel> mFiles = FileUtil.findMediaFiles(getApplicationContext(), pictureFolderPath);
                            binding.recyclerView.setAdapter(new GalleryAdapter(MainActivity.this, mFiles, mediaViewModel));
                        }
                    });
                    binding.recyclerView.setAdapter(folderAdapter);
                } else {
                    List<MediaModel> mFiles = FileUtil.findMediaFiles(getApplicationContext(), ""); // media file or
                    //List<String> mFiles = FileUtil.findImageFileInDirectory(DIRECTORY, new String[]{"png", "jpg"}); // device file
                    binding.recyclerView.setAdapter(new GalleryAdapter(MainActivity.this, mFiles, mediaViewModel));
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

        try {
            getLocation(23.002272, 72.502243);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLocation(double latitude, double longitude) throws IOException {
        android.location.Geocoder geocoder = new android.location.Geocoder(MainActivity.this);
        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
        String street_number, route, locality, area1, area2, area3, postal_code, country;
        if (addressList != null && addressList.size() > 0) {
            String address = addressList.get(0).getAddressLine(addressList.get(0).getMaxAddressLineIndex());
            street_number = addressList.get(0).getFeatureName();
            route = addressList.get(0).getThoroughfare();
            locality = addressList.get(0).getLocality();
            area1 = addressList.get(0).getAdminArea();
            area2 = addressList.get(0).getSubAdminArea();
            postal_code = addressList.get(0).getPostalCode();
            country = addressList.get(0).getCountryName();

            Log.e("Location", "" + address);


        }
    }
}