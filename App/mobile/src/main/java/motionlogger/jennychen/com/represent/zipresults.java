package motionlogger.jennychen.com.represent;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.ListView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class zipresults extends AppCompatActivity {
    protected String[] names;
    protected String[] party;
    protected String[] type;
    protected String[] email;
    protected String[] twitterID;
    protected String[] website;
    protected JSONArray people;
    protected JSONObject person;
    protected String location;
    protected String[] bioID;
    private static final String TWITTER_KEY = "hBRCvlQPLtfaDOs50VHqumvr1";
    private static final String TWITTER_SECRET = "9jPFxCku7CFurx6gqTVRCZwOvdPvqwQB3V3tpQ9ubqaSuFKvqq";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_zipresults);
        Intent intent = getIntent();

        final ListView candidatelist = (ListView) findViewById(R.id.ziplistview);




        try {
            location = intent.getStringExtra("location");
            JSONObject candidates = new JSONObject(intent.getStringExtra("candidates"));
            people = candidates.optJSONArray("results");
            names = new String[people.length()];
            party = new String[people.length()];
            type = new String[people.length()];
            email = new String[people.length()];
            website = new String[people.length()];
            bioID = new String[people.length()];
            twitterID = new String[people.length()];

            for(int i=0; i < people.length(); i++) {

                try {
                    person = people.getJSONObject(i);
                    twitterID[i] = person.optString("twitter_id").toString();
                    bioID[i]= person.optString("bioguide_id").toString();




                    names[i] = person.optString("first_name").toString() + " " + person.optString("last_name").toString();
                    if (person.optString("party").toString().equals("D")) {
                        party[i] = "Democrat";
                    }else{
                        party[i] = "Republican";
                    }
                    if (person.optString("title").toString().equals("Sen")){
                        type[i] = "Senator";
                    }else{
                        type[i] = "Representative";
                    }
                    email[i]= person.optString("oc_email").toString();
                    website[i] = person.optString("website").toString();





                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }








//        int[] photos = {R.drawable.janet_bewley, R.drawable.rob_cowles, R.drawable.scott_allen, R.drawable.scott_allen};
//        String[] twitter = {"Janet Bewley @janetbewey4wi 2h", "Rob Cowles @SenRobCowles 2h","Scott Allen @scott_allen 8h", "Scott Allen @scott_allen 8h" };
//        String[] twitterdetail = {"Congratulations to our new chariwomen, Marth Laning !! Follow her @laningforwi","Retweeted @SenRobCowles meets with #citystadiumauto motive students", "8 Smart Bathroom Design Ideas. These are great! @hgtv", "8 Smart Bathroom Design Ideas. These are great! @hgtv" };



        final CandidateArrayAdapter adapter = new CandidateArrayAdapter(this, bioID, twitterID, names, party, type, email, website, people, location);
        candidatelist.setAdapter(adapter);





    }


}
