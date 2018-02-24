package axun.com.banner;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * Created by Administrator on 2018/2/9.
 */

public class BannerAdapter extends PagerAdapter {
    private int initPosition = -1;
    private int topPosition = -1;

    private List<View> imageViews;


    public BannerAdapter(List<View> images) {
        this.imageViews = images;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if (imageViews.size() <= 1) {
            View imageView = imageViews.get(topPosition);
            container.addView(imageView);
            return imageView;
        } else {
            /**
             * 初始化状态
             * 向左滑动
             * 向右滑动
             * 由于ViewPager有预加载机制,默认加载一页,因此在第一次初始化的时候,会调用三次这个方法.
             * (第一次： position=1073741823 第二次： position=1073741822 第三次： position=1073741824)
             *
             * 而后续,这个方法仅被执行一次,并且执行的是预加载下一页的请求.
             * */
            Log.e("TAG", "position=" + position);
            if (initPosition == -1) {
                /**
                 * 初始化状态
                 * topPosition 记录第一次初始化的索引位.用于后续作比较判断下次是向右滑动还是向左滑动
                 * initPosition 初始化图片集合的索引值
                 * */
                topPosition = position;
                initPosition = 0;
            } else if (topPosition < position) {
                /**
                 * 向左滑动
                 * 得出偏移量后比较是否超过图片集合的大小
                 * */
                int value = position - topPosition;
                initPosition += value;
                if (initPosition == imageViews.size()) {
                    /**
                     * 滑动到了最后一页
                     * */
                    initPosition = 0;
                } else if (initPosition > imageViews.size()) {
                    /**
                     * 如果超出了图片集合的大小,则 initPosition = 超过的数值
                     * */
                    initPosition = (initPosition - imageViews.size());
                }
                topPosition = position;
            } else if (topPosition > position) {
                int value = topPosition - position;
                initPosition -= value;
                if (initPosition == -1) {
                    /**
                     * 滑动到了第一页
                     * */
                    initPosition = imageViews.size() - 1;
                } else if (initPosition < -1) {
                    /**
                     * 当计算后的值小于了集合大小，则用集合大小减去小于的这部分
                     * */
                    initPosition = (imageViews.size() - (Math.abs(initPosition)));
                }
                topPosition = position;
            }
            Log.e("TAG", "topPosition=" + topPosition);
            Log.e("TAG", "initPosition=" + initPosition);
            /**
             * 只用这句话应该会出现问题
             * */
//        position %= images.size();
//        if (position < 0) {
//            position = position + images.size();
//        }
            View imageView = imageViews.get(initPosition);
            ViewParent parent = imageView.getParent();
            if (parent != null) {
                ViewGroup viewGroup = (ViewGroup) parent;
                viewGroup.removeView(imageView);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v,position);
                }
            });
            container.addView(imageView);
            return imageView;
        }
    }

    private OnBannerClickListener listener;

    public void setOnBannerClickListener(OnBannerClickListener listener) {
        this.listener = listener;
    }

    public interface OnBannerClickListener{
        void onClick(View view,int position);
    }


}
