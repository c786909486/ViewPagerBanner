package axun.com.viewpagerbanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import java.util.ArrayList;
import java.util.List;

import axun.com.banner.Banner;
import axun.com.banner.BannerStyle;

public class MainActivity extends AppCompatActivity {

    private Banner mBannerView;
    private String[] images = new String[]{
            "http://pic27.nipic.com/20130220/11588199_085535217129_2.jpg",
            "http://img15.3lian.com/2015/h1/280/d/11.jpg",
            "http://images.ali213.net/picfile/pic/2014/12/10/927_2014121034411595.jpg"
    };
    private List<String> titles = new ArrayList<>();
    private Banner mBannerView2;
    private Banner mBannerView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titles.add("猫1");
        titles.add("猫2");
        titles.add("狗");
        initView();
    }

    private void initView() {
        mBannerView = (Banner) findViewById(R.id.banner_view);
        mBannerView.setIndicatorGravity(Gravity.RIGHT);
        mBannerView.setIndicatorStyle(BannerStyle.INDICATOR_NUMBER);
        mBannerView.setNetImages(images);

        mBannerView2 = (Banner) findViewById(R.id.banner_view2);
        mBannerView2.setAutoEnable(true);
        mBannerView2.setNetImages(images);


        mBannerView3 = (Banner) findViewById(R.id.banner_view3);
        mBannerView3.setIndicatorStyle(BannerStyle.TITLE_WITHOUT_INDICATOR);
        mBannerView3.setAutoEnable(true);
        mBannerView3.setNetImageWithTitle(images,titles);
    }
}
