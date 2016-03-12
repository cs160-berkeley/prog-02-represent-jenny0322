package motionlogger.jennychen.com.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import io.fabric.sdk.android.Fabric;


/**
 * Created by jenny0322 on 3/1/16.
 */
public class CandidateArrayAdapter extends BaseAdapter {
    protected String[] names;
    protected String[] party;
    protected String[] type;
    protected Context context;
    protected String[] email;
    protected String[] website;
    protected String location;
    protected JSONArray people;
//    protected Tweet[] twits;
    protected String[] twitterID;
    protected String[] bioID;
    private String photoURL = "https://theunitedstates.io/images/congress/original/";

    private static final String TWITTER_KEY = "hBRCvlQPLtfaDOs50VHqumvr1";
    private static final String TWITTER_SECRET = "9jPFxCku7CFurx6gqTVRCZwOvdPvqwQB3V3tpQ9ubqaSuFKvqq";


    public CandidateArrayAdapter(Context context, String[] bioID, String[] twitterID, String[] names, String[] party, String[] type, String[] email, String[] website, JSONArray jArray, String location) {

        this.context = context;
        this.names = names;
        this.bioID = bioID;
        this.party = party;
        this.type = type;
        this.email = email;
        this.website = website;
        this.twitterID = twitterID;
        this.location = location;
        this.people = jArray;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){






        LayoutInflater inflater = (LayoutInflater) context

                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.zipresults_main, parent, false);

        TextView nameView = (TextView) v.findViewById(R.id.name_dynamic);

        final FrameLayout twitterView = (FrameLayout) v.findViewById(R.id.tweet_dynamic);

        TextView partyView = (TextView) v.findViewById(R.id.party_dynamic);

        TextView typeView = (TextView) v. findViewById(R.id.type_dynamic);

        TextView siteView = (TextView) v. findViewById(R.id.site_dynamic);

        TextView emailView = (TextView) v. findViewById(R.id.email_dynamic);



//        TextView twitterView = (TextView) v. findViewById(R.id.twitter_dynamic);
//
//        TextView twitterDetailView = (TextView) v. findViewById(R.id.twitterdetail_dynamic);

        if (twitterID[position]!=null) {
            TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
                @Override
                public void success(Result<AppSession> appSessionResult) {
                    AppSession session = appSessionResult.data;

                    TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                    twitterApiClient.getStatusesService().userTimeline(null, twitterID[position], 1, null, null, false, false, false, true, new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> listResult) {
                            for (Tweet tweet : listResult.data) {
                                twitterView.addView(new CompactTweetView(context, tweet));

                            }
                        }

                        @Override
                        public void failure(TwitterException e) {
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                public void failure(TwitterException e) {
                    e.printStackTrace();
                }
            });
        }else{
            TextView error = new TextView(context);
            error.setText("Oops, "+ names[position] + "does not seem to have presence on Twitter!");
            twitterView.addView(error);
        }



        ImageView avatarView = (ImageView) v.findViewById(R.id.avatar_dynamic);

        nameView.setText(names[position]);

        if (party[position].equals("Republican")) {
            partyView.setTextColor(ContextCompat.getColor(context, R.color.red));

        }

        partyView.setText(party[position]);


        typeView.setText(type[position]);



        siteView.setText(Html.fromHtml("<a href=\"" + website[position] + "\">" + website[position] + "</a>"));
        siteView.setClickable(true);
        siteView.setMovementMethod(LinkMovementMethod.getInstance());

        emailView.setText(Html.fromHtml("<a href=\"" + "mailto:" + email[position] + "\">" + email[position] + "</a>"));

        emailView.setClickable(true);
        emailView.setMovementMethod(LinkMovementMethod.getInstance());


        new DownloadImageTask((ImageView) v.findViewById(R.id.avatar_dynamic))
                    .execute(photoURL+bioID[position]+".jpg");



//        twitterView.setText(twitter[position]);
//
//        twitterDetailView.setText(twitterDetail[position]);

//        pictureView.setBackgroundResource(photos[position]);

        v.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //Send Toast or Launch Activity here
                Intent detail = new Intent(context, detail.class);

                detail.putExtra("location", location);
                System.out.println("results sends " + location);
                try {
                    JSONObject person = people.getJSONObject(position);
                    detail.putExtra("person", person.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                context.startActivity(detail);



            }

        });

        return v;
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            BitmapDrawable ob = new BitmapDrawable(context.getResources(), result);
            bmImage.setBackgroundDrawable(ob);
        }
    }



}

