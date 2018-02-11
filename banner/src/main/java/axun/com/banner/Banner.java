package axun.com.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/2/8.
 */

public class Banner extends RelativeLayout implements ViewPager.OnPageChangeListener{

    private LinearLayout pointLayout;//圆点指示器布局
    private TextView numberLayout;//数字指示器布局
    private TextView titleLayout;//标题;
    private LinearLayout titleBar;//带指示器的标题栏

    private ViewPager pager;
    private BannerAdapter mAdapter;
    private Context context;
    private List<View> imageViews;//imageview集合
    private List<?> images;//图片地址集合
    private List<String> titles;//标题集合
    private int[] localImages;//本地图片数组
    private int dataLength;//图片数据长度

    private boolean needIndicator = true; //是否需要指示器
    private int gravity = BannerConfig.CENTER;//指示器位置，只对无标题指示器生效
    private int indicatorStyle = BannerStyle.INDICATOR_POINT;//指示器类型
    private int delayTime = 3000;//自动轮播时间
    private boolean isAuto = false;//是否自动轮播
    private int pointDrawable = R.drawable.point_drawable; //圆点图片
    private int numberBg = R.drawable.number_bg; //数字指示器背景
    private int titleBg = R.color.default_bg;//标题背景


    private int currentInx = 0;

