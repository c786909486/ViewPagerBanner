package axun.com.viewpagerbanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import axun.com.banner.Banner;

public class MainActivity extends AppCompatActivity {

    private Banner mBannerView;
    private String[] images = new String[]{
            "http://pic27.nipic.com/20130220/11588199_085535217129_2.jpg",
            "http://img15.3lian.com/2015/h1/280/d/11.jpg",
            "http://images.ali213.net/picfile/pic/2014/12/10/927_2014121034411595.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBannerView = (Banner) findViewById(R.id.banner_view);
        mBannerView.setIndicatorGravity(Gravity.RIGHT);
        mBannerView.setNetImages(images);
//        mBannerView.setListener(new Banner.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(MainActivity.this,"点击了第"+position+"个",Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
