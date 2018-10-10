# Banner
  * 自定义轮播图Banner，自定义View继承自RelativeLayout
  * 重写ViewPager.PageTransformer transformPage(View page, float position)方法
    实现各种过渡动画效果

  * 效果图：
  <div align="left">
   <img src="https://github.com/YouriZhang/imagefolder/blob/master/girls.gif" width="320" height="600">
  </div> 

  * 自定义属性：
  ```
   <declare-styleable name="BannerLayoutStyle">
           # 选中时指示器颜色
          <attr name="selectedIndicatorColor" format="color|reference" />
           # 未选中时指示器颜色
          <attr name="unSelectedIndicatorColor" format="color|reference" />
           # 选中时指示器drawable resId
          <attr name="selectedIndicator" format="reference"/>
           # 未选中时指示器drawable resId
          <attr name="unSelectedIndicator" format="reference"/>
           # 指示器高度
          <attr name="selectedIndicatorHeight" format="dimension|reference" />
           # 指示器宽度
          <attr name="selectedIndicatorWidth" format="dimension|reference" />
           # 未选中选中时指示器高度
          <attr name="unSelectedIndicatorHeight" format="dimension|reference" />
           # 未选中选中时指示器宽度
          <attr name="unSelectedIndicatorWidth" format="dimension|reference" />
           # 指示器间距
          <attr name="indicatorSpace" format="dimension|reference" />

          <attr name="indicatorMargin" format="dimension|reference" />
           # 自动轮播时间间隔
          <attr name="autoPlayDuration" format="integer|reference" />
           # viewPage scroll duration
          <attr name="scrollDuration" format="integer|reference" />
           # 是否自动轮播，默认true
          <attr name="isAutoPlay" format="boolean|reference"/>
           #  图片宽高比
          <attr name="layoutHeightPercentReferW" format="float" />
      </declare-styleable>
  ```

  * 示例代码：
  ```
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
  ```

