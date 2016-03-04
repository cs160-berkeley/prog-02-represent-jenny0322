package motionlogger.jennychen.com.represent;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

public class WatchListener extends WearableListenerService {
    private static final String VOTE = "/Vote";
    private static final String SHOW = "/Show";
    private static final String SHOWZIP = "/ShowZip";

    public void onMessageReceived(MessageEvent messageEvent) {

        if( messageEvent.getPath().equalsIgnoreCase( VOTE ) ) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Intent intent = new Intent(this, vote.class );
            if (value.equals("true")) {
                intent.putExtra("longitude", 10.00);
                intent.putExtra("latitude", 10.00);
            }else {
                intent.putExtra("longitude", 0.00);
                intent.putExtra("latitude", 0.00);

            }


            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service

            startActivity(intent);
        } else if (messageEvent.getPath().equalsIgnoreCase( SHOW )) {

            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, Show.class );

            if (value.equals("true")) {
                intent.putExtra("longitude", 10.00);
                intent.putExtra("latitude", 10.00);
            }else {
                intent.putExtra("longitude", 0.00);
                intent.putExtra("latitude", 0.00);

            }
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);



            //you need to add this flag since you're starting a new activity from a service


            startActivity(intent);

        } else if (messageEvent.getPath().equalsIgnoreCase( SHOWZIP)) {

            Intent intent = new Intent(this, Show.class);
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("longitude", 0.00);
            intent.putExtra("latitude", 0.00);

            //you need to add this flag since you're starting a new activity from a service


            startActivity(intent);
        }else {
                super.onMessageReceived( messageEvent );

        }

    }



}
