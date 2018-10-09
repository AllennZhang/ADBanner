package com.hipac.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.hipac.banner.transformer.DefaultTransformer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by youri on 2017/9/22.
 *  自定义轮播图
 *  2017.9.22修订内容：
 *  解决banner加载重影问题（删除Glide加载placeHolder），增加PageTransformer动画，
 *  2017.10.17修订内容：
 *  解决banner真实数据少于3条时因为模拟添加数据引起的mBannerData.get(position)越界问题，删除原有模拟添加数据代码
 */
public class Banner extends RelativeLayout {

    private ViewPager pager;
    //指示器容器
    private LinearLayout indicatorContainer;

    private int WHAT_AUTO_PLAY = 1000;   //轮播事件 msg

    private boolean isAutoPlay = true;   //自动轮播

    private int itemCount;              //轮播图数量

    private int selectedIndicatorColor = 0xffff0000;
    private int unSelectedIndicatorColor = 0x88888888;

    private int selectedIndicatorResId;
    private int unSelectedIndicatorResId;

    private int selectedIndicatorHeight = 6;
    private int selectedIndicatorWidth = 6;
    private int unSelectedIndicatorHeight = 6;
    private int unSelectedIndicatorWidth = 6;

    private int autoPlayDuration = 4000;     //自动轮播时间间隔
    private int scrollDuration = 900;        //

    private int indicatorSpace = 5;
    private int indicatorMargin = 10;

    private int currentPosition;

    private BannerLoader imageLoader;   //图片加载框架，可自行定制
    List<? extends ItemInfo> mBannerData;
    private Class<? extends ViewPager.PageTransformer> mPagerTransform = DefaultTransformer.class;//默认切换效果

//    private static MyHandler handler ;

    private int currentRealPosition;
    private float mHeightPercentReferW = 0;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoPlay();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    private OnBannerItemClickListener onBannerItemClickListener;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                if (pager != null && isAutoPlay) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                    handler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);
                }
            }
            return false;
        }
    });


    public Banner(Context context) {
        this(context, null);

    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);

    }

    private void init(AttributeSet attrs, int defStyle) {

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BannerLayoutStyle, defStyle, 0);
        selectedIndicatorColor = array.getColor(R.styleable.BannerLayoutStyle_selectedIndicatorColor, selectedIndicatorColor);
        unSelectedIndicatorColor = array.getColor(R.styleable.BannerLayoutStyle_unSelectedIndicatorColor, unSelectedIndicatorColor);

//        selectedIndicatorResId = array.getResourceId(R.styleable.BannerLayoutStyle_selectedIndicator, R.drawable.shape_white_pot);
//        unSelectedIndicatorResId = array.getResourceId(R.styleable.BannerLayoutStyle_unSelectedIndicator, R.drawable.shape_gray_pot);

        selectedIndicatorResId = array.getResourceId(R.styleable.BannerLayoutStyle_selectedIndicator, R.drawable.shape_rec_indicator_selected);
        unSelectedIndicatorResId = array.getResourceId(R.styleable.BannerLayoutStyle_unSelectedIndicator, R.drawable.shape_rec_indicator_unselected);

        selectedIndicatorHeight = (int) array.getDimension(R.styleable.BannerLayoutStyle_selectedIndicatorHeight, selectedIndicatorHeight);
        selectedIndicatorWidth = (int) array.getDimension(R.styleable.BannerLayoutStyle_selectedIndicatorWidth, selectedIndicatorWidth);
        unSelectedIndicatorHeight = (int) array.getDimension(R.styleable.BannerLayoutStyle_unSelectedIndicatorHeight, unSelectedIndicatorHeight);
        unSelectedIndicatorWidth = (int) array.getDimension(R.styleable.BannerLayoutStyle_unSelectedIndicatorWidth, unSelectedIndicatorWidth);

        indicatorSpace = (int) array.getDimension(R.styleable.BannerLayoutStyle_indicatorSpace, indicatorSpace);
        indicatorMargin = (int) array.getDimension(R.styleable.BannerLayoutStyle_indicatorMargin, indicatorMargin);
        autoPlayDuration = array.getInt(R.styleable.BannerLayoutStyle_autoPlayDuration, autoPlayDuration);
        scrollDuration = array.getInt(R.styleable.BannerLayoutStyle_scrollDuration, scrollDuration);
        isAutoPlay = array.getBoolean(R.styleable.BannerLayoutStyle_isAutoPlay, isAutoPlay);
        mHeightPercentReferW = array.getFloat(R.styleable.BannerLayoutStyle_layoutHeightPercentReferW, 0);
        array.recycle();

