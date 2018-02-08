package axun.com.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/2/8.
 */

public class BannerViewPager extends ViewPager {
    private boolean scrollable = true;

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.scrollable){
            if (getCurrentItem() == 0 && getChildCount() == 0){
                return false;
            }
            return super.onTouchEvent(ev);
        }else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.scrollable){
            if (getCurrentItem() == 0 && getChildCount() == 0){
                return false;
            }
            return super.onInterceptTouchEvent(ev);
        }else {
            return false;
        }
    }
}
