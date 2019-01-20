package com.hansalhalk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ImagePagerAdapter mImagePagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mViewPager = findViewById(R.id.view_pager);
        mImagePagerAdapter = new ImagePagerAdapter();
        mViewPager.setAdapter(mImagePagerAdapter);

    }

    public void getStarted(View view) {
        if ( HelperClass.isOnline(this) ){
            Uri uri = Uri.parse("http://www.hansalhalk.com/");
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(websiteIntent);
        }
        else
            Toast.makeText(this, "من فضلك تأكد من إتصالك بالإنترنت !!!", Toast.LENGTH_SHORT).show();
    }

    public void haveAccount(View view) {
            Intent loginIntent = new Intent(SecondActivity.this, LoginActivity.class);
            startActivity(loginIntent);
    }

    public void callPhone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+201111125248"));
        startActivity(intent);
    }

//    public void onBackPressed() {
//        moveTaskToBack(true);
//    }

    private class ImagePagerAdapter extends PagerAdapter {

        private int[] mImages = new int[] {
                R.drawable.slider1,
                R.drawable.slider2,
                R.drawable.slider3,
                R.drawable.slider4,
                R.drawable.slider5
        };

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = SecondActivity.this;
            ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(
                    R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(mImages[position]);
            container.addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }

}
