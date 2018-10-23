package com.hipac.banners;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.hipac.banner.Banner;
import com.hipac.banner.DefaultBannerLoader;
import com.hipac.banner.transformer.AccordionTransformer;
import com.hipac.banner.transformer.BackgroundToForegroundTransformer;
import com.hipac.banner.transformer.CubeInTransformer;
import com.hipac.banner.transformer.CubeOutTransformer;
import com.hipac.banner.transformer.DefaultTransformer;
import com.hipac.banner.transformer.ForegroundToBackgroundTransformer;

import java.util.Arrays;
import java.util.List;

public class BannerActivity extends AppCompatActivity {
    private List<String> imgurls = Arrays.asList("https://ws1.sinaimg.cn/large/0065oQSqly1fw0vdlg6xcj30j60mzdk7.jpg",
                                               "https://ws1.sinaimg.cn/large/0065oQSqly1fuo54a6p0uj30sg0zdqnf.jpg",
                                               "https://ws1.sinaimg.cn/large/0065oQSqly1fuh5fsvlqcj30sg10onjk.jpg",
                                               "https://ww1.sinaimg.cn/large/0065oQSqly1fu7xueh1gbj30hs0uwtgb.jpg",
                                               "https://ww1.sinaimg.cn/large/0065oQSqgy1fu39hosiwoj30j60qyq96.jpg",
                                               "https://ww1.sinaimg.cn/large/0065oQSqly1ftzsj15hgvj30sg15hkbw.jpg",
                                               "https://ww1.sinaimg.cn/large/0065oQSqly1ftf1snjrjuj30se10r1kx.jpg",
                                               "http://ww1.sinaimg.cn/large/0065oQSqly1fszxi9lmmzj30f00jdadv.jpg",
                                               "http://ww1.sinaimg.cn/large/0065oQSqly1fswhaqvnobj30sg14hka0.jpg");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        Banner banner = findViewById(R.id.banner);
        TextView tvDesc = findViewById(R.id.tv_desc);
        tvDesc.setText(getString(R.string.gank_io_introduction));
        tvDesc.setMovementMethod(ScrollingMovementMethod.getInstance());
        String transform = getIntent().getStringExtra("transform");
        //设置不同的transform
        switch (transform){
            case "defaultT":
                banner.setBannerAnimation(DefaultTransformer.class);
                break;
            case "Accordion":
                banner.setBannerAnimation(AccordionTransformer.class);
                break;
            case "CubeIn":
                banner.setBannerAnimation(CubeInTransformer.class);
                break;
            case "CubeOut":
                banner.setBannerAnimation(CubeOutTransformer.class);
                break;
            case "ForegroundToBg":
                banner.setBannerAnimation(ForegroundToBackgroundTransformer.class);
                break;
            case "BgToForeground":
                banner.setBannerAnimation(BackgroundToForegroundTransformer.class);
                break;
        }
        //设置图片加载器，默认用glide实现
        banner.setBannerLoader(new DefaultBannerLoader());
        //设置数据，最后调用
        banner.setViewUrls(imgurls);

    }
}