//        //绘制未选中状态图形
//        LayerDrawable unSelectedLayerDrawable;
//        LayerDrawable selectedLayerDrawable;
//        GradientDrawable unSelectedGradientDrawable;
//        unSelectedGradientDrawable = new GradientDrawable();
//
//        //绘制选中状态图形
//        GradientDrawable selectedGradientDrawable;
//        selectedGradientDrawable = new GradientDrawable();
//
//        unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
//        selectedGradientDrawable.setShape(GradientDrawable.OVAL);
//
//        unSelectedGradientDrawable.setColor(unSelectedIndicatorColor);
//        unSelectedGradientDrawable.setSize(unSelectedIndicatorWidth, unSelectedIndicatorHeight);
//        unSelectedLayerDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
//
//        selectedGradientDrawable.setColor(selectedIndicatorColor);
//        selectedGradientDrawable.setSize(selectedIndicatorWidth, selectedIndicatorHeight);
//        selectedLayerDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});

    }

    /**
     * 设置选中与未选中的指示器资源 在setViewUrls（）之前调用
     *
     * @param unselectedIndicatorResId
     * @param selectedIndicatorResId
     */
    public Banner setBannerIndicator(@DrawableRes int unselectedIndicatorResId, @DrawableRes int selectedIndicatorResId) {
        this.unSelectedIndicatorResId = unselectedIndicatorResId;
        this.selectedIndicatorResId = selectedIndicatorResId;
        return this;
    }

    public Banner setBannerIndicatorColor(@ColorRes int unselectedIndicatorColor, @ColorRes int selectedIndicatorColor) {
        this.unSelectedIndicatorColor = unselectedIndicatorColor;
        this.selectedIndicatorColor = selectedIndicatorColor;
        return this;
    }

    public Banner setBannerIndicatorPadding(int padding) {
        this.indicatorSpace = padding;
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mHeightPercentReferW!=0){
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(widthMeasureSpec) * mHeightPercentReferW), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public Banner setData(List<? extends ItemInfo> data) {
        this.mBannerData = data;
        ArrayList<String> urls = new ArrayList<>();
        for (ItemInfo bannerItem : mBannerData) {
            urls.add(bannerItem.getUrl());
        }
        setViewUrls(urls);
        return this;
    }

    //添加网络图片路径
    public Banner setViewUrls(List<String> urls) {
        if (urls == null || urls.size() <= 0) {
            return this;
        }
        List<String> imageUrls = new ArrayList<>();
        itemCount = urls.size();
        final int count = itemCount;
        Log.e("Banner","itemCount: "+count) ;

        if (count < 1) {//当item个数0
          return this;
        }
        imageUrls.addAll(urls);
        setViews(imageUrls);
        return this;
    }

    @NonNull
    private ImageView getImageView(OnClickListener clickListener) {
        ImageView imageView = new ImageView(getContext());
        ViewGroup.LayoutParams  layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setOnClickListener(clickListener);
//        imageView.setImageDrawable(null);
        return imageView;
    }


    public Banner setBannerLoader(BannerLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public ViewPager getPager() {
        if (pager != null) {
            return pager;
        }
        return null;
    }

    //添加任意View视图
    public void setViews(final List<String> imageUrls) {
        final int count = itemCount;

        removeAllViews();
        //初始化pager
        pager = new ViewPager(getContext());
        //pager.setOffscreenPageLimit(3);
        //添加viewpager到根布局
        addView(pager);
        setSliderTransformDuration(scrollDuration);
        //设置动画
        setPageTransformer(count>1);
        //初始化indicatorContainer
        indicatorContainer = new LinearLayout(getContext());
        indicatorContainer.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        //设置margin
        params.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin);
        //添加指示器容器布局到SliderLayout
        addView(indicatorContainer, params);

        indicatorContainer.removeAllViews();
        //初始化指示器，并添加到指示器容器布局
        if(count>1) {
            for (int i = 0; i < count; i++) {
                ImageView indicator = new ImageView(getContext());
                indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                indicator.setPadding(indicatorSpace, indicatorSpace, indicatorSpace, indicatorSpace);
                indicator.setImageResource(unSelectedIndicatorResId);
                // indicator.setImageDrawable(unSelectedDrawable);
                indicatorContainer.addView(indicator);
            }
        }
        LoopPagerAdapter pagerAdapter = new LoopPagerAdapter(imageUrls,itemCount==1?1: Integer.MAX_VALUE);
        pager.setAdapter(pagerAdapter);
        //设置当前item到Integer.MAX_VALUE中间的一个值，看起来像无论是往前滑还是往后滑都是ok的
        //如果不设置，用户往左边滑动的时候已经划不动了
        int targetItemPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % count;
        currentPosition = targetItemPosition;
        currentRealPosition = targetItemPosition%count;
        pager.setCurrentItem(targetItemPosition);
        switchIndicator(targetItemPosition % count);
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                currentRealPosition =  position%count;
                switchIndicator(position % count);
            }
        });
        isAutoPlay = imageUrls!=null&&imageUrls.size()>1;
        if (isAutoPlay) {
            startAutoPlay();
        }

    }

    public Banner setSliderTransformDuration(int duration) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(pager.getContext(), null, duration);
            mScroller.set(pager, scroller);
        } catch (Exception e) {

        }
        return this;
    }

    /**
     * 设置viewpager动画
     * @param transformer
     * @return
     */
    public Banner setBannerAnimation(Class<? extends ViewPager.PageTransformer> transformer) {
        mPagerTransform = transformer;
        return this;
    }

    private void setPageTransformer(boolean reverseDrawingOrder) {
        ViewPager.PageTransformer transformer = null;
        try {
            transformer =  mPagerTransform.newInstance();
            if (getPager() != null && transformer != null) {
                pager.setPageTransformer(reverseDrawingOrder, transformer);
            }
        } catch (Exception e) {
            Log.e("Banner", "Please set the PageTransformer class");
        }
    }

    /**
     * 开始自动轮播
     */
    private void startAutoPlay() {
        stopAutoPlay(); // 避免重复消息
        if (isAutoPlay) {
            handler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        if (visibility == VISIBLE) {
            startAutoPlay();
        } else {
            stopAutoPlay();
        }
    }


    /**
     * 停止自动轮播
     */
    private void stopAutoPlay() {
        if (pager != null) {
            pager.setCurrentItem(pager.getCurrentItem(), false);
        }
        if (isAutoPlay) {
            handler.removeMessages(WHAT_AUTO_PLAY);
            if (pager != null) {
                pager.setCurrentItem(pager.getCurrentItem(), false);
            }
        }

    }

    /**
     * @param autoPlay 是否自动轮播
     */
    public Banner setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
        return this;
    }

    public void onDestory() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAutoPlay();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startAutoPlay();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 切换指示器状态
     *
     * @param currentPosition 当前位置
     */
    private void switchIndicator(int currentPosition) {
        for (int i = 0; i < indicatorContainer.getChildCount(); i++) {
            // ((ImageView) indicatorContainer.getChildAt(i)).setImageDrawable(i == currentPosition ? selectedDrawable : unSelectedDrawable);
            ((ImageView) indicatorContainer.getChildAt(i)).setImageResource(i == currentPosition ? selectedIndicatorResId : unSelectedIndicatorResId);
        }
    }


    public Banner setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
        return this;
    }

    public interface OnBannerItemClickListener {
        void onItemClick(int position, ItemInfo itemData);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    private static class SavedState extends BaseSavedState {
        int currentPosition;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


    private class LoopPagerAdapter extends PagerAdapter {

        private ImageView[] imageViews;

        private List<String> imageUrls;


        private OnClickListener listener;

        private
        LoopPagerAdapter(List<String> urls, int itemCount) {
            this.imageUrls = urls;
            this.itemCount = itemCount;
            listener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBannerItemClickListener != null) {
                        final int localRealPoint = imageUrls.size() == 1 ? 0 : currentRealPosition;
                        if (localRealPoint < 0 || localRealPoint >= imageUrls.size()) return;
                        ItemInfo item = null;
                        if (mBannerData != null && mBannerData.size() > 0 && localRealPoint >= 0 && localRealPoint < mBannerData.size()) {
                            item = mBannerData.get(localRealPoint);
                        }
                        onBannerItemClickListener.onItemClick(localRealPoint, item);
                    }
                }
            };
            if(itemCount>1) {
                imageViews = new ImageView[3];
                imageViews[0] = getImageView(listener);
                imageViews[1] = getImageView(listener);
                imageViews[2] = getImageView(listener);
            }else {
                imageViews = new ImageView[1];
                imageViews[0] = getImageView(listener);
            }
        }

        private int itemCount = Integer.MAX_VALUE;

        @Override
        public int getCount() {
            //Integer.MAX_VALUE = 2147483647
            return itemCount;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //计算真实位置
            int realPosition = position%imageUrls.size();
//            Logs.e("Banner",position%3+"__"+position);
            ImageView imageView = imageViews[position%3];
            //  imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            Logs.e("Banner",imageUrls.get(realPosition));
            imageLoader.displayImage(getContext(), imageUrls.get(realPosition), imageView,realPosition);
            if(imageView.getParent()!=null) {
                ((ViewGroup)imageView.getParent()).removeView(imageView);
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, Object object) {

        }
    }

    public class FixedSpeedScroller extends Scroller {

        private int mDuration = 1000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
            this(context, interpolator);
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

    public interface BannerLoader {
        void displayImage(Context context, String path, ImageView imageView, int position);
    }

    public interface ItemInfo {
        String getUrl();
    }

}


