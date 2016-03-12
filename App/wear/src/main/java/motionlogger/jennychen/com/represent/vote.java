package motionlogger.jennychen.com.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class vote extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        TextView obama = (TextView) findViewById(R.id.obama_data);
        TextView location = (TextView) findViewById(R.id.location);
        TextView romney = (TextView) findViewById(R.id.romney_data);



            String[] locationObj = extras.getString("location").split(",");
            String county = locationObj[1];
            String state = locationObj[0];
            location.setText(state + ", " + county + " County");

            String json = null;


            InputStream is = null;
            try {
                is = getAssets().open("election-county-2012.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");

                try {
                    JSONArray jarray = new JSONArray(json);
                    for(int i = 0; i < jarray.length(); i++){
                        JSONObject j = (JSONObject) jarray.get(i);
                        if(j.optString("county-name").equals(county)){
                            obama.setText("Obama: "+j.optString("obama-percentage")+"%");
                            romney.setText("Romney: "+ j.optString("romney-percentage")+"%");

                        }
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }



            } catch (IOException e1) {
                e1.printStackTrace();
            }




















    }
}
