package motionlogger.jennychen.com.represent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class detail extends AppCompatActivity {

    protected String randomized;
    protected JSONObject person;
    private static final String SUNLIGHT_KEY = "174186c2b0da4e1faafad031a3c3030e";
    private static final String BILL_URL = "https://congress.api.sunlightfoundation.com/bills?sponsor_id=";
    private static final String COMMITTEE_URL = "https://congress.api.sunlightfoundation.com/committees?member_ids=";
    private String photoURL = "https://theunitedstates.io/images/congress/original/";
    private String bioID;
    private TextView committeeView;
    private TextView typeview;
    private TextView partyView;
    private TextView nameView;
    private TextView billView;
    private TextView termView;
    protected String location;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        location = intent.getStringExtra("location");


        nameView = (TextView) findViewById(R.id.name);
        ImageView avatarView = (ImageView) findViewById(R.id.avatar);
        partyView = (TextView) findViewById(R.id.party);
        typeview = (TextView) findViewById(R.id.type);
        committeeView = (TextView) findViewById(R.id.committeeinfo);
        billView = (TextView) findViewById(R.id.billinfo);
        termView = (TextView) findViewById(R.id.serviceterm);

        try {
            person = new JSONObject(intent.getStringExtra("person"));
            bioID = person.optString("bioguide_id");
            nameView.setText(person.optString("first_name").toString() + " " + person.optString("last_name").toString());
            if (person.optString("party").toString().equals("D")) {
                partyView.setText("Democrat");
                partyView.setTextColor(getResources().getColor(R.color.blue));
            }else{
                partyView.setText("Republican");
                partyView.setTextColor(getResources().getColor(R.color.red));
            }if (person.optString("title").toString().equals("Sen")){
                typeview.setText("Senator");
            }else{
                typeview.setText("Representative");
            }
            termView.setText(person.optString("term_end").toString());
            new CommitteeRequest().execute();
            new BillRequest().execute();
            new DownloadImageTask((ImageView)findViewById(R.id.avatar))
                    .execute(photoURL+bioID +".jpg");


        } catch (JSONException e) {
            e.printStackTrace();
        }





    }
    public void showVote(View view) {
        Intent voteIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        voteIntent.putExtra("TYPE", "vote");

        System.out.println("detailview sends " + location);

        voteIntent.putExtra("LOCATION", location);


        startService(voteIntent);
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
            BitmapDrawable ob = new BitmapDrawable(getResources(), result);
            bmImage.setBackgroundDrawable(ob);
        }
    }



    class CommitteeRequest extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... urls) {


            // params comes from the execute() call: params[0] is the url.
            try {
                URL url = new URL(COMMITTEE_URL + bioID+ "&apikey=" + SUNLIGHT_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }

            Log.i("INFO", response);
            try {
                JSONObject committee = new JSONObject(response);
                JSONArray committeeArray = committee.optJSONArray("results");
                StringBuilder committeeDisplay = new StringBuilder();
                for (int i = 0; i < committee.length(); i++) {
                    JSONObject jObject = committeeArray.getJSONObject(i);
                    committeeDisplay.append("- ").append(jObject.optString("name")).append("\n");
                }

                committeeView.setText(committeeDisplay.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
    }

    class BillRequest extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... urls) {


            // params comes from the execute() call: params[0] is the url.
            try {
                System.out.println(bioID);
                URL url = new URL(BILL_URL + bioID+ "&apikey=" + SUNLIGHT_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }

            Log.i("INFO", response);
            try {

                JSONObject bill = new JSONObject(response);
                JSONArray billArray = bill.optJSONArray("results");
                StringBuilder billDisplay = new StringBuilder();
                for (int i = 0; i < bill.length(); i++) {
                    JSONObject jObject = billArray.getJSONObject(i);
                    if (!jObject.optString("short_title").equals("null")){

                        billDisplay.append("- ").append(jObject.optString("short_title")).append(" (").append(jObject.optString("last_action_at")).append(")").append("\n");
                    }else{
                        billDisplay.append("- ").append(jObject.optString("official_title")).append(" (").append(jObject.optString("last_action_at")).append(")").append("\n");
                    }



                }


                billView.setText(billDisplay.toString());




            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
    }


}
