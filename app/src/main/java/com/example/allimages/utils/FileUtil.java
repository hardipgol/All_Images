package com.example.allimages.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.allimages.activities.FavMediaActivity;
import com.example.allimages.database.models.MediaModel;
import com.example.allimages.database.models.imageFolder;
import com.example.allimages.viewModel.MediaViewModel;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class FileUtil {

    public static List<MediaModel> findMediaFiles(Context context, String folderPath) {
        List<MediaModel> fileList = new ArrayList<>();

        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE};
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor
        Cursor cursor;
        if (folderPath.equals("")) {
            cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                    null, orderBy);
        } else {
            cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + folderPath + "%"}, orderBy);
        }
        if (cursor != null) {
            //Total number of images
            int count = cursor.getCount();

            //Create an array to store path to all the images
            String[] arrPath = new String[count];
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int date = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                double latitude = cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
                double longitude = cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
                Log.e("size==>>", "" + latitude + " " + longitude);

                arrPath[i] = cursor.getString(dataColumnIndex);

                MediaModel model = new MediaModel();
                model.setImagePath(cursor.getString(dataColumnIndex));
                model.setCreatedAt(getStandardDate((new Date(cursor.getLong(date) * 1000l)), false));
                fileList.add(model);
            }
            cursor.close();
        }
        return fileList;
    }

    public static String getStandardDate(Date date, boolean inNewLine) {
        if (inNewLine) {
            return new SimpleDateFormat("dd-MMM-yyyy\nhh:mm a", Locale.getDefault()).format(date);
        } else {
            return new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault()).format(date);
        }
    }

    public static ArrayList<imageFolder> getPicturePaths(Context context) {
        ArrayList<imageFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();

        final String orderBy = MediaStore.Images.Media._ID;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media._ID};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, orderBy);
        try {
            if (cursor != null) {

                while (cursor.moveToNext()) {
                    imageFolder folds = new imageFolder();
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                    if (folder != null) {
                        String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                        folderpaths = folderpaths + folder + "/";
                        if (!picPaths.contains(folderpaths)) {
                            picPaths.add(folderpaths);

                            folds.setPath(folderpaths);
                            folds.setFolderName(folder);
                            folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                            folds.addpics();
                            picFolders.add(folds);
                        } else {
                            for (int i = 0; i < picFolders.size(); i++) {
                                if (picFolders.get(i).getPath().equals(folderpaths)) {
                                    picFolders.get(i).setFirstPic(datapath);
                                    picFolders.get(i).addpics();
                                }
                            }
                        }
                    }
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return picFolders;
    }

    public static String getFileSize(String imagePath) {
        File file = new File(imagePath);
        long fileSizeInBytes = file.length();
        return getStringSizeLengthFile(fileSizeInBytes);
    }

    public static String getStringSizeLengthFile(long size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMb = sizeKb * sizeKb;
        float sizeGb = sizeMb * sizeKb;
        float sizeTerra = sizeGb * sizeKb;

        if (size < sizeMb)
            return df.format(size / sizeKb) + " Kb";
        else if (size < sizeGb)
            return df.format(size / sizeMb) + " Mb";
        else if (size < sizeTerra)
            return df.format(size / sizeGb) + " Gb";

        return "";
    }
}