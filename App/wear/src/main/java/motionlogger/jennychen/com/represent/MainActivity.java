package motionlogger.jennychen.com.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;

public class MainActivity extends Activity implements SensorEventListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.



    public double longitude;
    public double latitude;
    private TextView mTextView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);








    }

    private void randomLocation() {

            Random r = new Random();
            longitude = 62.2651*r.nextDouble()+(-124.6261);
            latitude = 30.9817*r.nextDouble()+ 18.0056;

//            Toast toast = Toast.makeText(getApplicationContext(), "New latitude is "+latitude, Toast.LENGTH_SHORT);
//            Toast toast1 = Toast.makeText(getApplicationContext(), "New longitude is " + longitude, Toast.LENGTH_SHORT);
//            toast.show();
//            toast1.show();


            Intent shakeIntent = new Intent(getBaseContext(), WatchToPhoneService.class);

            shakeIntent.putExtra("DETAIL", "shake");


            shakeIntent.putExtra("SHAKELOCATION", String.valueOf(longitude) + "," + String.valueOf(latitude) );



            startService(shakeIntent);

    }
    @Override
    public void onSensorChanged(SensorEvent event){
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    randomLocation();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }





    @Override

        protected void onPause() {
            super.onPause();
        mSensorManager.unregisterListener(this);

        }

        @Override
        public void onResume() {
            super.onResume();
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        }










}
