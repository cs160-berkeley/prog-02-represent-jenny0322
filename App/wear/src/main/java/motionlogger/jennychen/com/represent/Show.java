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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Show extends FragmentActivity {
    public String location;
    private static int NUM_PAGES;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    protected JSONArray people;
    JSONObject currPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        location = extras.getString("location");
        try {
            JSONObject candidates = new JSONObject(intent.getStringExtra("candidates"));
            people = candidates.optJSONArray("results");
            NUM_PAGES = people.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), people);
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
        private JSONArray candidates;

        public ScreenSlidePagerAdapter(FragmentManager fm, JSONArray people) {
            super(fm);
            this.candidates = people;
        }

        @Override
        public Fragment getItem(int position) {

            try {
                currPerson = candidates.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }return ScreenSlidePageFragment.create(position, currPerson);

        }

        @Override
        public int getCount() {

            return NUM_PAGES;
        }
    }

    public void showDetail(View view) {
        Intent detailIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
        detailIntent.putExtra("DETAIL", "detail");
        int currIndex = mPager.getCurrentItem();
        try {
            detailIntent.putExtra("PERSON", people.getJSONObject(currIndex).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        if (mPager.getCurrentItem() == 0) {
//
//            detailIntent.putExtra("NAME", "Janet Bewley");
//        }else if (mPager.getCurrentItem() == 1) {
//
//            detailIntent.putExtra("NAME", "Robert Cowles");
//        }else{
//
//            detailIntent.putExtra("NAME", "Scott Allen");
//        }
            detailIntent.putExtra("LOCATION", location);




        startService(detailIntent);
    }

    public void displayVote(View view) {
        Intent voteIntent = new Intent(this, vote.class);
        voteIntent.putExtra("location", location);
        startActivity(voteIntent);
    }
}
