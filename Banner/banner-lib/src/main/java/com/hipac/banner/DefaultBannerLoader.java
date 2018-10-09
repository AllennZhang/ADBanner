package com.hipac.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by youri on 2017/9/22.
 */

public class DefaultBannerLoader implements Banner.BannerLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView, int position) {
       initData(imageView,position);
        GlideApp.with(context)
                .load(path)
                .centerCrop()
                .dontAnimate()
                .error(R.drawable.gallary)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .priority(Priority.HIGH)
                .into(imageView);
    }

    public void initData(View view, int pos){

    }
}
