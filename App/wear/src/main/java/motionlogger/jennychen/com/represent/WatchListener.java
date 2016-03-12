package motionlogger.jennychen.com.represent;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class WatchListener extends WearableListenerService {
    private static final String VOTE = "/Vote";
    private static final String SHOW = "/Show";
    private static final String SHOWZIP = "/ShowZip";

    public void onMessageReceived(MessageEvent messageEvent) {

        if( messageEvent.getPath().equalsIgnoreCase( VOTE ) ) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Intent intent = new Intent(this, vote.class );

            intent.putExtra("location", value);
            System.out.println("Watch listener received" + value);



            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service

            startActivity(intent);
        } else if (messageEvent.getPath().equalsIgnoreCase( SHOW ) || messageEvent.getPath().equalsIgnoreCase( SHOWZIP )){

            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            String[] values = value.split(";");
            String candidates = values[0];
            String location = values[1];
            System.out.println("watchListener receives from show" + location);
            Intent intent = new Intent(this, Show.class );

            try {
                JSONObject candidateObj = new JSONObject(candidates);
                intent.putExtra("candidates", candidateObj.toString());
                intent.putExtra("location", location);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);



            //you need to add this flag since you're starting a new activity from a service


            startActivity(intent);

        } else {
                super.onMessageReceived( messageEvent );

        }

    }



}
