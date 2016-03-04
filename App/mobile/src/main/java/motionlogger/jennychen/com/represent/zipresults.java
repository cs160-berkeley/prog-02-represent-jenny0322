package motionlogger.jennychen.com.represent;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class zipresults extends AppCompatActivity {
    protected static String extra_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zipresults);
        Intent intent = getIntent();

        final ListView candidatelist = (ListView) findViewById(R.id.ziplistview);

        String [] names = {"Janet Bewley", "Robert Cowles", "Scott Allen", "Scott Allen"};
        int[] photos = {R.drawable.janet_bewley, R.drawable.rob_cowles, R.drawable.scott_allen, R.drawable.scott_allen};
        String[] party = {"Democrat", "Republican","Republican", "Republican" };
        String[] type = {"Senator","Senator", "Rep.", "Rep." };
        String[] email = {"sen.bewley@legis.wi.gov","sen.cowles@legis.wi.gov", "rep.allen@legis.wi.gov", "rep.allen@legis.wi.gov" };
        String[] website = {"http://legis.wisconsin.gov/senate/25/bewley","http://legis.wisconsin.gov/senate/cowles/Pages/default.aspx", "http://legis.wisconsin.gov/assembly/97/allen", "http://legis.wisconsin.gov/assembly/97/allen" };
        String[] twitter = {"Janet Bewley @janetbewey4wi 2h", "Rob Cowles @SenRobCowles 2h","Scott Allen @scott_allen 8h", "Scott Allen @scott_allen 8h" };
        String[] twitterdetail = {"Congratulations to our new chariwomen, Marth Laning !! Follow her @laningforwi","Retweeted @SenRobCowles meets with #citystadiumauto motive students", "8 Smart Bathroom Design Ideas. These are great! @hgtv", "8 Smart Bathroom Design Ideas. These are great! @hgtv" };



        final CandidateArrayAdapter adapter = new CandidateArrayAdapter(this, names, photos, party, type, email, website, twitter, twitterdetail);
        candidatelist.setAdapter(adapter);





    }


}
