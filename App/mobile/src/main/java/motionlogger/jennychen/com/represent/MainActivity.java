package motionlogger.jennychen.com.represent;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements LocationListener{
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected String randomized;

    protected Context context;
    protected String inputZip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText zipCode = (EditText) findViewById(R.id.zipcode);

    }

    public void zipSearch(View view) {
        Intent intent_zip = new Intent(this, zipresults.class);
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("TYPE", "showZip");

        startActivity(intent_zip);
        startService(sendIntent);
    }

    public void currentLocation(View view) {
        Intent intent_current = new Intent(this, results.class);
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("TYPE", "show");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");


        }
        intent_current.putExtra("randomized", "false");
        sendIntent.putExtra("RANDOMIZED", "false");
        startActivity(intent_current);
        startService(sendIntent);
    }


    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onLocationChanged(Location location) {

    }
    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
