package axun.com.viewpagerbanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import axun.com.banner.Banner;
import axun.com.banner.BannerStyle;
import axun.com.banner.transform.DepthPageTransformer;
import axun.com.banner.transform.ZoomOutPageTransformer;

public class MainActivity extends AppCompatActivity {

    private Banner mBannerView;
    private String[] images = new String[]{
            "http://pic27.nipic.com/20130220/11588199_085535217129_2.jpg",
            "http://img15.3lian.com/2015/h1/280/d/11.jpg",
            "http://images.ali213.net/picfile/pic/2014/12/10/927_2014121034411595.jpg"
    };

    private int[] localImages = new int[]{
            R.mipmap.animal_1,
            R.mipmap.animal_2,
            R.mipmap.animal_3
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
        mBannerView.setScrollDuration(1000);
        mBannerView.setImages(images);
        mBannerView.setOnItemClickListener(new Banner.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this,"点击了第"+(position+1)+"个",Toast.LENGTH_SHORT).show();
            }
        });

        mBannerView2 = (Banner) findViewById(R.id.banner_view2);
        mBannerView2.setAutoEnable(true);
        mBannerView2.setScrollDuration(1000);
        mBannerView2.setTransformer(new ZoomOutPageTransformer());
        mBannerView2.setImages(localImages);


        mBannerView3 = (Banner) findViewById(R.id.banner_view3);
        mBannerView3.setIndicatorStyle(BannerStyle.TITLE_WITH_NUMBER);
        mBannerView3.setAutoEnable(true);
        mBannerView3.setScrollDuration(1000);
        mBannerView3.setTransformer(new DepthPageTransformer());
        mBannerView3.setImageWithTitle(images,titles);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBannerView.stopAutoPlay();
        mBannerView2.stopAutoPlay();
        mBannerView3.stopAutoPlay();
    }
}
