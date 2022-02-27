package com.example.allimages.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.media.ExifInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.allimages.R;
import com.example.allimages.activities.MainActivity;
import com.example.allimages.database.models.MediaModel;
import com.example.allimages.databinding.GalleryListRowBinding;
import com.example.allimages.interfaces.ISize;
import com.example.allimages.utils.FileUtil;
import com.example.allimages.utils.SizeFromImage;
import com.example.allimages.viewModel.MediaViewModel;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.CustomViewHolder> {

    private final List<MediaModel> mFileList;
    private final Activity mActivity;
    private GalleryListRowBinding binding;
    private MediaViewModel mediaViewModel;

    public GalleryAdapter(Activity activity, List<MediaModel> fileList, MediaViewModel mediaViewModel) {
        mActivity = activity;
        mFileList = fileList;
        this.mediaViewModel = mediaViewModel;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.gallery_list_row, parent, false);
        return new CustomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (mFileList.get(position).getImgFav() == 0) {
            holder.binding.imgFav.setImageResource(R.drawable.ic_unfav);
        } else {
            holder.binding.imgFav.setImageResource(R.drawable.ic_fav);
        }

        Glide.with(mActivity)
                .load(mFileList.get(position).getImagePath())
                .override(200, 200)
                .centerCrop()
                .into(holder.binding.imageResource);

        holder.binding.txtDate.setText("" + mFileList.get(position).getCreatedAt());

        ISize size = new SizeFromImage(mFileList.get(position).getImagePath());
        holder.binding.txtSize.setText("" + FileUtil.getFileSize(mFileList.get(position).getImagePath()) + " " + size.width() + " x " + size.height());

        final int itemPosition = holder.getAdapterPosition();
        holder.binding.imageResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity instanceof MainActivity) {
                    if (mFileList.get(itemPosition).getImgFav() == 0) {
                        mFileList.get(itemPosition).setImgFav(1);
                    } else {
                        mFileList.get(itemPosition).setImgFav(0);
                    }
                    notifyItemChanged(itemPosition);
                    mediaViewModel.insert(mFileList.get(itemPosition));
                } else {
                    mediaViewModel.delete(mFileList.get(itemPosition));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        GalleryListRowBinding binding;

        CustomViewHolder(GalleryListRowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
