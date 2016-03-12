package motionlogger.jennychen.com.represent;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class PhoneListenerService extends WearableListenerService {
    private static final String DETAIL = "/detail";
    private static final String SHAKE = "/shake";
    private String currLat;
    private String currLong;
    private static final String SUNLIGHT_KEY = "174186c2b0da4e1faafad031a3c3030e";
    private static final String SUNLIGHT_URL = "https://congress.api.sunlightfoundation.com/legislators/locate?";
    private static final String GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    private static final String GEOCODING_KEY = "AIzaSyDZhPfE0vCaCTYrB3_gvCp05BQiWY0_QDc";
    private Intent showShake;
    protected Intent sendIntent;





    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String person;
        String location;


        if( messageEvent.getPath().equalsIgnoreCase(DETAIL) ) {

            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, detail.class );
            String[] message = value.split(";");


            person = message[0];
            location = message[1];
            intent.putExtra("location", location);
            intent.putExtra("person", person);
            System.out.println("phone listener received " + location);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //you need to add this flag since you're starting a new activity from a service




            startActivity(intent);

            // Value contains the String we sent over in WatchToPhoneService, "good job"
//            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            // Make a toast with the String
//            Context context = getApplicationContext();
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, value, duration);
//            toast.show();

            // so you may notice this crashes the phone because it's
            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
            // replace sending a toast with, like, starting a new activity or something.
            // who said skeleton code is untouchable? #breakCSconceptions



        }else if( messageEvent.getPath().equalsIgnoreCase(SHAKE) ) {

            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            showShake = new Intent(this, results.class);
            sendIntent = new Intent(this, PhoneToWatchService.class);
            sendIntent.putExtra("TYPE", "show");

            showShake.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String[] coordinates = value.split(",");
            currLong = coordinates[0];
            currLat = coordinates[1];

            new RetrieveShake().execute();




        }else {
            super.onMessageReceived( messageEvent );
        }

    }
    class RetrieveShake extends AsyncTask<String, Void, String> {
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



                showShake.putExtra("candidates", jObject.toString());
                sendIntent.putExtra("CANDIDATES", jObject.toString());

                new ReverseGeocoding().execute();



            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

    class ReverseGeocoding extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... urls) {


            // params comes from the execute() call: params[0] is the url.
            try {
                URL url = new URL(GEOCODING_URL + currLat + ","+currLong+ "&key=" + GEOCODING_KEY);
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
                    showShake.putExtra("location", state + "," + county);
                    sendIntent.putExtra("LOCATION", state + "," + county);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }









            } catch (JSONException e) {
                e.printStackTrace();
            }
            startActivity(showShake);
            startService(sendIntent);


        }
    }



}
