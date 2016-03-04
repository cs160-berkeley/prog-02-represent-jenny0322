package motionlogger.jennychen.com.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

public class Show extends FragmentActivity {
    public static double latitude;
    public static double longitude;
    private static int NUM_PAGES;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        latitude = extras.getDouble("latitude");
        longitude = extras.getDouble("longitude");

        if (longitude == 0.00 || latitude == 0.00) {

            NUM_PAGES = 4;

        }else {
            NUM_PAGES = 3;
        }


        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        }else{
            mPager.setCurrentItem(mPager.getCurrentItem() -1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {

            return NUM_PAGES;
        }
    }
    public void displayVote(View view) {
        Intent voteIntent = new Intent(this, vote.class);
        voteIntent.putExtra("longitude", longitude);
        voteIntent.putExtra("longitude", longitude);
        startActivity(voteIntent);
    }
    public void showDetail(View view) {
        Intent detailIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
        detailIntent.putExtra("DETAIL", "detail");
        if (mPager.getCurrentItem() == 0) {

            detailIntent.putExtra("NAME", "Janet Bewley");
        }else if (mPager.getCurrentItem() == 1) {

            detailIntent.putExtra("NAME", "Robert Cowles");
        }else{

            detailIntent.putExtra("NAME", "Scott Allen");
        }if (longitude == 0.00 || latitude== 0.00) {
            detailIntent.putExtra("RANDOMIZED", "false");
        }else{
            detailIntent.putExtra("RANDOMIZED", "true");
        }

        startService(detailIntent);
    }
}
