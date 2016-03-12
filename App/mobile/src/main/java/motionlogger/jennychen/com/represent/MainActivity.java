package motionlogger.jennychen.com.represent;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.location.Location;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
//    private static final String TWITTER_KEY = "hBRCvlQPLtfaDOs50VHqumvr1";
//    private static final String TWITTER_SECRET = "9jPFxCku7CFurx6gqTVRCZwOvdPvqwQB3V3tpQ9ubqaSuFKvqq";

    private static final String SUNLIGHT_KEY = "174186c2b0da4e1faafad031a3c3030e";
    private static final String SUNLIGHT_URL = "https://congress.api.sunlightfoundation.com/legislators/locate?";
    private static final String REVERSEGEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    private static final String GEOCODING_KEY = "AIzaSyDZhPfE0vCaCTYrB3_gvCp05BQiWY0_QDc";
    private static final String GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";





    protected String officeState;

    protected Context context;
    protected String inputZip;
    protected Location mCurrentLocation;
    private GoogleApiClient mGoogleApiClient;
    protected String currLat;
    protected String currLong;
    protected Intent sendIntent;
    protected Intent intent_location;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    public void zipSearch(View view) {
        final EditText zipCode = (EditText) findViewById(R.id.zipcode);
        inputZip = zipCode.getText().toString();
        intent_location = new Intent(this, zipresults.class);
        sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("TYPE", "showZip");

        new RetrieveZipLocation().execute();
//        new ReverseGeocoding().execute();
//
//
//        intent_location.putExtra("candidates", jObject.toString());
//        sendIntent.putExtra("CANDIDATES", jObject.toString());
//
//        startActivity(intent_location);
//        startService(sendIntent);
    }

    public void currentLocation(View view) {
        intent_location = new Intent(this, results.class);
        sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("TYPE", "show");

        try {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }
        if (mCurrentLocation != null) {
            currLat = String.valueOf(mCurrentLocation.getLatitude());
            currLong = String.valueOf(mCurrentLocation.getLongitude());

        }
        new RetrieveCurrentLocation().execute();





//        intent_current.putExtra("randomized", "false");
//        sendIntent.putExtra("RANDOMIZED", "false");


    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }
class RetrieveCurrentLocation extends AsyncTask<String, Void, String> {
    private Exception exception;
    protected String doInBackground(String... urls) {


        // params comes from the execute() call: params[0] is the url.
        try {
            URL url = new URL(SUNLIGHT_URL + "latitude=" + currLat +"&longitude=" + currLong + "&apikey=" + SUNLIGHT_KEY);
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
            }
            finally{
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }
    @Override
    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }

        Log.i("INFO", response);
        try {
            JSONObject jObject = new JSONObject(response);
            intent_location.putExtra("candidates", jObject.toString());
            sendIntent.putExtra("CANDIDATES", jObject.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ReverseGeocoding().execute();








}

}
    class RetrieveZipLocation extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... urls) {


            // params comes from the execute() call: params[0] is the url.
            try {
                URL url = new URL(SUNLIGHT_URL + "zip=" + inputZip + "&apikey=" + SUNLIGHT_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    try {
                        JSONObject jObject = new JSONObject(stringBuilder.toString());
                        System.out.println(jObject.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
                JSONObject jObject = new JSONObject(response);
                intent_location.putExtra("candidates", jObject.toString());
                sendIntent.putExtra("CANDIDATES", jObject.toString());
                JSONArray people = jObject.optJSONArray("results");
                JSONObject person = people.getJSONObject(0);
                officeState = person.optString("state_name") ;



            } catch (JSONException e) {
                e.printStackTrace();
            }

            new ZipGeocoding().execute();


        }
    }

    class ReverseGeocoding extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... urls) {


            // params comes from the execute() call: params[0] is the url.
            try {
                URL url = new URL(REVERSEGEOCODING_URL + currLat + ","+currLong+ "&key=" + GEOCODING_KEY);
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
            String county = null;
            String state = null;
            try {
                JSONObject jObject_location = new JSONObject(response);
//                JSONArray geoInfo = new JSONArray();
//                JSONObject item = new JSONObject();
//                item.put("county", jObject_location.optString("administrative_area_level_2").toString());
//                item.put("state", jObject_location.optString("administrative_area_level_1").toString());
//                geoInfo.put(item);


                JSONArray results = jObject_location.optJSONArray("results");

                try {
                    JSONObject zero = results.getJSONObject(0);
                    JSONArray address_components = zero.getJSONArray("address_components");

                    for (int i = 0; i < address_components.length(); i++)
                    {
                        JSONObject object = address_components.getJSONObject(i);
                        String long_name = object.getString("long_name");
                        JSONArray mtypes = object.getJSONArray("types");
                        String Type = mtypes.getString(0);
                        if (Type.equalsIgnoreCase("administrative_area_level_2"))
                        {
                            String[] countyArray = long_name.split(" ");
                            county = countyArray[0];
                        }
                        else if (Type.equalsIgnoreCase("administrative_area_level_1"))
                        {
                            state = long_name;



                        }

                    }
                    intent_location.putExtra("location", state + "," + county);
                    sendIntent.putExtra("LOCATION", state + "," + county);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }









            } catch (JSONException e) {
                e.printStackTrace();
            }
            startActivity(intent_location);
            startService(sendIntent);


        }
    }
    class ZipGeocoding extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... urls) {


            // params comes from the execute() call: params[0] is the url.
            try {
                URL url = new URL(GEOCODING_URL + officeState + "components=postal_code:"+inputZip+ "&key=" + GEOCODING_KEY);
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
            String county = null;
            String state = officeState;
            try {
                JSONObject jObject_location = new JSONObject(response);
//                JSONArray geoInfo = new JSONArray();
//                JSONObject item = new JSONObject();
//                item.put("county", jObject_location.optString("administrative_area_level_2").toString());
//                item.put("state", jObject_location.optString("administrative_area_level_1").toString());
//                geoInfo.put(item);


                JSONArray results = jObject_location.optJSONArray("results");

                try {
                    JSONObject zero = results.getJSONObject(0);
                    JSONArray address_components = zero.getJSONArray("address_components");

                    for (int i = 0; i < address_components.length(); i++)
                    {
                        JSONObject object = address_components.getJSONObject(i);
                        String long_name = object.getString("long_name");
                        JSONArray mtypes = object.getJSONArray("types");
                        String Type = mtypes.getString(0);
                        if (Type.equalsIgnoreCase("administrative_area_level_2"))
                        {
                            String[] countyArray = long_name.split(" ");
                            county = countyArray[0];
                        }
                        else if (Type.equalsIgnoreCase("administrative_area_level_1"))
                        {
                            state = long_name;



                        }

                    }
                    intent_location.putExtra("location", state + "," + county);
                    sendIntent.putExtra("LOCATION", state + "," + county);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }









            } catch (JSONException e) {
                e.printStackTrace();
            }
            startActivity(intent_location);
            startService(sendIntent);


        }
    }



}