    public Banner(@NonNull Context context) {
        this(context,null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViewPager();
    }

    public void setScrollDuration(int scrollDuration){
        if (pager!=null){

            ViewPagerScroller scroller = new ViewPagerScroller(context);
            scroller.setScrollDuration(scrollDuration);
            scroller.initViewPagerScroll(pager);//这个是设置切换过渡时间为2秒
        }
    }

    /**
     * 是否显示指示器
     * @param needIndicator
     */
    public void setNeedIndicator(boolean needIndicator) {
        this.needIndicator = needIndicator;
    }

    /**
     * 设置指示器位置
     * #BannerConfig.LEFT
     * #BannerConfig.RIGHT
     * #BannerConfig.CENTER
     * @param gravity
     */
    public void setIndicatorGravity(int gravity){
        this.gravity = gravity;
    }

    /**
     * 设置指示器样式
     * @param style
     */
    public void setIndicatorStyle(int style){
        this.indicatorStyle = style;
    }

    /**
     * 设置圆点图片
     * @param pointDrawable
     */
    public void setPointDrawable(@DrawableRes int pointDrawable){
        this.pointDrawable = pointDrawable;
    }

    /**
     * 设置数字布局背景
     * @param numberBg
     */
    public void setNumberBg(@DrawableRes int numberBg){
        this.numberBg = numberBg;
    }

    /**
     * 设置自动轮播
     * @param isAuto
     */
    public void setAutoEnable(boolean isAuto){
        this.isAuto = isAuto;
    }

    public void setTransformer(ViewPager.PageTransformer transformer){
        if (pager!=null){
            pager.setPageTransformer(false,transformer);
        }
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager(){
        pager = new ViewPager(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pager.setLayoutParams(layoutParams);
        pager.setOverScrollMode(OVER_SCROLL_NEVER);
        this.addView(pager);
        imageViews = new ArrayList<>();
        mAdapter = new BannerAdapter(imageViews);
        pager.setAdapter(mAdapter);
        mAdapter.setOnBannerClickListener(new BannerAdapter.OnBannerClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (listener!=null){
                    listener.onItemClick(view,position%images.size());
                }
            }
        });
        pager.addOnPageChangeListener(this);
        pager.setCurrentItem(Integer.MAX_VALUE/2);
        pager.setOffscreenPageLimit(0);
    }

    /**
     * 设置标题栏背景
     * @param titleBg
     */
    public void setTitleBg(int titleBg) {
        this.titleBg = titleBg;
    }

    /**
     * 加载图片和标题
     * @param images
     * @param titles
     */
    public void setImageWithTitle(List<String> images, List<String> titles){
        this.images = images;
        this.titles = titles;
        addImage();
    }

    public void setImageWithTitle(String[] images, List<String> titles){
        this.titles = titles;
        setImages(images);
    }

    /**
     * 设置图片
     * @param images
     */
    public void setImages(List<?> images){
        this.images = images;
        addImage();
    }

    /**
     * 设置图片
     * @param images
     */
    public void setImages(String[] images){
        this.images = new ArrayList<>();
        Collections.addAll((Collection<? super String>) this.images, images);
        addImage();
    }

    public void setImages(int[] images){
        this.localImages = images;
        addImage();
    }

    /**
     * 添加imageView到viewPager中
     */
    private void addImage(){
        if (images == null && localImages == null){
            return;
        }
       if (images!=null){
           for (Object image:images){
               ImageView imageView = new ImageView(context);
               imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
               ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
               imageView.setLayoutParams(layoutParams);
               Glide.with(context).load(image).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                       .into(imageView);
               imageViews.add(imageView);
           }
           Log.d("Banner","添加数据"+imageViews.size());
       }else {
           for (int image:localImages){
               ImageView imageView = new ImageView(context);
               imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
               ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
               imageView.setLayoutParams(layoutParams);
               Glide.with(context).load(image).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                       .into(imageView);
               imageViews.add(imageView);
           }
           Log.d("Banner","添加数据"+imageViews.size());
       }
        mAdapter.notifyDataSetChanged();
        dataLength = images == null?localImages.length:images.size();
        setIndicator(dataLength);
        setAutoPlay();
    }

    private Handler handler = new Handler();

    /**
     * 设置自动轮播
     */
    private void setAutoPlay() {
        if (isAuto){
            startAutoPlay();
        }
    }

    /**
     * 开启自动轮播
     */
    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    /**
     * 关闭自动轮播
     */
    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    /**
     * 轮播任务
     */
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (dataLength > 1 && isAuto) {
                int current = pager.getCurrentItem();
                pager.setCurrentItem(current+1);
                handler.postDelayed(task,delayTime);
            }
        }
    };

    /**
     * 手指滑动时间，手指接触时停止轮播，手指离开时开启轮播
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAuto) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 添加指示器
     * @param length
     */
    @SuppressLint("SetTextI18n")
    private void setIndicator(int length){
        if (needIndicator){
            if (indicatorStyle == BannerStyle.INDICATOR_POINT){
                pointLayout = new LinearLayout(context);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.setMargins(16,0,16,25);
                pointLayout.setLayoutParams(layoutParams);
                if (gravity == BannerConfig.CENTER){
                    pointLayout.setGravity(Gravity.CENTER);
                }else if (gravity == BannerConfig.LEFT){
                    pointLayout.setGravity(Gravity.LEFT);
                }else if (gravity == BannerConfig.RIGHT){
                    pointLayout.setGravity(Gravity.RIGHT);
                }
                this.addView(pointLayout);
                if (length>1){
                    for (int i = 0; i<length;i++){
                        View view = new View(context);
                        LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(20,20);
                        view.setLayoutParams(pointParams);
                        pointParams.setMargins(10,0,10,0);
                        view.setBackgroundResource(pointDrawable);
                        pointLayout.addView(view);
                    }
                    pointLayout.getChildAt(0).setSelected(true);
                }

            }else if (indicatorStyle == BannerStyle.INDICATOR_NUMBER){
                numberLayout = new TextView(context);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                if (gravity == BannerConfig.RIGHT){
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }else if (gravity == BannerConfig.LEFT){
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                }else if (gravity == BannerConfig.CENTER){
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                }
                layoutParams.setMargins(16,0,16,25);
                numberLayout.setLayoutParams(layoutParams);
                numberLayout.setPadding(15,15,15,15);
                numberLayout.setBackgroundResource(numberBg);
                numberLayout.setTextColor(Color.WHITE);
                numberLayout.setTextSize(13);
                this.addView(numberLayout);
                numberLayout.setText("1/"+length);
            }else if (indicatorStyle == BannerStyle.TITLE_WITHOUT_INDICATOR){
                if (titles!=null){
                    titleLayout = new TextView(context);
                    titleLayout.setPadding(16,10,16,10);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    titleLayout.setLayoutParams(layoutParams);
                    titleLayout.setBackgroundResource(titleBg);
                    titleLayout.setTextSize(13);
                    titleLayout.setTextColor(Color.WHITE);
                    this.addView(titleLayout);
                    titleLayout.setText(titles.get(0));
                }
            }else if (indicatorStyle == BannerStyle.TITLE_WITH_POINT){
                if (titles!=null){
                    titleBar = new LinearLayout(context);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    titleBar.setOrientation(LinearLayout.HORIZONTAL);
                    titleBar.setBackgroundResource(titleBg);
                    titleBar.setLayoutParams(layoutParams);
                    titleBar.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
                    titleBar.setPadding(16,10,16,10);
                    this.addView(titleBar);
                    //添加标题
                    titleLayout = new TextView(context);
                    LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                    titleLayout.setTextSize(13);
                    titleLayout.setTextColor(Color.WHITE);
                    titleLayout.setText(titles.get(0));
                    titleLayout.setLayoutParams(titleParams);
                    titleBar.addView(titleLayout);
                    //添加point
                    pointLayout = new LinearLayout(context);
                    LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    pointParams.setMargins(16,0,16,0);
                    pointLayout.setLayoutParams(pointParams);
                    titleBar.addView(pointLayout);
                    if (length>1){
                        for (int i = 0; i<length;i++){
                            View view = new View(context);
                            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(20,20);
                            itemParams.setMargins(10,0,10,0);
                            view.setLayoutParams(itemParams);
                            view.setBackgroundResource(pointDrawable);
                            pointLayout.addView(view);
                        }
                        pointLayout.getChildAt(0).setSelected(true);
                    }
                }
            }else if (indicatorStyle == BannerStyle.TITLE_WITH_NUMBER){
                if (titles!=null){
                    titleBar = new LinearLayout(context);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    titleBar.setOrientation(LinearLayout.HORIZONTAL);
                    titleBar.setBackgroundResource(titleBg);
                    titleBar.setLayoutParams(layoutParams);
                    titleBar.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
                    titleBar.setPadding(16,10,16,10);
                    this.addView(titleBar);
                    //添加标题
                    titleLayout = new TextView(context);
                    LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                    titleLayout.setTextSize(13);
                    titleLayout.setTextColor(Color.WHITE);
                    titleLayout.setText(titles.get(0));
                    titleLayout.setLayoutParams(titleParams);
                    titleBar.addView(titleLayout);
                    //添加数字指示器
                    numberLayout = new TextView(context);
                    LinearLayout.LayoutParams numberParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    numberLayout.setTextColor(Color.WHITE);
                    numberLayout.setTextSize(13);
                    numberLayout.setLayoutParams(numberParams);
                    numberLayout.setText("1/"+length);
                    titleBar.addView(numberLayout);
                }
            }
        }
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {

        if (indicatorStyle == BannerStyle.INDICATOR_POINT){
            if (pointLayout != null){
                pointLayout.getChildAt(currentInx).setSelected(false);
                pointLayout.getChildAt(position%dataLength).setSelected(true);
                currentInx = position%dataLength;
            }
        }else if (indicatorStyle == BannerStyle.INDICATOR_NUMBER){
            if (numberLayout !=null){
                numberLayout.setText(((position%dataLength)+1)+"/"+dataLength);
            }
        }else if (indicatorStyle == BannerStyle.TITLE_WITHOUT_INDICATOR){
            if (titleLayout!=null){
                titleLayout.setText(titles.get(position%titles.size()));
            }
        }else if (indicatorStyle == BannerStyle.TITLE_WITH_POINT){
            if (titleLayout!=null){
                titleLayout.setText(titles.get(position%titles.size()));
            }
            if (pointLayout!=null){
                pointLayout.getChildAt(currentInx).setSelected(false);
                pointLayout.getChildAt(position%dataLength).setSelected(true);
                currentInx = position%dataLength;
            }
        }else if (indicatorStyle == BannerStyle.TITLE_WITH_NUMBER){
            if (titleLayout!=null){
                titleLayout.setText(titles.get(position%titles.size()));
            }
            if (numberLayout !=null){
                numberLayout.setText(((position%images.size())+1)+"/"+dataLength);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
