package axun.com.banner;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/2/8.
 */

public class Banner extends RelativeLayout implements ViewPager.OnPageChangeListener{

    private LinearLayout pointLayout;
    private BannerViewPager pager;
    private BannerAdapter mAdapter;
    private Context context;
    private List<View> imageViews;
    private List<String> images;
    private boolean needIndicator = true;
    private int gravity = Gravity.CENTER;
    private int currentInx = 0;
    private int indicatorStyle = BannerStyle.INDICATOR_POINT;
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

    public void setNeedIndicator(boolean needIndicator) {
        this.needIndicator = needIndicator;
    }

    public void setIndicatorGravity(int gravity){
        this.gravity = gravity;
    }

    public void setIndicatorStyle(int style){
        this.indicatorStyle = style;
    }

    private void initViewPager(){
        pager = new BannerViewPager(context);
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
                    listener.onItemClick(view,position);
                }
            }
        });
        pager.addOnPageChangeListener(this);

    }

    public void setNetImages(List<String> images){
        this.images = images;
        addNetImage();
    }

    public void setNetImages(String[] images){
        this.images = new ArrayList<>();
        Collections.addAll(this.images, images);
        addNetImage();
    }

    private void addNetImage(){
        if (images == null){
            return;
        }
        for (String image:images){
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            Glide.with(context).load(image).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
            imageViews.add(imageView);
        }
        Log.d("Banner","添加数据"+imageViews.size());
        mAdapter.notifyDataSetChanged();
        setIndicator();
    }

    private void setIndicator(){
        if (needIndicator){
            if (indicatorStyle == BannerStyle.INDICATOR_POINT){
                pointLayout = new LinearLayout(context);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.setMargins(16,0,16,25);
                pointLayout.setLayoutParams(layoutParams);
                pointLayout.setGravity(gravity);
                this.addView(pointLayout);
                if (images.size()>0){
                    for (int i = 0; i<images.size();i++){
                        View view = new View(context);
                        LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(20,20);
                        view.setLayoutParams(pointParams);
                        pointParams.setMargins(10,0,10,0);
                        view.setBackgroundResource(R.drawable.point_drawable);
                        pointLayout.addView(view);
                    }
                }
                pager.setCurrentItem(0);
                pointLayout.getChildAt(0).setSelected(true);
            }else if (indicatorStyle == BannerStyle.INDICATOR_NUMBER){

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
                pointLayout.getChildAt(position).setSelected(true);
                currentInx = position;
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

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
