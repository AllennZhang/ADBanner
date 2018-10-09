package com.hipac.banner;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * 适用于Glide v3.x版本
 * 自定义Module 实现对Glide的全局管理，需要在AndroidManifest进行配置
 *   <meta-data
 *      android:name="com.xxx.SimpleGlideModule"
 *      android:value="GlideModule" />
 * Created by youri on 2017/5/13.
 */

/*public class SimpleGlideModule implements GlideModule {

    public static DiskCache cache;
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 在 Android 中有两个主要的方法对图片进行解码：ARGB8888 和 RGB565。前者为每个像素使用了 4 个字节，
        // 后者仅为每个像素使用了 2 个字节。ARGB8888 的优势是图像质量更高以及能存储一个 alpha 通道。
        // Picasso 使用 ARGB8888，Glide 默认使用低质量的 RGB565。
        // 对于 Glide 使用者来说：你使用 Glide module 方法去改变解码规则。
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

        //设置缓存目录
        //ExternalCacheDiskCacheFactory,变为SDCard/Android/data/应用包名/cache/glideCache
        //InternalCacheDiskCacheFactory,变为data/data/应用包名/cache/glideCache
        String dir ="glideCache";
        File file = new File(context.getCacheDir(),dir);
        if (!file.exists()){
            file.mkdirs();
        }
        cache = DiskLruCacheWrapper.get(file,DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                return cache;
            }
        });
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,dir,DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE));
        //设置memory和Bitmap池的大小,Glide会根据不通手机性能计算合适的大小
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

//        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
//        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(defaultMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));


    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }

}*/

/**
 * Glide v4 使用 注解处理器 (Annotation Processor) 来生成出一个 API，
 * 在 Application 模块中可使用该流式 API 一次性调用到 RequestBuilder，
 * RequestOptions 和集成库中所有的选项
 */
@GlideModule
public final class SimpleGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //自定义配置，default do noting
        super.applyOptions(context, builder);
    }
}
