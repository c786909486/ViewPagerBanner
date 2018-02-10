package axun.com.banner;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/2/9.
 */

public class BannerAdapter extends PagerAdapter {

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
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if (imageViews.get(position % imageViews.size()).getParent() != null) {
            ((ViewPager) imageViews.get(position % imageViews.size())
                    .getParent()).removeView(imageViews.get(position
                    % imageViews.size()));
        }
        View view = imageViews.get(position % imageViews.size());
        ((ViewPager) container).addView(view, 0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v,position);
            }
        });
        return view;
    }

    private OnBannerClickListener listener;

    public void setOnBannerClickListener(OnBannerClickListener listener) {
        this.listener = listener;
    }

    public interface OnBannerClickListener{
        void onClick(View view,int position);
    }

}
