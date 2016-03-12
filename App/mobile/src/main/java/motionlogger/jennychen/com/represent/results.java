package motionlogger.jennychen.com.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class results extends AppCompatActivity {
    protected String[] names = new String[3];
    protected String[] party = new String[3];
    protected String[] type = new String[3];
    protected String[] email = new String[3];
    protected String[] website = new String[3];
    protected String[] twitterID = new String[3];
    protected String[] bioID = new String[3];

    protected String location;
    protected JSONObject person;
    protected JSONArray people;
    private static final String TWITTER_KEY = "hBRCvlQPLtfaDOs50VHqumvr1";
    private static final String TWITTER_SECRET = "9jPFxCku7CFurx6gqTVRCZwOvdPvqwQB3V3tpQ9ubqaSuFKvqq";






    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zipresults);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));


        Intent intent = getIntent();
        final ListView candidatelist = (ListView) findViewById(R.id.ziplistview);
//        int[] photos = {R.drawable.janet_bewley, R.drawable.rob_cowles, R.drawable.scott_allen, R.drawable.scott_allen};
//        String[] twitter = {"Janet Bewley @janetbewey4wi 2h", "Rob Cowles @SenRobCowles 2h","Scott Allen @scott_allen 8h", "Scott Allen @scott_allen 8h" };
//        String[] twitterdetail = {"Congratulations to our new chariwomen, Marth Laning !! Follow her @laningforwi","Retweeted @SenRobCowles meets with #citystadiumauto motive students", "8 Smart Bathroom Design Ideas. These are great! @hgtv", "8 Smart Bathroom Design Ideas. These are great! @hgtv" };


        try {
            JSONObject candidates = new JSONObject(intent.getStringExtra("candidates"));
            location = intent.getStringExtra("location");

            people = candidates.optJSONArray("results");

            for(int i=0; i < people.length(); i++) {
                try {
                    person = people.getJSONObject(i);

                    bioID[i] = person.optString("bioguide_id").toString();


                    names[i] = person.optString("first_name").toString() + " " + person.optString("last_name").toString();
                    if (person.optString("party").toString().equals("D")) {
                        party[i] = "Democrat";
                    }else{
                        party[i] = "Republican";
                    }
                    if (person.optString("title").toString().equals("Sen")){
                        type[i] = "Senator";
                    }else{
                        type[i] = person.optString("title").toString();
                    }
                    email[i]= person.optString("oc_email").toString();
                    website[i] = person.optString("website").toString();
                    twitterID[i] = person.optString("twitter_id").toString();



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            } catch (JSONException e) {
            e.printStackTrace();
        }




        final CandidateArrayAdapter adapter = new CandidateArrayAdapter(this, bioID, twitterID, names, party, type, email, website, people, location);
        candidatelist.setAdapter(adapter);


    }


//    public void viewDetail1(View view) {
//
//        Intent detail = new Intent(this, detail.class);
//
//        startActivity(detail);
//
//    }
//    public void viewDetail2(View view) {
//        Intent detail = new Intent(this, detail.class);
//
//
//
//        startActivity(detail);
//
//    }
//    public void viewDetail3(View view) {
//        Intent detail = new Intent(this, detail.class);
//
//
//        startActivity(detail);
//
//    }

}
