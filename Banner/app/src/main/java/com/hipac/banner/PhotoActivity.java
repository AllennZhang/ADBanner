package com.hipac.banner;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class PhotoActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView ivDetail = findViewById(R.id.iv_detail);
        GlideApp.with(this).load("http://img.hicdn.cn/item/201808/08311025286091.gif").downsample(DownsampleStrategy.DEFAULT).placeholder(R.drawable.gallary).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.e("PhotoActivity","onLoadFailed:"+e.toString());
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.e("PhotoActivity","onResourceReady:");
                return false;
            }
        }).into(ivDetail);
//        GlideApp.with(this)
//                .asBitmap()
//                .load("http://img.hicdn.cn/item/201808/08311025286091.gif")
//                .downsample(DownsampleStrategy.DEFAULT)
//                .placeholder(R.drawable.gallary)
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                .skipMemoryCache(false)
//                .priority(Priority.HIGH)
//                .listener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        Log.e("PhotoActivity","onLoadFailed:"+e.toString());
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        Log.e("PhotoActivity","onResourceReady:");
//                        return false;
//                    }
//                })
//                .into(ivDetail);
    }
}
