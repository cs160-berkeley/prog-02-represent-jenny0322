package motionlogger.jennychen.com.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class vote extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        double latitude = extras.getDouble("latitude");
        double longitude = extras.getDouble("longitude");


        if (latitude != 0.00 || longitude != (0.00)) {


            TextView obama = (TextView) findViewById(R.id.obama_data);
            TextView location = (TextView) findViewById(R.id.location);
            location.setText("California, Orange County");
            obama.setText("Obama: 15.2%");
            TextView romney = (TextView) findViewById(R.id.romney_data);
            romney.setText("Romney: 59.4%");
        }
    }
}
